<?php
/**
 * MUG - Monitor Universal de Glucosa (Estilo SugarDash / SugarPixel)
 * Backend proxy para LibreLinkUp API (v4.16.0+) y Nightscout (compatible con PHP 7.x / 8.x)
 */
@ini_set('display_errors', '0');
error_reporting(0);
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$configFile = __DIR__ . '/config.json';
$cacheFile  = __DIR__ . '/token_cache.json';
$debugFile  = __DIR__ . '/llu_debug.json';

function loadJson($file, $default = array()) {
    if (!file_exists($file)) return $default;
    $raw = @file_get_contents($file);
    $data = @json_decode($raw, true);
    return is_array($data) ? $data : $default;
}

function saveJson($file, $data) {
    @file_put_contents($file, json_encode($data, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
}

$action = isset($_GET['action']) ? $_GET['action'] : 'status';

if ($action === 'save_config' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    if (!is_array($input)) {
        http_response_code(400);
        echo json_encode(array('ok' => false, 'error' => 'Datos inválidos'));
        exit;
    }
    $current = loadJson($configFile, array());
    $merged = array_merge($current, $input);
    saveJson($configFile, $merged);
    // Limpiar caché de token al guardar cambios
    if (file_exists($cacheFile)) @unlink($cacheFile);
    echo json_encode(array('ok' => true, 'config' => array(
        'source' => isset($merged['source']) ? $merged['source'] : 'demo',
        'llu_email' => isset($merged['llu_email']) ? $merged['llu_email'] : '',
        'llu_region' => isset($merged['llu_region']) ? $merged['llu_region'] : 'ae',
        'ns_url' => isset($merged['ns_url']) ? $merged['ns_url'] : '',
        'patient_name' => isset($merged['patient_name']) ? $merged['patient_name'] : 'Paciente',
        'low_threshold' => isset($merged['low_threshold']) ? intval($merged['low_threshold']) : 70,
        'urgent_low' => isset($merged['urgent_low']) ? intval($merged['urgent_low']) : 55,
        'high_threshold' => isset($merged['high_threshold']) ? intval($merged['high_threshold']) : 180,
        'urgent_high' => isset($merged['urgent_high']) ? intval($merged['urgent_high']) : 250,
        'stale_minutes' => isset($merged['stale_minutes']) ? intval($merged['stale_minutes']) : 5
    )));
    exit;
}

$config = loadJson($configFile, array(
    'source' => 'demo',
    'llu_email' => '',
    'llu_password' => '',
    'llu_region' => 'ae',
    'ns_url' => '',
    'ns_token' => '',
    'patient_name' => 'FreeStyle Libre',
    'low_threshold' => 70,
    'urgent_low' => 55,
    'high_threshold' => 180,
    'urgent_high' => 250,
    'stale_minutes' => 5
));

if ($action === 'get_config') {
    $safe = $config;
    $safe['llu_password'] = !empty($config['llu_password']) ? '********' : '';
    echo json_encode(array('ok' => true, 'config' => $safe));
    exit;
}

/**
 * Helper cURL para LibreLinkUp y Nightscout
 */
function httpRequest($url, $method = 'GET', $headers = array(), $body = null) {
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 15);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, $method);
    if ($body !== null) {
        curl_setopt($ch, CURLOPT_POSTFIELDS, $body);
    }
    if (!empty($headers)) {
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    }
    $resp = curl_exec($ch);
    $code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    $err  = curl_error($ch);
    return array('code' => $code, 'body' => $resp, 'error' => $err);
}

/**
 * Extrae el valor en mg/dL desde un objeto de lectura de LibreLinkUp
 */
function extractMgDl($item) {
    if (!is_array($item)) return 0;
    if (!empty($item['ValueInMgPerDl'])) return intval(round($item['ValueInMgPerDl']));
    if (!empty($item['Value'])) {
        $v = floatval($item['Value']);
        // Si viene en mmol/L (< 35), convertir a mg/dL (* 18)
        return ($v < 35) ? intval(round($v * 18.0182)) : intval(round($v));
    }
    return 0;
}

/**
 * Fetch desde Nightscout API v1
 */
function fetchNightscout($config) {
    $baseUrl = rtrim($config['ns_url'], '/');
    if (empty($baseUrl)) {
        return array('ok' => false, 'auth_error' => true, 'error' => 'Ingresa una URL válida de Nightscout.');
    }
    $url = $baseUrl . '/api/v1/entries/sgv.json?count=36';
    if (!empty($config['ns_token'])) {
        $url .= '&token=' . urlencode($config['ns_token']);
    }
    $res = httpRequest($url, 'GET', array('Accept: application/json'));
    if ($res['code'] !== 200 || empty($res['body'])) {
        return array('ok' => false, 'auth_error' => true, 'error' => 'No se pudo conectar a Nightscout. Verifica la URL y el token.');
    }
    $entries = json_decode($res['body'], true);
    if (!is_array($entries)) {
        return array('ok' => false, 'auth_error' => true, 'error' => 'Respuesta inválida desde Nightscout. Verifica la URL.');
    }
    if (count($entries) === 0) {
        return array('ok' => false, 'auth_ok' => true, 'stale' => true, 'error' => 'Sin Información de Sensor');
    }
    $latest = $entries[0];
    $prev   = isset($entries[1]) ? $entries[1] : null;
    $sgv    = intval($latest['sgv']);
    $delta  = $prev ? ($sgv - intval($prev['sgv'])) : 0;

    $dirMap = array(
        'DoubleUp' => '↑↑',
        'SingleUp' => '↑',
        'FortyFiveUp' => '↗',
        'Flat' => '→',
        'FortyFiveDown' => '↘',
        'SingleDown' => '↓',
        'DoubleDown' => '↓↓'
    );
    $dir = isset($latest['direction']) ? $latest['direction'] : 'Flat';
    $arrow = isset($dirMap[$dir]) ? $dirMap[$dir] : '→';

    $history = array();
    foreach (array_reverse($entries) as $e) {
        if (isset($e['sgv'])) {
            $ts = isset($e['date']) ? intval($e['date'] / 1000) : time();
            $history[] = array(
                'time' => $ts,
                'label' => date('H:i', $ts),
                'value' => intval($e['sgv'])
            );
        }
    }

    $timestamp = isset($latest['date']) ? intval($latest['date'] / 1000) : time();
    return array(
        'ok' => true,
        'source' => 'nightscout',
        'patient' => $config['patient_name'],
        'glucose' => $sgv,
        'delta' => $delta,
        'arrow' => $arrow,
        'trend_code' => $dir,
        'timestamp' => $timestamp,
        'minutes_ago' => max(0, round((time() - $timestamp) / 60)),
        'history' => $history
    );
}

/**
 * Resuelve la URL regional oficial de LibreLinkUp usando /llu/config/country
 */
function resolveRegionalHost($region, $headers) {
    $cfgRes = httpRequest('https://api.libreview.io/llu/config/country?country=DE', 'GET', $headers);
    $cfgData = @json_decode($cfgRes['body'], true);
    if (!empty($cfgData['data']['RegionalMap'][$region]['lslApi'])) {
        return rtrim($cfgData['data']['RegionalMap'][$region]['lslApi'], '/');
    }
    return "https://api-{$region}.libreview.io";
}

/**
 * Fetch desde LibreLinkUp (Follower API de FreeStyle Libre v4.16.0)
 */
function fetchLibreLinkUp($config, $cacheFile, $debugFile) {
    $email = trim($config['llu_email']);
    $pass  = $config['llu_password'];
    $region = !empty($config['llu_region']) ? $config['llu_region'] : 'ae';

    if (empty($email) || empty($pass)) {
        return array('ok' => false, 'auth_error' => true, 'error' => 'Ingresa tu correo y contraseña de LibreLinkUp.');
    }

    $cache = loadJson($cacheFile, array());
    $lluVersion = !empty($cache['llu_version']) ? $cache['llu_version'] : '4.16.0';
    $token = isset($cache['token']) ? $cache['token'] : '';
    $accountIdHash = isset($cache['account_id_hash']) ? $cache['account_id_hash'] : '';
    $host = !empty($cache['host']) ? $cache['host'] : (($region === 'global') ? 'https://api.libreview.io' : "https://api-{$region}.libreview.io");

    $buildHeaders = function($ver, $jwt = '', $accId = '') {
        $h = array(
            'Accept: application/json',
            'Content-Type: application/json',
            'product: llu.android',
            'version: ' . $ver,
            'Cache-Control: no-cache',
            'User-Agent: Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36'
        );
        if (!empty($jwt)) $h[] = 'Authorization: Bearer ' . $jwt;
        if (!empty($accId)) $h[] = 'Account-Id: ' . $accId;
        return $h;
    };

    // 1. Login si no hay token válido
    if (empty($token) || empty($cache['expires']) || $cache['expires'] < time()) {
        $loginRes = httpRequest(
            $host . '/llu/auth/login',
            'POST',
            $buildHeaders($lluVersion),
            json_encode(array('email' => $email, 'password' => $pass))
        );
        if ($loginRes['code'] === 0) {
            return array('ok' => false, 'auth_error' => true, 'error' => 'Sin conexión a internet para conectar con LibreLinkUp.');
        }
        $loginData = @json_decode($loginRes['body'], true);

        // Si Abbott exige una versión mínima más reciente (ej. 4.17.0)
        if (!empty($loginData['data']['minimumVersion'])) {
            $lluVersion = $loginData['data']['minimumVersion'];
            $loginRes = httpRequest(
                $host . '/llu/auth/login',
                'POST',
                $buildHeaders($lluVersion),
                json_encode(array('email' => $email, 'password' => $pass))
            );
            $loginData = @json_decode($loginRes['body'], true);
        }

        // Si Abbott pide redirección regional (redirect: true, region: "...")
        if (!empty($loginData['data']['redirect']) && !empty($loginData['data']['region'])) {
            $newRegion = $loginData['data']['region'];
            $host = resolveRegionalHost($newRegion, $buildHeaders($lluVersion));
            $loginRes = httpRequest(
                $host . '/llu/auth/login',
                'POST',
                $buildHeaders($lluVersion),
                json_encode(array('email' => $email, 'password' => $pass))
            );
            $loginData = @json_decode($loginRes['body'], true);
        }

        if (isset($loginData['status']) && intval($loginData['status']) === 4) {
            return array('ok' => false, 'auth_error' => true, 'error' => 'Debes abrir la app oficial LibreLinkUp en tu teléfono y aceptar los nuevos Términos de Uso.');
        }

        if (empty($loginData['data']['authTicket']['token'])) {
            saveJson($debugFile, array('step' => 'login', 'http_code' => $loginRes['code'], 'curl_error' => $loginRes['error'], 'response_status' => isset($loginData['status']) ? $loginData['status'] : null));
            return array('ok' => false, 'auth_error' => true, 'error' => 'Correo o contraseña incorrectos en LibreLinkUp. Verifica tus datos.');
        }

        $token = $loginData['data']['authTicket']['token'];
        $expires = isset($loginData['data']['authTicket']['expires']) ? intval($loginData['data']['authTicket']['expires']) : (time() + 3600);
        $userId = isset($loginData['data']['user']['id']) ? $loginData['data']['user']['id'] : '';
        $accountIdHash = !empty($userId) ? hash('sha256', $userId) : '';

        saveJson($cacheFile, array(
            'token' => $token,
            'expires' => $expires,
            'account_id_hash' => $accountIdHash,
            'host' => $host,
            'llu_version' => $lluVersion
        ));
    }

    // 2. Obtener conexiones de pacientes
    $connRes = httpRequest($host . '/llu/connections', 'GET', $buildHeaders($lluVersion, $token, $accountIdHash));
    $connData = @json_decode($connRes['body'], true);

    if (!empty($connData['data']['minimumVersion']) || (isset($connData['status']) && intval($connData['status']) !== 0) || !isset($connData['data'][0])) {
        if (!empty($connData['data']['minimumVersion'])) {
            $lluVersion = $connData['data']['minimumVersion'];
        }
        @unlink($cacheFile);
        if (empty($connData['data']) || !isset($connData['data'][0])) {
            return array('ok' => false, 'auth_error' => true, 'error' => 'Esta cuenta de LibreLinkUp no tiene pacientes vinculados activos.');
        }
    }

    // Elegir el paciente activo (que tenga medición disponible)
    $patient = $connData['data'][0];
    foreach ($connData['data'] as $cand) {
        if (!empty($cand['glucoseMeasurement']) || !empty($cand['glucoseItem'])) {
            $patient = $cand;
            break;
        }
    }

    $patientId = isset($patient['patientId']) ? $patient['patientId'] : '';
    $patientName = trim((isset($patient['firstName']) ? $patient['firstName'] : '') . ' ' . (isset($patient['lastName']) ? $patient['lastName'] : ''));

    // 3. Obtener gráfico e historial del paciente
    $graphRes = httpRequest($host . '/llu/connections/' . urlencode($patientId) . '/graph', 'GET', $buildHeaders($lluVersion, $token, $accountIdHash));
    $graphData = @json_decode($graphRes['body'], true);

    $connNode = isset($graphData['data']['connection']) ? $graphData['data']['connection'] : array();
    $graphPoints = (isset($graphData['data']['graphData']) && is_array($graphData['data']['graphData'])) ? $graphData['data']['graphData'] : array();

    // Buscar la lectura actual en curso (si el sensor no tiene lectura actual, debe mostrar -.-)
    $current = null;
    if (!empty($connNode['glucoseMeasurement'])) {
        $current = $connNode['glucoseMeasurement'];
    } elseif (!empty($connNode['glucoseItem'])) {
        $current = $connNode['glucoseItem'];
    } elseif (!empty($patient['glucoseMeasurement'])) {
        $current = $patient['glucoseMeasurement'];
    } elseif (!empty($patient['glucoseItem'])) {
        $current = $patient['glucoseItem'];
    }

    // Guardar diagnóstico sin datos sensibles para depuración
    saveJson($debugFile, array(
        'step' => 'graph_ok_check',
        'host' => $host,
        'version' => $lluVersion,
        'connections_count' => count($connData['data']),
        'patient_keys' => is_array($patient) ? array_keys($patient) : array(),
        'conn_node_keys' => is_array($connNode) ? array_keys($connNode) : array(),
        'graph_points_count' => count($graphPoints),
        'has_current' => !empty($current),
        'current_keys' => is_array($current) ? array_keys($current) : array()
    ));

    if (!$current || extractMgDl($current) <= 0) {
        return array('ok' => false, 'error' => 'Sin Información de Sensor');
    }

    $glucose = extractMgDl($current);
    $trendNum = isset($current['TrendArrow']) ? intval($current['TrendArrow']) : 3;
    // Abbott TrendArrow: 1=↓↓, 2=↘, 3=→, 4=↗, 5=↑↑
    $arrowMap = array(1 => '↓↓', 2 => '↘', 3 => '→', 4 => '↗', 5 => '↑↑');
    $arrow = isset($arrowMap[$trendNum]) ? $arrowMap[$trendNum] : '→';

    $history = array();
    foreach ($graphPoints as $pt) {
        $val = extractMgDl($pt);
        if ($val > 0) {
            $ts = !empty($pt['FactoryTimestamp']) ? strtotime($pt['FactoryTimestamp'] . ' UTC') : (!empty($pt['Timestamp']) ? strtotime($pt['Timestamp']) : time());
            if (!$ts) $ts = time();
            $history[] = array(
                'time' => $ts,
                'label' => date('H:i', $ts),
                'value' => $val
            );
        }
    }

    $lastTs = !empty($current['FactoryTimestamp']) ? strtotime($current['FactoryTimestamp'] . ' UTC') : (!empty($current['Timestamp']) ? strtotime($current['Timestamp']) : time());
    if (!$lastTs) $lastTs = time();

    $minutesAgo = max(0, round((time() - $lastTs) / 60));
    $staleLimit = isset($config['stale_minutes']) && intval($config['stale_minutes']) > 0 ? intval($config['stale_minutes']) : 5;

    $delta = 0;
    if (count($history) > 0) {
        $lastHist = $history[count($history) - 1]['value'];
        $delta = $glucose - $lastHist;
    }

    $history[] = array(
        'time' => $lastTs,
        'label' => date('H:i', $lastTs),
        'value' => $glucose
    );

    if ($minutesAgo > $staleLimit) {
        return array(
            'ok' => false,
            'stale' => true,
            'minutes_ago' => $minutesAgo,
            'error' => 'Sin Información de Sensor',
            'history' => array_slice($history, -36)
        );
    }

    return array(
        'ok' => true,
        'source' => 'librelinkup',
        'patient' => !empty($patientName) ? $patientName : $config['patient_name'],
        'glucose' => $glucose,
        'delta' => $delta,
        'arrow' => $arrow,
        'timestamp' => $lastTs,
        'minutes_ago' => $minutesAgo,
        'history' => array_slice($history, -36)
    );
}

// Ejecutar consulta según fuente seleccionada
$source = isset($_GET['source']) ? $_GET['source'] : $config['source'];

if ($source === 'librelinkup') {
    echo json_encode(fetchLibreLinkUp($config, $cacheFile, $debugFile));
    exit;
} elseif ($source === 'nightscout') {
    echo json_encode(fetchNightscout($config));
    exit;
} else {
    // Modo Simulación / Demo interactivo
    $preset = isset($_GET['preset']) ? $_GET['preset'] : 'normal';
    $now = time();
    $base = 112;
    $delta = 2;
    $arrow = '→';

    if ($preset === 'low') { $base = 64; $delta = -6; $arrow = '↘'; }
    elseif ($preset === 'urgent_low') { $base = 49; $delta = -11; $arrow = '↓↓'; }
    elseif ($preset === 'high') { $base = 204; $delta = 8; $arrow = '↗'; }
    elseif ($preset === 'urgent_high') { $base = 268; $delta = 14; $arrow = '↑↑'; }

    $history = array();
    for ($i = 24; $i >= 0; $i--) {
        $t = $now - ($i * 300);
        $wave = round(sin($i / 3.5) * 14);
        $drift = ($i * (-$delta / 5));
        $val = max(40, min(350, round($base + $drift + $wave)));
        if ($i === 0) $val = $base;
        $history[] = array(
            'time' => $t,
            'label' => date('H:i', $t),
            'value' => $val
        );
    }

    echo json_encode(array(
        'ok' => true,
        'source' => 'demo',
        'patient' => $config['patient_name'] . ' (Demo)',
        'glucose' => $base,
        'delta' => $delta,
        'arrow' => $arrow,
        'timestamp' => $now,
        'minutes_ago' => 1,
        'history' => $history
    ));
    exit;
}
