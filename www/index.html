<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta name="theme-color" content="#070a12">
    <link rel="manifest" href="manifest.json">
    <title>MUG · Monitor Universal de Glucosa</title>
    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            -webkit-tap-highlight-color: transparent;
            user-select: none;
            -webkit-user-select: none;
        }
        html, body {
            width: 100%;
            height: 100%;
            background-color: #070a12;
            color: #f8fafc;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
            overflow: hidden;
            transition: background-color 0.25s ease, color 0.25s ease;
        }

        /* Estado SIN DATOS / >5 MIN: Fondo gris y fuente negra */
        body.no-data {
            background-color: #9ca3af !important;
            color: #000000 !important;
        }
        body.no-data .topbar,
        body.no-data .bottombar {
            border-color: rgba(0, 0, 0, 0.2);
        }
        body.no-data .patient-title,
        body.no-data .clock-box,
        body.no-data .updated-pill,
        body.no-data .unit-label,
        body.no-data .demo-label {
            color: #000000 !important;
        }
        body.no-data .source-badge {
            background: rgba(0, 0, 0, 0.14);
            color: #000000 !important;
        }
        body.no-data .btn-menu-bars,
        body.no-data .chip {
            background: rgba(0, 0, 0, 0.12);
            border-color: rgba(0, 0, 0, 0.35);
            color: #000000 !important;
        }
        body.no-data .btn-menu-bars svg {
            stroke: #000000 !important;
        }
        body.no-data .reading-card {
            background: #b0b7c0 !important;
            border-color: #000000 !important;
        }
        body.no-data .glucose-value,
        body.no-data .trend-arrow {
            color: #000000 !important;
            text-shadow: none !important;
        }
        body.no-data .range-pill {
            background: rgba(0, 0, 0, 0.18) !important;
            color: #000000 !important;
        }
        body.no-data .status-dot {
            background: #000000 !important;
            box-shadow: none !important;
        }
        body.no-data .sensor-status-msg {
            color: #000000 !important;
        }

        /* Capa de brillo para Modo Noche */
        #dimmer-overlay {
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            background: #000000;
            opacity: 0;
            pointer-events: none;
            z-index: 9000;
            transition: opacity 0.25s ease;
        }

        /* Contenedor principal */
        .app-shell {
            display: -webkit-flex;
            display: flex;
            -webkit-flex-direction: column;
            flex-direction: column;
            width: 100%;
            height: 100%;
            padding: 14px 20px;
            justify-content: space-between;
        }

        /* Barra Superior */
        .topbar {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: space-between;
            justify-content: space-between;
            border-bottom: 1px solid rgba(255,255,255,0.08);
            padding-bottom: 10px;
            flex-shrink: 0;
        }
        .brand-group {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            gap: 10px;
        }
        .status-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #10b981;
            display: inline-block;
        }
        .patient-title {
            font-size: 18px;
            font-weight: 700;
            letter-spacing: 0.3px;
            color: #e2e8f0;
        }
        .source-badge {
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 1px;
            padding: 3px 8px;
            border-radius: 6px;
            background: rgba(255,255,255,0.08);
            color: #94a3b8;
            font-weight: 600;
        }
        .clock-box {
            font-size: 24px;
            font-weight: 700;
            color: #cbd5e1;
            font-variant-numeric: tabular-nums;
        }

        /* Botón único de barras de menú (Hamburguesa) */
        .btn-menu-bars {
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.16);
            color: #f8fafc;
            width: 46px;
            height: 42px;
            border-radius: 12px;
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: center;
            justify-content: center;
            cursor: pointer;
        }
        .btn-menu-bars:active {
            background: rgba(255,255,255,0.2);
        }

        /* Cuerpo Principal: Pantalla Completa solo para Lectura Gigante */
        .dashboard-main {
            display: -webkit-flex;
            display: flex;
            -webkit-flex: 1;
            flex: 1;
            padding: 12px 0;
            min-height: 0;
            -webkit-align-items: stretch;
            align-items: stretch;
        }

        .reading-card {
            -webkit-flex: 1;
            flex: 1;
            width: 100%;
            background: radial-gradient(circle at top left, rgba(255,255,255,0.045), rgba(255,255,255,0.015));
            border: 2px solid rgba(16, 185, 129, 0.4);
            border-radius: 26px;
            display: -webkit-flex;
            display: flex;
            -webkit-flex-direction: column;
            flex-direction: column;
            -webkit-justify-content: center;
            justify-content: center;
            -webkit-align-items: center;
            align-items: center;
            padding: 20px 24px;
            position: relative;
            transition: border-color 0.25s, background 0.25s;
        }

        .range-pill {
            position: absolute;
            top: 18px;
            left: 22px;
            padding: 6px 16px;
            border-radius: 999px;
            font-size: 14px;
            font-weight: 800;
            letter-spacing: 0.8px;
            text-transform: uppercase;
            background: rgba(16, 185, 129, 0.18);
            color: #34d399;
        }
        .updated-pill {
            position: absolute;
            top: 20px;
            right: 22px;
            font-size: 14px;
            color: #94a3b8;
            font-weight: 600;
        }

        .big-reading-row {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: center;
            justify-content: center;
            gap: 24px;
            margin-top: 8px;
        }
        .glucose-value {
            font-size: 210px;
            line-height: 0.9;
            font-weight: 900;
            letter-spacing: -5px;
            color: #10b981;
            font-variant-numeric: tabular-nums;
        }
        .trend-column {
            display: -webkit-flex;
            display: flex;
            -webkit-flex-direction: column;
            flex-direction: column;
            -webkit-align-items: center;
            align-items: center;
        }
        .trend-arrow {
            font-size: 105px;
            line-height: 1;
            font-weight: 900;
            color: #10b981;
        }
        .sensor-status-msg {
            margin-top: 10px;
            font-size: 22px;
            font-weight: 800;
            letter-spacing: 0.5px;
            color: #000000;
            display: none;
            text-align: center;
        }
        .unit-label {
            margin-top: 10px;
            font-size: 16px;
            color: #64748b;
            font-weight: 700;
            letter-spacing: 1.5px;
            text-transform: uppercase;
        }

        /* Barra Inferior de Simulador (solo visible en modo demo) */
        .bottombar {
            display: none;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: center;
            justify-content: center;
            padding-top: 8px;
            border-top: 1px solid rgba(255,255,255,0.08);
            flex-shrink: 0;
            flex-wrap: wrap;
            gap: 8px;
        }
        .demo-bar {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            gap: 6px;
            flex-wrap: wrap;
        }
        .demo-label {
            font-size: 12px;
            color: #64748b;
            font-weight: 700;
            margin-right: 4px;
            text-transform: uppercase;
        }
        .chip {
            padding: 6px 11px;
            border-radius: 8px;
            font-size: 12px;
            font-weight: 700;
            border: 1px solid rgba(255,255,255,0.12);
            background: rgba(255,255,255,0.05);
            color: #cbd5e1;
            cursor: pointer;
        }
        .chip.active {
            background: #10b981;
            color: #052e16;
            border-color: #10b981;
        }

        /* Menú y Modales */
        .modal-backdrop {
            position: fixed;
            top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(3, 7, 18, 0.84);
            display: none;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: center;
            justify-content: center;
            z-index: 8000;
            padding: 16px;
        }
        .modal-backdrop.open {
            display: -webkit-flex;
            display: flex;
        }
        .modal-card {
            background: #111827;
            border: 1px solid rgba(255,255,255,0.15);
            border-radius: 20px;
            max-width: 540px;
            width: 100%;
            max-height: 92vh;
            overflow-y: auto;
            padding: 22px;
            color: #f8fafc;
        }
        .modal-card.wide {
            max-width: 720px;
        }
        .modal-header {
            display: -webkit-flex;
            display: flex;
            -webkit-justify-content: space-between;
            justify-content: space-between;
            -webkit-align-items: center;
            align-items: center;
            margin-bottom: 16px;
            padding-bottom: 10px;
            border-bottom: 1px solid rgba(255,255,255,0.08);
        }
        .modal-header h2 {
            font-size: 19px;
            color: #f8fafc;
        }
        .btn-close {
            background: rgba(255,255,255,0.08);
            border: 1px solid rgba(255,255,255,0.15);
            color: #ffffff;
            width: 36px;
            height: 36px;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
        }

        /* Lista de opciones del Menú Hamburguesa */
        .menu-list {
            display: -webkit-flex;
            display: flex;
            -webkit-flex-direction: column;
            flex-direction: column;
            gap: 10px;
        }
        .menu-item {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            -webkit-justify-content: space-between;
            justify-content: space-between;
            width: 100%;
            padding: 14px 16px;
            background: #1f2937;
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 14px;
            color: #f8fafc;
            font-size: 15px;
            font-weight: 700;
            text-align: left;
            cursor: pointer;
        }
        .menu-item:active {
            background: #374151;
        }
        .menu-item-left {
            display: -webkit-flex;
            display: flex;
            -webkit-align-items: center;
            align-items: center;
            gap: 12px;
        }
        .menu-item-sub {
            font-size: 12px;
            color: #94a3b8;
            font-weight: 500;
            display: block;
            margin-top: 2px;
        }

        /* Formularios */
        .form-group {
            margin-bottom: 14px;
        }
        .form-group label {
            display: block;
            font-size: 12px;
            font-weight: 700;
            color: #94a3b8;
            margin-bottom: 5px;
            text-transform: uppercase;
        }
        .form-control {
            width: 100%;
            padding: 10px 12px;
            background: #1f2937;
            border: 1px solid #374151;
            border-radius: 10px;
            color: #ffffff;
            font-size: 14px;
        }
        .form-row {
            display: -webkit-flex;
            display: flex;
            gap: 10px;
        }
        .form-row .form-group {
            -webkit-flex: 1;
            flex: 1;
        }
        .btn-primary {
            width: 100%;
            padding: 12px;
            background: #10b981;
            color: #052e16;
            font-weight: 800;
            border: none;
            border-radius: 10px;
            font-size: 15px;
            cursor: pointer;
            margin-top: 6px;
        }
        .btn-primary:disabled {
            opacity: 0.65;
            cursor: not-allowed;
        }
        .btn-secondary {
            display: inline-block;
            padding: 8px 14px;
            background: rgba(255,255,255,0.1);
            border: 1px solid rgba(255,255,255,0.2);
            color: #f8fafc;
            font-weight: 700;
            border-radius: 8px;
            font-size: 13px;
            text-decoration: none;
            cursor: pointer;
            margin-top: 8px;
            margin-right: 6px;
        }
        .btn-secondary:active {
            background: rgba(255,255,255,0.22);
        }

        /* Estado de carga (Loading Spinner) y Mensajes de Error en Conectar */
        @keyframes spin {
            0% { -webkit-transform: rotate(0deg); transform: rotate(0deg); }
            100% { -webkit-transform: rotate(360deg); transform: rotate(360deg); }
        }
        .spinner {
            display: inline-block;
            width: 16px;
            height: 16px;
            border: 2.5px solid rgba(255,255,255,0.3);
            border-top-color: #ffffff;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
            vertical-align: middle;
            margin-right: 8px;
        }
        .modal-feedback {
            display: none;
            padding: 11px 14px;
            border-radius: 10px;
            font-size: 13px;
            font-weight: 700;
            margin-bottom: 12px;
            line-height: 1.45;
        }
        .modal-feedback.loading {
            display: block;
            background: rgba(59, 130, 246, 0.18);
            border: 1px solid #3b82f6;
            color: #93c5fd;
        }
        .modal-feedback.error {
            display: block;
            background: rgba(239, 68, 68, 0.2);
            border: 1px solid #ef4444;
            color: #fca5a5;
        }
        .modal-feedback.success {
            display: block;
            background: rgba(16, 185, 129, 0.2);
            border: 1px solid #10b981;
            color: #6ee7b7;
        }

        /* Gráfico dentro del modal */
        .stats-strip {
            display: -webkit-flex;
            display: flex;
            -webkit-justify-content: space-between;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 10px;
            margin-bottom: 12px;
            background: #1f2937;
            padding: 10px 14px;
            border-radius: 12px;
        }
        .stat-item {
            font-size: 13px;
            color: #94a3b8;
        }
        .stat-item strong {
            color: #f8fafc;
            font-weight: 800;
        }
        .svg-container {
            width: 100%;
            height: 230px;
            background: rgba(255,255,255,0.02);
            border: 1px solid rgba(255,255,255,0.08);
            border-radius: 14px;
            padding: 8px;
        }
        #trend-svg {
            width: 100%;
            height: 100%;
            display: block;
        }

        /* Tarjetas de Donación / Info */
        .info-box {
            background: #1f2937;
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 14px;
            padding: 14px 16px;
            margin-bottom: 12px;
            font-size: 14px;
            line-height: 1.55;
            color: #e2e8f0;
        }
        .info-box h3 {
            font-size: 15px;
            color: #34d399;
            margin-bottom: 6px;
        }
        .donate-badge {
            display: inline-block;
            padding: 3px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 800;
            margin-bottom: 6px;
        }

        /* Adaptación para pantallas verticales de celulares */
        @media (max-width: 760px) {
            .glucose-value {
                font-size: 135px;
            }
            .trend-arrow {
                font-size: 68px;
            }
            .sensor-status-msg {
                font-size: 18px;
            }
        }
    </style>
</head>
<body class="no-data">

<div id="dimmer-overlay"></div>

<div class="app-shell">
    <!-- Barra Superior con Icono de Barras de Menú -->
    <header class="topbar">
        <div class="brand-group">
            <span class="status-dot" id="status-dot"></span>
            <span class="patient-title" id="patient-name">FreeStyle Libre</span>
            <span class="source-badge" id="source-badge">SIN DATOS</span>
        </div>

        <div class="clock-box" id="live-clock">--:--</div>

        <button class="btn-menu-bars" onclick="openModal('menu-modal')" title="Menú de Opciones" aria-label="Menú">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#f8fafc" stroke-width="2.5" stroke-linecap="round">
                <line x1="3" y1="6" x2="21" y2="6"></line>
                <line x1="3" y1="12" x2="21" y2="12"></line>
                <line x1="3" y1="18" x2="21" y2="18"></line>
            </svg>
        </button>
    </header>

    <!-- Cuerpo Principal: Solo Lectura de Glucosa y Flecha -->
    <main class="dashboard-main">
        <section class="reading-card" id="reading-card">
            <span class="range-pill" id="range-pill">SIN DATOS</span>
            <span class="updated-pill" id="updated-pill">Sin lectura</span>

            <div class="big-reading-row">
                <div class="glucose-value" id="glucose-value">-.-</div>
                <div class="trend-column">
                    <div class="trend-arrow" id="trend-arrow">—</div>
                </div>
            </div>

            <div class="sensor-status-msg" id="sensor-status-msg">Sin Información de Sensor</div>
            <div class="unit-label">mg/dL · Monitoreo Continuo</div>
        </section>
    </main>

    <!-- Barra Inferior (Solo visible en Modo Simulador) -->
    <footer class="bottombar" id="bottom-demo-bar">
        <div class="demo-bar">
            <span class="demo-label">Simulador:</span>
            <button class="chip" onclick="setDemoPreset('nodata', this)">⚪ Sin Sensor (-.-)</button>
            <button class="chip active" onclick="setDemoPreset('normal', this)">🟢 Normal (112)</button>
            <button class="chip" onclick="setDemoPreset('low', this)">🟡 Baja (64)</button>
            <button class="chip" onclick="setDemoPreset('urgent_low', this)">🔴 Urgente Baja (49)</button>
            <button class="chip" onclick="setDemoPreset('high', this)">🟠 Alta (204)</button>
            <button class="chip" onclick="setDemoPreset('urgent_high', this)">🟣 Muy Alta (268)</button>
        </div>
    </footer>
</div>

<!-- 1. MODAL MENÚ PRINCIPAL (Icono de Barras) -->
<div class="modal-backdrop" id="menu-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>☰ Menú de Opciones</h2>
            <button class="btn-close" onclick="closeModal('menu-modal')">✕</button>
        </div>
        <div class="menu-list">
            <button class="menu-item" onclick="openConnectFromMenu()">
                <div class="menu-item-left">
                    <span>🔗</span>
                    <div>
                        Conectar Cuenta
                        <span class="menu-item-sub">FreeStyle Libre (LibreLinkUp), Nightscout o Simulador</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="switchModal('menu-modal', 'alarm-modal')">
                <div class="menu-item-left">
                    <span>🔔</span>
                    <div>
                        Opciones de Alarma y Rangos
                        <span class="menu-item-sub">Umbrales mg/dL, límite de 5 min sin sensor y brillo</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="openGraphFromMenu()">
                <div class="menu-item-left">
                    <span>📈</span>
                    <div>
                        Gráfico de Tendencia
                        <span class="menu-item-sub">Historial reciente, promedio, mínimo, máximo y TIR</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="closeModal('menu-modal'); toggleFullScreen();">
                <div class="menu-item-left">
                    <span>⛶</span>
                    <div>
                        Pantalla Completa
                        <span class="menu-item-sub">Alternar modo monitor a pantalla completa</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="switchModal('menu-modal', 'legal-modal')">
                <div class="menu-item-left">
                    <span>⚖️</span>
                    <div>
                        Aviso Legal
                        <span class="menu-item-sub">Exención de responsabilidad y condiciones de uso</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="switchModal('menu-modal', 'about-modal')">
                <div class="menu-item-left">
                    <span>ℹ️</span>
                    <div>
                        Versión y Contacto
                        <span class="menu-item-sub">Información de la APK, WhatsApp y correo de soporte</span>
                    </div>
                </div>
                <span>›</span>
            </button>

            <button class="menu-item" onclick="switchModal('menu-modal', 'donate-modal')" style="border-color: rgba(16, 185, 129, 0.4);">
                <div class="menu-item-left">
                    <span>💛</span>
                    <div>
                        Donar al Proyecto
                        <span class="menu-item-sub">Apoyar mediante MACH, Mercado Pago o PayPal</span>
                    </div>
                </div>
                <span>›</span>
            </button>
        </div>
    </div>
</div>

<!-- 2. MODAL CONECTAR CUENTA -->
<div class="modal-backdrop" id="config-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>🔗 Conectar Cuenta</h2>
            <button class="btn-close" onclick="closeModal('config-modal')">✕</button>
        </div>

        <div class="form-group">
            <label>Fuente de Datos</label>
            <select class="form-control" id="cfg-source" onchange="updateModalSourceFields()">
                <option value="librelinkup">FreeStyle Libre (vía cuenta LibreLinkUp)</option>
                <option value="nightscout">Nightscout (URL de API v1)</option>
                <option value="demo">Modo Simulador / Demo</option>
            </select>
        </div>

        <div id="fields-llu">
            <div class="form-group">
                <label>Correo de LibreLinkUp (Cuenta Seguidor)</label>
                <input type="email" class="form-control" id="cfg-llu-email" placeholder="ejemplo@correo.com">
            </div>
            <div class="form-row">
                <div class="form-group">
                    <label>Contraseña LibreLinkUp</label>
                    <input type="password" class="form-control" id="cfg-llu-password" placeholder="••••••••">
                </div>
                <div class="form-group">
                    <label>Región Servidor</label>
                    <select class="form-control" id="cfg-llu-region">
                        <option value="global">Autodetectar (Recomendado)</option>
                        <option value="la">Latinoamérica (la)</option>
                        <option value="ae">Internacional (ae)</option>
                        <option value="us">Estados Unidos (us)</option>
                        <option value="eu">Europa (eu)</option>
                        <option value="eu2">Europa 2 (eu2)</option>
                    </select>
                </div>
            </div>
        </div>

        <div id="fields-ns" style="display:none;">
            <div class="form-group">
                <label>URL de tu sitio Nightscout</label>
                <input type="url" class="form-control" id="cfg-ns-url" placeholder="https://mi-nightscout.herokuapp.com">
            </div>
            <div class="form-group">
                <label>Token de acceso (Opcional)</label>
                <input type="text" class="form-control" id="cfg-ns-token" placeholder="mi-token-12345">
            </div>
        </div>

        <div class="form-group">
            <label>Nombre del Paciente en Pantalla</label>
            <input type="text" class="form-control" id="cfg-patient-name" value="FreeStyle Libre">
        </div>

        <!-- Banner de Loading / Error / Éxito al conectar -->
        <div id="config-feedback" class="modal-feedback"></div>

        <button class="btn-primary" id="btn-save-config" onclick="saveAndVerifyAccount()">Guardar y Conectar</button>
    </div>
</div>

<!-- 3. MODAL OPCIONES DE ALARMA Y RANGOS -->
<div class="modal-backdrop" id="alarm-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>🔔 Opciones de Alarma y Rangos</h2>
            <button class="btn-close" onclick="closeModal('alarm-modal')">✕</button>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Urgente Baja (mg/dL)</label>
                <input type="number" class="form-control" id="cfg-urgent-low" value="55">
            </div>
            <div class="form-group">
                <label>Alerta Baja (mg/dL)</label>
                <input type="number" class="form-control" id="cfg-low" value="70">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Alerta Alta (mg/dL)</label>
                <input type="number" class="form-control" id="cfg-high" value="180">
            </div>
            <div class="form-group">
                <label>Urgente Alta (mg/dL)</label>
                <input type="number" class="form-control" id="cfg-urgent-high" value="250">
            </div>
        </div>

        <div class="form-group">
            <label>Tiempo máximo sin lectura antes de «Sin Información de Sensor» (minutos)</label>
            <input type="number" class="form-control" id="cfg-stale-minutes" value="5" min="1" max="60">
        </div>

        <div class="form-group">
            <label>🌙 Brillo de Pantalla (Modo Noche)</label>
            <input type="range" class="form-control" id="brightness-slider" min="15" max="100" value="100" oninput="setBrightness(this.value)" style="padding:6px;">
        </div>

        <button class="btn-primary" onclick="saveThresholdsOnly()">Guardar Opciones</button>
    </div>
</div>

<!-- 4. MODAL GRÁFICO DE TENDENCIA -->
<div class="modal-backdrop" id="graph-modal">
    <div class="modal-card wide">
        <div class="modal-header">
            <h2>📈 Gráfico de Tendencia Reciente</h2>
            <button class="btn-close" onclick="closeModal('graph-modal')">✕</button>
        </div>

        <div class="stats-strip">
            <span class="stat-item">Promedio: <strong id="stat-avg">--</strong> mg/dL</span>
            <span class="stat-item">Mínimo: <strong id="stat-min">--</strong> mg/dL</span>
            <span class="stat-item">Máximo: <strong id="stat-max">--</strong> mg/dL</span>
            <span class="stat-item">En Rango (TIR): <strong id="stat-tir">--</strong></span>
        </div>

        <div class="svg-container">
            <svg id="trend-svg" viewBox="0 0 600 240" preserveAspectRatio="none"></svg>
        </div>
    </div>
</div>

<!-- 5. MODAL AVISO LEGAL -->
<div class="modal-backdrop" id="legal-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>⚖️ Aviso Legal y Exención de Responsabilidad</h2>
            <button class="btn-close" id="btn-close-legal" onclick="closeModal('legal-modal')">✕</button>
        </div>
        <div class="info-box">
            <h3>1. Propósito Exclusivamente Informativo</h3>
            <p><strong>MUG (Monitor Universal de Glucosa)</strong> es una herramienta secundaria de visualización a distancia diseñada para facilitar la lectura de datos provenientes de servicios en la nube de terceros (como LibreLinkUp o Nightscout). <strong>No es un dispositivo médico certificado</strong> ni sustituye el monitoreo directo en el lector o aplicación oficial del fabricante del sensor.</p>
        </div>
        <div class="info-box">
            <h3>2. Prohibición de Uso para Decisiones Médicas</h3>
            <p>El usuario acepta expresamente <strong>no tomar decisiones médicas, terapéuticas ni de dosificación de insulina o medicamentos</strong> basándose en la información mostrada por esta aplicación. Antes de cualquier acción clínica, el usuario debe verificar su nivel de glucosa mediante un glucómetro capilar o el receptor oficial aprobado por su médico tratante.</p>
        </div>
        <div class="info-box">
            <h3>3. Exención de Responsabilidades de Uso</h3>
            <p>La transmisión de datos depende de conexiones a internet, servidores de terceros y sincronización Bluetooth/NFC del teléfono emisor, pudiendo presentar retrasos, interrupciones o inexactitudes. El desarrollador y los colaboradores de MUG quedan <strong>totalmente eximidos de cualquier responsabilidad civil, médica, directa o indirecta</strong> derivada del uso, mal funcionamiento, falta de conexión o interpretación de los datos exhibidos en esta aplicación.</p>
        </div>
        <button class="btn-primary" id="btn-accept-legal" onclick="acceptLegalDisclaimer()">✅ He leído y acepto las condiciones de uso</button>
    </div>
</div>

<!-- 6. MODAL INFORMACIÓN DE LA VERSIÓN Y CONTACTO -->
<div class="modal-backdrop" id="about-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>ℹ️ Información y Contacto</h2>
            <button class="btn-close" onclick="closeModal('about-modal')">✕</button>
        </div>
        <div style="text-align:center; margin-bottom:14px; background:#ffffff; padding:12px; border-radius:14px;">
            <img src="logo.png" alt="MUG Logo" style="max-height:95px; width:auto;">
        </div>
        <div class="info-box">
            <h3>MUG · Monitor Universal de Glucosa</h3>
            <p><strong>Versión de la APK:</strong> v1.1.0 (Compilación Autónoma Android)</p>
            <p><strong>Desarrollador:</strong> Alvaro Olave · Código Creativo</p>
            <p><strong>Repositorio Oficial:</strong> github.com/alvarolave-design/mug</p>
            <p><strong>Compatibilidad:</strong> Tablets y teléfonos Android (desde Android 5.0 Lollipop en adelante) y navegadores web.</p>
        </div>
        <div class="info-box" style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
            <div style="flex:1;">
                <h3>💬 Soporte por WhatsApp</h3>
                <p>¿Consultas o ayuda para instalarla?</p>
                <p style="margin-top:4px; font-size:15px; color:#34d399; font-weight:800;">+56 9 3223 2764</p>
                <button class="btn-secondary" style="border-color:#10b981; color:#6ee7b7;" onclick="openExternal('https://wa.me/56932232764?text=Hola%2C%20vi%20la%20app%20MUG%20Monitor%20y%20tengo%20una%20consulta')">
                    💬 Abrir WhatsApp
                </button>
            </div>
            <img src="https://api.qrserver.com/v1/create-qr-code/?size=110x110&margin=4&data=https%3A%2F%2Fwa.me%2F56932232764%3Ftext%3DHola%252C%2520vi%2520la%2520app%2520MUG%2520Monitor" alt="QR WhatsApp" style="width:86px; height:86px; border-radius:8px; background:#fff; flex-shrink:0;" onerror="this.style.display='none'">
        </div>
        <div class="info-box">
            <h3>📧 Contacto por Correo</h3>
            <p style="margin-top:4px; font-size:14px; color:#34d399; font-weight:700;">codigo.creativo.ao@gmail.com</p>
            <button class="btn-secondary" onclick="openExternal('mailto:codigo.creativo.ao@gmail.com?subject=Soporte%20MUG%20Monitor%20v1.1.0')">
                ✉️ Enviar Correo de Contacto
            </button>
        </div>
        <button class="btn-primary" onclick="closeModal('about-modal')">Cerrar</button>
    </div>
</div>

<!-- 7. MODAL DONAR AL PROYECTO -->
<div class="modal-backdrop" id="donate-modal">
    <div class="modal-card">
        <div class="modal-header">
            <h2>💛 Donar y Apoyar el Proyecto</h2>
            <button class="btn-close" onclick="closeModal('donate-modal')">✕</button>
        </div>
        <p style="font-size:13px; color:#94a3b8; margin-bottom:12px;">
            Si estás viendo esto en un teléfono o tablet antiguo, puedes <strong>escanear los códigos QR con la cámara de tu celular principal</strong> o abrir el enlace directo:
        </p>

        <!-- MACH con QR para escanear desde el celular principal -->
        <div class="info-box" style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
            <div style="flex:1;">
                <span class="donate-badge" style="background:rgba(124, 58, 237, 0.25); color:#c4b5fd;">💜 MACH</span>
                <p style="font-size:13px;">Escanea el QR con tu celular principal o abre el link:</p>
                <button class="btn-secondary" style="border-color:#8b5cf6; color:#ddd6fe;" onclick="openExternal('https://app.soymach.com/rF2V/fnfvmt1m')">
                    🔗 Abrir MACH
                </button>
            </div>
            <img src="https://api.qrserver.com/v1/create-qr-code/?size=110x110&margin=4&data=https%3A%2F%2Fapp.soymach.com%2FrF2V%2Ffnfvmt1m" alt="QR MACH" style="width:86px; height:86px; border-radius:8px; background:#fff; flex-shrink:0;" onerror="this.style.display='none'">
        </div>

        <!-- Mercado Pago con QR -->
        <div class="info-box" style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
            <div style="flex:1;">
                <span class="donate-badge" style="background:rgba(14, 165, 233, 0.25); color:#7dd3fc;">🤝 Mercado Pago</span>
                <p style="font-size:13px;">Escanea el QR con tu celular principal o toca el botón:</p>
                <button class="btn-secondary" style="border-color:#0ea5e9; color:#bae6fd;" onclick="openExternal('https://link.mercadopago.cl/codigocreativo')">
                    🔗 Abrir Mercado Pago
                </button>
            </div>
            <img src="https://api.qrserver.com/v1/create-qr-code/?size=110x110&margin=4&data=https%3A%2F%2Flink.mercadopago.cl%2Fcodigocreativo" alt="QR Mercado Pago" style="width:86px; height:86px; border-radius:8px; background:#fff; flex-shrink:0;" onerror="this.style.display='none'">
        </div>

        <!-- PayPal con QR -->
        <div class="info-box" style="display:flex; align-items:center; justify-content:space-between; gap:12px;">
            <div style="flex:1;">
                <span class="donate-badge" style="background:rgba(59, 130, 246, 0.25); color:#93c5fd;">💙 PayPal</span>
                <p style="font-size:13px;">Aporte internacional mediante <strong>PayPal.Me</strong>:</p>
                <button class="btn-secondary" style="border-color:#3b82f6; color:#bfdbfe;" onclick="openExternal('https://www.paypal.com/paypalme/codigoCreativo/10')">
                    🔗 Abrir PayPal.Me
                </button>
            </div>
            <img src="https://api.qrserver.com/v1/create-qr-code/?size=110x110&margin=4&data=https%3A%2F%2Fwww.paypal.com%2Fpaypalme%2FcodigoCreativo%2F10" alt="QR PayPal" style="width:86px; height:86px; border-radius:8px; background:#fff; flex-shrink:0;" onerror="this.style.display='none'">
        </div>

        <button class="btn-primary" onclick="closeModal('donate-modal')">Cerrar</button>
    </div>
</div>

<script>
    // Estado de la aplicación (Sin alarmas visuales ni sonoras)
    var appState = {
        source: 'librelinkup',
        preset: 'normal',
        lastHistory: [],
        lastColor: '#10b981',
        config: {
            urgent_low: 55,
            low_threshold: 70,
            high_threshold: 180,
            urgent_high: 250,
            stale_minutes: 5,
            patient_name: 'FreeStyle Libre'
        }
    };

    // Reloj en tiempo real
    function updateClock() {
        var now = new Date();
        var hh = ('0' + now.getHours()).slice(-2);
        var mm = ('0' + now.getMinutes()).slice(-2);
        document.getElementById('live-clock').textContent = hh + ':' + mm;
    }
    setInterval(updateClock, 1000);
    updateClock();

    // Brillo / Modo Noche
    function setBrightness(val) {
        var overlay = document.getElementById('dimmer-overlay');
        var opacity = (100 - parseInt(val, 10)) / 100;
        overlay.style.opacity = opacity.toFixed(2);
    }

    // Abrir enlaces externos (MACH, Mercado Pago, PayPal, Correo) en Android o navegador
    function openExternal(url) {
        if (window.MugNative && typeof window.MugNative.openExternalUrl === 'function') {
            window.MugNative.openExternalUrl(url);
        } else {
            window.open(url, '_blank');
        }
    }

    // Copiar RUT / Datos de transferencia al portapapeles
    function copyText(text, btnId, originalLabel) {
        var btn = document.getElementById(btnId);
        try {
            if (navigator.clipboard && navigator.clipboard.writeText) {
                navigator.clipboard.writeText(text);
            } else {
                var ta = document.createElement('textarea');
                ta.value = text;
                document.body.appendChild(ta);
                ta.select();
                document.execCommand('copy');
                document.body.removeChild(ta);
            }
            if (btn) {
                btn.textContent = '✅ ¡Copiado!';
                setTimeout(function() { btn.textContent = originalLabel; }, 2000);
            }
        } catch (e) {}
    }

    // Pantalla completa
    function toggleFullScreen() {
        var doc = window.document;
        var docEl = doc.documentElement;
        var requestFull = docEl.requestFullscreen || docEl.mozRequestFullScreen || docEl.webkitRequestFullScreen || docEl.msRequestFullscreen;
        var cancelFull = doc.exitFullscreen || doc.mozCancelFullScreen || doc.webkitExitFullscreen || doc.msExitFullscreen;

        if (!doc.fullscreenElement && !doc.mozFullScreenElement && !doc.webkitFullscreenElement && !doc.msFullscreenElement) {
            if (requestFull) requestFull.call(docEl);
        } else {
            if (cancelFull) cancelFull.call(doc);
        }
        if ('wakeLock' in navigator) {
            navigator.wakeLock.request('screen').catch(function(){});
        }
    }

    function addBodyClass(cls) {
        if (document.body.className.indexOf(cls) === -1) {
            document.body.className = (document.body.className + ' ' + cls).trim();
        }
    }
    function removeBodyClass(cls) {
        var reg = new RegExp('\\b' + cls + '\\b', 'g');
        document.body.className = document.body.className.replace(reg, '').replace(/\s+/g, ' ').trim();
    }

    // Gestión de Modales
    function openModal(id) {
        document.getElementById(id).className = 'modal-backdrop open';
    }
    function closeModal(id) {
        document.getElementById(id).className = 'modal-backdrop';
    }
    function switchModal(fromId, toId) {
        closeModal(fromId);
        openModal(toId);
    }
    function openConnectFromMenu() {
        var fb = document.getElementById('config-feedback');
        fb.className = 'modal-feedback';
        fb.style.display = 'none';
        fb.innerHTML = '';
        var btn = document.getElementById('btn-save-config');
        btn.disabled = false;
        btn.textContent = 'Guardar y Conectar';
        switchModal('menu-modal', 'config-modal');
    }
    function openGraphFromMenu() {
        closeModal('menu-modal');
        openModal('graph-modal');
        renderGraph(appState.lastHistory, appState.lastColor);
    }

    // Estado cuando NO hay datos o han pasado más de 5 minutos: "-.-", fondo gris, fuente negra y "Sin Información de Sensor"
    function setNoDataState(customMsg) {
        addBodyClass('no-data');

        var valEl = document.getElementById('glucose-value');
        var arrowEl = document.getElementById('trend-arrow');
        var pillEl = document.getElementById('range-pill');
        var updEl = document.getElementById('updated-pill');
        var cardEl = document.getElementById('reading-card');
        var dotEl = document.getElementById('status-dot');
        var msgEl = document.getElementById('sensor-status-msg');

        valEl.textContent = '-.-';
        valEl.style.color = '#000000';

        arrowEl.textContent = '—';
        arrowEl.style.color = '#000000';

        pillEl.textContent = 'SIN DATOS';
        pillEl.style.background = 'rgba(0, 0, 0, 0.16)';
        pillEl.style.color = '#000000';

        updEl.textContent = 'Sin lectura reciente';
        cardEl.style.borderColor = '#000000';
        dotEl.style.background = '#000000';

        msgEl.style.display = 'block';
        msgEl.textContent = customMsg || 'Sin Información de Sensor';
    }

    // Colores según rango (SIN alarmas visuales ni destellos)
    function applyThemeByGlucose(val) {
        removeBodyClass('no-data');
        document.getElementById('sensor-status-msg').style.display = 'none';

        var c = appState.config;
        var color = '#10b981'; // Verde En Rango
        var bgTint = 'rgba(16, 185, 129, 0.18)';
        var label = 'EN RANGO';

        if (val <= c.urgent_low) {
            color = '#ef4444';
            bgTint = 'rgba(239, 68, 68, 0.22)';
            label = 'URGENTE BAJA';
        } else if (val < c.low_threshold) {
            color = '#f59e0b';
            bgTint = 'rgba(245, 158, 11, 0.22)';
            label = 'GLUCOSA BAJA';
        } else if (val >= c.urgent_high) {
            color = '#c084fc';
            bgTint = 'rgba(192, 132, 252, 0.22)';
            label = 'URGENTE ALTA';
        } else if (val > c.high_threshold) {
            color = '#fb923c';
            bgTint = 'rgba(251, 146, 60, 0.22)';
            label = 'GLUCOSA ALTA';
        }

        var valEl = document.getElementById('glucose-value');
        var arrowEl = document.getElementById('trend-arrow');
        var pillEl = document.getElementById('range-pill');
        var cardEl = document.getElementById('reading-card');
        var dotEl = document.getElementById('status-dot');

        valEl.style.color = color;
        arrowEl.style.color = color;
        pillEl.style.background = bgTint;
        pillEl.style.color = color;
        pillEl.textContent = label;
        cardEl.style.borderColor = color;
        dotEl.style.background = color;

        return color;
    }

    // Dibujar gráfico SVG (en el modal de Gráfico de Tendencia)
    function renderGraph(history, activeColor) {
        var svg = document.getElementById('trend-svg');
        if (!history || history.length === 0) {
            svg.innerHTML = '';
            document.getElementById('stat-avg').textContent = '--';
            document.getElementById('stat-min').textContent = '--';
            document.getElementById('stat-max').textContent = '--';
            document.getElementById('stat-tir').textContent = '--';
            return;
        }

        var w = 600, h = 240, padX = 34, padY = 20;
        var minVal = 40, maxVal = 300;
        var sum = 0, minSeen = 999, maxSeen = 0, inRangeCount = 0;

        for (var i = 0; i < history.length; i++) {
            var v = history[i].value;
            sum += v;
            if (v < minSeen) minSeen = v;
            if (v > maxSeen) maxSeen = v;
            if (v >= appState.config.low_threshold && v <= appState.config.high_threshold) {
                inRangeCount++;
            }
            if (v > maxVal - 10) maxVal = v + 20;
        }

        document.getElementById('stat-avg').textContent = Math.round(sum / history.length);
        document.getElementById('stat-min').textContent = minSeen;
        document.getElementById('stat-max').textContent = maxSeen;
        document.getElementById('stat-tir').textContent = Math.round((inRangeCount / history.length) * 100) + '%';

        function mapY(val) {
            var ratio = (val - minVal) / (maxVal - minVal);
            return h - padY - (ratio * (h - padY * 2));
        }
        function mapX(idx) {
            if (history.length <= 1) return w / 2;
            return padX + (idx / (history.length - 1)) * (w - padX * 2);
        }

        var yHigh = mapY(appState.config.high_threshold);
        var yLow  = mapY(appState.config.low_threshold);

        var html = '';
        html += '<rect x="' + padX + '" y="' + yHigh + '" width="' + (w - padX * 2) + '" height="' + Math.max(2, yLow - yHigh) + '" fill="rgba(16, 185, 129, 0.09)" rx="6" />';
        html += '<line x1="' + padX + '" y1="' + yHigh + '" x2="' + (w - padX) + '" y2="' + yHigh + '" stroke="rgba(251, 146, 60, 0.35)" stroke-dasharray="5,5" stroke-width="1.5" />';
        html += '<line x1="' + padX + '" y1="' + yLow + '" x2="' + (w - padX) + '" y2="' + yLow + '" stroke="rgba(239, 68, 68, 0.45)" stroke-dasharray="5,5" stroke-width="1.5" />';

        html += '<text x="4" y="' + (yHigh + 4) + '" fill="#94a3b8" font-size="11" font-weight="bold">' + appState.config.high_threshold + '</text>';
        html += '<text x="4" y="' + (yLow + 4) + '" fill="#94a3b8" font-size="11" font-weight="bold">' + appState.config.low_threshold + '</text>';

        var points = [];
        for (var j = 0; j < history.length; j++) {
            points.push(mapX(j).toFixed(1) + ',' + mapY(history[j].value).toFixed(1));
        }

        var strokeCol = activeColor || '#10b981';
        html += '<polyline fill="none" stroke="' + strokeCol + '" stroke-width="3.5" stroke-linecap="round" stroke-linejoin="round" points="' + points.join(' ') + '" />';

        for (var k = 0; k < history.length; k++) {
            var cx = mapX(k).toFixed(1);
            var cy = mapY(history[k].value).toFixed(1);
            var r = (k === history.length - 1) ? 6 : 3.2;
            html += '<circle cx="' + cx + '" cy="' + cy + '" r="' + r + '" fill="' + strokeCol + '" />';
        }

        svg.innerHTML = html;
    }

    function handleStatusResponse(rawText) {
        try {
            var data = JSON.parse(rawText);
            if (data.history && Array.isArray(data.history)) {
                appState.lastHistory = data.history;
            }
            var staleLimit = (appState.config && appState.config.stale_minutes) ? parseInt(appState.config.stale_minutes, 10) : 5;
            if (isNaN(staleLimit) || staleLimit <= 0) staleLimit = 5;

            if (!data.ok || data.glucose === null || data.glucose === undefined || isNaN(parseInt(data.glucose, 10)) || (data.minutes_ago !== undefined && parseInt(data.minutes_ago, 10) > staleLimit)) {
                setNoDataState('Sin Información de Sensor');
                return data;
            }

            document.getElementById('patient-name').textContent = data.patient || appState.config.patient_name;
            document.getElementById('source-badge').textContent = (data.source || 'LIBRELINKUP').toUpperCase();
            document.getElementById('glucose-value').textContent = data.glucose;
            document.getElementById('trend-arrow').textContent = data.arrow || '→';
            document.getElementById('updated-pill').textContent = data.minutes_ago <= 0 ? 'Ahora mismo' : ('hace ' + data.minutes_ago + ' min');

            var activeColor = applyThemeByGlucose(parseInt(data.glucose, 10));
            appState.lastColor = activeColor;
            renderGraph(appState.lastHistory, activeColor);
            return data;
        } catch (e) {
            setNoDataState('Sin Información de Sensor');
            return { ok: false, auth_error: true, error: 'Respuesta inválida del servidor.' };
        }
    }

    function fetchGlucoseData() {
        if (appState.source === 'demo' && appState.preset === 'nodata') {
            setNoDataState('Sin Información de Sensor');
            return;
        }

        if (window.MugNative && typeof window.MugNative.fetchStatus === 'function') {
            setTimeout(function() {
                try {
                    var resText = window.MugNative.fetchStatus(appState.source, appState.preset);
                    handleStatusResponse(resText);
                } catch (e) {
                    setNoDataState('Sin Información de Sensor');
                }
            }, 10);
            return;
        }

        var url = 'api.php?action=status&source=' + encodeURIComponent(appState.source) + '&preset=' + encodeURIComponent(appState.preset) + '&_t=' + Date.now();
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    handleStatusResponse(xhr.responseText);
                } else {
                    setNoDataState('Sin Información de Sensor');
                }
            }
        };
        xhr.send();
    }

    function setDemoPreset(preset, btnEl) {
        appState.source = 'demo';
        appState.preset = preset;
        var chips = document.querySelectorAll('.chip');
        for (var i = 0; i < chips.length; i++) {
            chips[i].className = 'chip';
        }
        if (btnEl) btnEl.className = 'chip active';
        fetchGlucoseData();
    }

    function updateModalSourceFields() {
        var src = document.getElementById('cfg-source').value;
        document.getElementById('fields-llu').style.display = (src === 'librelinkup') ? 'block' : 'none';
        document.getElementById('fields-ns').style.display  = (src === 'nightscout') ? 'block' : 'none';
        var fb = document.getElementById('config-feedback');
        if (fb) { fb.style.display = 'none'; fb.className = 'modal-feedback'; }
    }

    function showConfigFeedback(type, htmlMsg) {
        var fb = document.getElementById('config-feedback');
        fb.className = 'modal-feedback ' + type;
        fb.style.display = 'block';
        fb.innerHTML = htmlMsg;
    }

    function buildConfigPayload() {
        var payload = {
            source: document.getElementById('cfg-source').value,
            llu_email: document.getElementById('cfg-llu-email').value.trim(),
            llu_region: document.getElementById('cfg-llu-region').value,
            ns_url: document.getElementById('cfg-ns-url').value.trim(),
            ns_token: document.getElementById('cfg-ns-token').value.trim(),
            patient_name: document.getElementById('cfg-patient-name').value.trim() || 'FreeStyle Libre',
            urgent_low: parseInt(document.getElementById('cfg-urgent-low').value, 10) || 55,
            low_threshold: parseInt(document.getElementById('cfg-low').value, 10) || 70,
            high_threshold: parseInt(document.getElementById('cfg-high').value, 10) || 180,
            urgent_high: parseInt(document.getElementById('cfg-urgent-high').value, 10) || 250,
            stale_minutes: parseInt(document.getElementById('cfg-stale-minutes').value, 10) || 5
        };
        var pass = document.getElementById('cfg-llu-password').value;
        if (pass && pass !== '********') {
            payload.llu_password = pass;
        }
        return payload;
    }

    // Guardar y verificar conexión mostrando Loading y Mensaje de Error si falla
    function saveAndVerifyAccount() {
        var payload = buildConfigPayload();
        var passVal = document.getElementById('cfg-llu-password').value;

        if (payload.source === 'librelinkup') {
            if (!payload.llu_email || !passVal) {
                showConfigFeedback('error', '❌ Ingresa tu correo y contraseña de LibreLinkUp.');
                return;
            }
        } else if (payload.source === 'nightscout') {
            if (!payload.ns_url) {
                showConfigFeedback('error', '❌ Ingresa la URL de tu servidor Nightscout.');
                return;
            }
        }

        var btn = document.getElementById('btn-save-config');
        btn.disabled = true;
        btn.textContent = 'Verificando...';
        showConfigFeedback('loading', '<span class="spinner"></span>Conectando y verificando cuenta...');

        appState.source = payload.source;
        appState.config = payload;
        document.getElementById('bottom-demo-bar').style.display = (appState.source === 'demo') ? 'flex' : 'none';

        if (window.MugNative && typeof window.MugNative.saveConfig === 'function') {
            setTimeout(function() {
                try {
                    window.MugNative.saveConfig(JSON.stringify(payload));
                    var resText = window.MugNative.fetchStatus(appState.source, appState.preset);
                    var data = handleStatusResponse(resText);

                    btn.disabled = false;
                    btn.textContent = 'Guardar y Conectar';

                    if (data && data.auth_error) {
                        showConfigFeedback('error', '❌ ' + (data.error || 'No se pudo conectar a la cuenta. Verifica tus datos.'));
                        return;
                    }

                    showConfigFeedback('success', '✅ ¡Cuenta verificada y conectada!');
                    setTimeout(function() {
                        closeModal('config-modal');
                    }, 550);
                } catch (e) {
                    btn.disabled = false;
                    btn.textContent = 'Guardar y Conectar';
                    showConfigFeedback('error', '❌ Error de red al verificar la cuenta.');
                }
            }, 60);
            return;
        }

        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'api.php?action=save_config', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status !== 200) {
                    btn.disabled = false;
                    btn.textContent = 'Guardar y Conectar';
                    showConfigFeedback('error', '❌ Error de comunicación con el servidor local.');
                    return;
                }
                var verifyXhr = new XMLHttpRequest();
                var url = 'api.php?action=status&source=' + encodeURIComponent(appState.source) + '&preset=' + encodeURIComponent(appState.preset) + '&_t=' + Date.now();
                verifyXhr.open('GET', url, true);
                verifyXhr.onreadystatechange = function() {
                    if (verifyXhr.readyState === 4) {
                        btn.disabled = false;
                        btn.textContent = 'Guardar y Conectar';
                        if (verifyXhr.status === 200) {
                            var data = handleStatusResponse(verifyXhr.responseText);
                            if (data && data.auth_error) {
                                showConfigFeedback('error', '❌ ' + (data.error || 'Correo o contraseña incorrectos.'));
                                return;
                            }
                            showConfigFeedback('success', '✅ ¡Cuenta verificada y conectada!');
                            setTimeout(function() {
                                closeModal('config-modal');
                            }, 550);
                        } else {
                            showConfigFeedback('error', '❌ Sin respuesta al verificar la cuenta.');
                        }
                    }
                };
                verifyXhr.send();
            }
        };
        xhr.send(JSON.stringify(payload));
    }

    function saveThresholdsOnly() {
        var payload = buildConfigPayload();
        appState.config = payload;
        if (window.MugNative && typeof window.MugNative.saveConfig === 'function') {
            window.MugNative.saveConfig(JSON.stringify(payload));
            closeModal('alarm-modal');
            fetchGlucoseData();
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.open('POST', 'api.php?action=save_config', true);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                closeModal('alarm-modal');
                fetchGlucoseData();
            }
        };
        xhr.send(JSON.stringify(payload));
    }

    function acceptLegalDisclaimer() {
        try { localStorage.setItem('mug_legal_accepted', '1'); } catch (e) {}
        if (appState.config) appState.config.legal_accepted = true;
        var closeBtn = document.getElementById('btn-close-legal');
        if (closeBtn) closeBtn.style.display = 'inline-block';
        closeModal('legal-modal');

        if (window.MugNative && typeof window.MugNative.saveConfig === 'function') {
            try { window.MugNative.saveConfig(JSON.stringify({ legal_accepted: true })); } catch (e) {}
        }

        // Si aún no tiene cuenta configurada, abrir directamente Conectar Cuenta
        if (appState.source === 'librelinkup' && (!appState.config || !appState.config.llu_email)) {
            openConnectFromMenu();
        }
    }

    function checkFirstLaunchLegal(c) {
        var acceptedLocal = false;
        try { acceptedLocal = (localStorage.getItem('mug_legal_accepted') === '1'); } catch (e) {}
        var acceptedCfg = (c && c.legal_accepted === true);
        if (!acceptedLocal && !acceptedCfg) {
            var closeBtn = document.getElementById('btn-close-legal');
            if (closeBtn) closeBtn.style.display = 'none';
            openModal('legal-modal');
        }
    }

    function applyLoadedConfig(rawText) {
        try {
            var res = JSON.parse(rawText);
            if (res.ok && res.config) {
                var c = res.config;
                appState.config = c;
                appState.source = c.source || 'librelinkup';
                document.getElementById('cfg-source').value = appState.source;
                document.getElementById('cfg-llu-email').value = c.llu_email || '';
                document.getElementById('cfg-llu-password').value = c.llu_password || '';
                document.getElementById('cfg-llu-region').value = c.llu_region || 'global';
                document.getElementById('cfg-ns-url').value = c.ns_url || '';
                document.getElementById('cfg-patient-name').value = c.patient_name || 'FreeStyle Libre';
                document.getElementById('cfg-urgent-low').value = c.urgent_low || 55;
                document.getElementById('cfg-low').value = c.low_threshold || 70;
                document.getElementById('cfg-high').value = c.high_threshold || 180;
                document.getElementById('cfg-urgent-high').value = c.urgent_high || 250;
                document.getElementById('cfg-stale-minutes').value = c.stale_minutes || 5;
                updateModalSourceFields();
                document.getElementById('bottom-demo-bar').style.display = (appState.source === 'demo') ? 'flex' : 'none';
                checkFirstLaunchLegal(c);
                fetchGlucoseData();
            } else {
                checkFirstLaunchLegal(null);
                setNoDataState('Sin Información de Sensor');
            }
        } catch (e) {
            checkFirstLaunchLegal(null);
            setNoDataState('Sin Información de Sensor');
        }
    }

    function loadSavedConfig() {
        if (window.MugNative && typeof window.MugNative.getConfig === 'function') {
            applyLoadedConfig(window.MugNative.getConfig());
            return;
        }
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'api.php?action=get_config&_t=' + Date.now(), true);
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    applyLoadedConfig(xhr.responseText);
                } else {
                    setNoDataState('Sin Información de Sensor');
                }
            }
        };
        xhr.send();
    }

    // Iniciar en estado sin datos (-.-) hasta recibir lectura válida (< 5 min) y actualizar cada 60s
    setNoDataState('Sin Información de Sensor');
    loadSavedConfig();
    setInterval(fetchGlucoseData, 60000);
</script>
</body>
</html>
