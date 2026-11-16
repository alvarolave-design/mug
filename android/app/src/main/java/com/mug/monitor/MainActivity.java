package com.mug.monitor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import com.getcapacitor.BridgeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getBridge() != null && getBridge().getWebView() != null) {
            getBridge().getWebView().addJavascriptInterface(new MugNativeBridge(this), "MugNative");
        }
    }

    public static class MugNativeBridge {
        private final Context context;
        private final SharedPreferences prefs;

        public MugNativeBridge(Context ctx) {
            this.context = ctx;
            this.prefs = ctx.getSharedPreferences("mug_device_prefs", Context.MODE_PRIVATE);
        }

        @JavascriptInterface
        public void openExternalUrl(String url) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception ignored) {}
        }

        @JavascriptInterface
        public String getConfig() {
            try {
                JSONObject cfg = new JSONObject();
                cfg.put("source", prefs.getString("source", "librelinkup"));
                cfg.put("llu_email", prefs.getString("llu_email", ""));
                String pass = prefs.getString("llu_password", "");
                cfg.put("llu_password", pass.isEmpty() ? "" : "********");
                cfg.put("llu_region", prefs.getString("llu_region", "global"));
                cfg.put("ns_url", prefs.getString("ns_url", ""));
                cfg.put("ns_token", prefs.getString("ns_token", ""));
                cfg.put("patient_name", prefs.getString("patient_name", "FreeStyle Libre"));
                cfg.put("urgent_low", prefs.getInt("urgent_low", 55));
                cfg.put("low_threshold", prefs.getInt("low_threshold", 70));
                cfg.put("high_threshold", prefs.getInt("high_threshold", 180));
                cfg.put("urgent_high", prefs.getInt("urgent_high", 250));
                cfg.put("stale_minutes", prefs.getInt("stale_minutes", 5));

                JSONObject res = new JSONObject();
                res.put("ok", true);
                res.put("config", cfg);
                return res.toString();
            } catch (Exception e) {
                return "{\"ok\":false}";
            }
        }

        @JavascriptInterface
        public String saveConfig(String jsonStr) {
            try {
                JSONObject in = new JSONObject(jsonStr);
                SharedPreferences.Editor ed = prefs.edit();
                if (in.has("source")) ed.putString("source", in.optString("source", "librelinkup"));
                if (in.has("llu_email")) ed.putString("llu_email", in.optString("llu_email", "").trim());
                if (in.has("llu_password")) {
                    String p = in.optString("llu_password", "");
                    if (!p.isEmpty() && !p.equals("********")) {
                        ed.putString("llu_password", p);
                    }
                }
                if (in.has("llu_region")) ed.putString("llu_region", in.optString("llu_region", "global"));
                if (in.has("ns_url")) ed.putString("ns_url", in.optString("ns_url", "").trim());
                if (in.has("ns_token")) ed.putString("ns_token", in.optString("ns_token", "").trim());
                if (in.has("patient_name")) ed.putString("patient_name", in.optString("patient_name", "FreeStyle Libre"));
                if (in.has("urgent_low")) ed.putInt("urgent_low", in.optInt("urgent_low", 55));
                if (in.has("low_threshold")) ed.putInt("low_threshold", in.optInt("low_threshold", 70));
                if (in.has("high_threshold")) ed.putInt("high_threshold", in.optInt("high_threshold", 180));
                if (in.has("urgent_high")) ed.putInt("urgent_high", in.optInt("urgent_high", 250));
                if (in.has("stale_minutes")) ed.putInt("stale_minutes", in.optInt("stale_minutes", 5));

                ed.remove("cached_token");
                ed.remove("cached_expires");
                ed.remove("cached_account_hash");
                ed.remove("cached_host");
                ed.apply();

                return getConfig();
            } catch (Exception e) {
                return "{\"ok\":false,\"auth_error\":true,\"error\":\"Error al guardar configuración.\"}";
            }
        }

        @JavascriptInterface
        public String fetchStatus(String sourceParam, String presetParam) {
            try {
                String source = (sourceParam != null && !sourceParam.isEmpty())
                        ? sourceParam
                        : prefs.getString("source", "librelinkup");

                if ("librelinkup".equals(source)) {
                    return fetchLibreLinkUp().toString();
                } else if ("nightscout".equals(source)) {
                    return fetchNightscout().toString();
                } else {
                    return fetchDemo(presetParam).toString();
                }
            } catch (Exception e) {
                try {
                    JSONObject err = new JSONObject();
                    err.put("ok", false);
                    err.put("auth_error", true);
                    err.put("error", "Error de red al conectar. Verifica tu conexión a internet.");
                    return err.toString();
                } catch (Exception ex) {
                    return "{\"ok\":false,\"auth_error\":true,\"error\":\"Error de conexión.\"}";
                }
            }
        }

        private String sha256(String input) throws Exception {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }

        private String httpCall(String urlStr, String method, String version, String token, String accountHash, String body) throws Exception {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("product", "llu.android");
            conn.setRequestProperty("version", version);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36");
            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            if (accountHash != null && !accountHash.isEmpty()) {
                conn.setRequestProperty("Account-Id", accountHash);
            }
            if (body != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }
            }
            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
            if (is == null) return "{}";
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            return sb.toString();
        }

        private int extractMgDl(JSONObject item) {
            if (item == null) return 0;
            if (item.has("ValueInMgPerDl") && item.optDouble("ValueInMgPerDl", 0) > 0) {
                return (int) Math.round(item.optDouble("ValueInMgPerDl", 0));
            }
            if (item.has("Value") && item.optDouble("Value", 0) > 0) {
                double v = item.optDouble("Value", 0);
                return (v < 35.0) ? (int) Math.round(v * 18.0182) : (int) Math.round(v);
            }
            return 0;
        }

        private long parseLibreTimestamp(String factoryRaw, String localRaw) {
            String[] formats = new String[]{
                "M/d/yyyy h:mm:ss a",
                "yyyy-MM-dd'T'HH:mm:ss",
                "MM/dd/yyyy hh:mm:ss a",
                "d/M/yyyy HH:mm:ss"
            };
            if (factoryRaw != null && !factoryRaw.isEmpty()) {
                for (String fmt : formats) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date d = sdf.parse(factoryRaw);
                        if (d != null) return d.getTime() / 1000L;
                    } catch (Exception ignored) {}
                }
            }
            if (localRaw != null && !localRaw.isEmpty()) {
                for (String fmt : formats) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
                        Date d = sdf.parse(localRaw);
                        if (d != null) return d.getTime() / 1000L;
                    } catch (Exception ignored) {}
                }
            }
            return System.currentTimeMillis() / 1000L;
        }

        private String resolveRegionalHost(String region, String version) {
            try {
                String raw = httpCall("https://api.libreview.io/llu/config/country?country=DE", "GET", version, "", "", null);
                JSONObject cfg = new JSONObject(raw);
                JSONObject data = cfg.optJSONObject("data");
                if (data != null && data.has("RegionalMap")) {
                    JSONObject rMap = data.optJSONObject("RegionalMap");
                    if (rMap != null && rMap.has(region)) {
                        String lsl = rMap.getJSONObject(region).optString("lslApi", "");
                        if (!lsl.isEmpty()) {
                            return lsl.endsWith("/") ? lsl.substring(0, lsl.length() - 1) : lsl;
                        }
                    }
                }
            } catch (Exception ignored) {}
            return "https://api-" + region + ".libreview.io";
        }

        private JSONObject fetchLibreLinkUp() throws Exception {
            JSONObject out = new JSONObject();
            String email = prefs.getString("llu_email", "").trim();
            String pass = prefs.getString("llu_password", "");
            String region = prefs.getString("llu_region", "global");

            if (email.isEmpty() || pass.isEmpty()) {
                out.put("ok", false);
                out.put("auth_error", true);
                out.put("error", "Ingresa tu correo y contraseña de LibreLinkUp.");
                return out;
            }

            String version = prefs.getString("cached_version", "4.16.0");
            String token = prefs.getString("cached_token", "");
            long expires = prefs.getLong("cached_expires", 0);
            String accountHash = prefs.getString("cached_account_hash", "");
            String host = prefs.getString("cached_host", "");
            if (host.isEmpty()) {
                host = "global".equals(region) ? "https://api.libreview.io" : ("https://api-" + region + ".libreview.io");
            }

            long nowSec = System.currentTimeMillis() / 1000L;

            if (token.isEmpty() || expires < nowSec) {
                JSONObject loginBody = new JSONObject();
                loginBody.put("email", email);
                loginBody.put("password", pass);

                String loginRaw = httpCall(host + "/llu/auth/login", "POST", version, "", "", loginBody.toString());
                JSONObject loginJson = new JSONObject(loginRaw);
                JSONObject loginData = loginJson.optJSONObject("data");

                if (loginData != null && !loginData.optString("minimumVersion", "").isEmpty()) {
                    version = loginData.optString("minimumVersion", "4.16.0");
                    loginRaw = httpCall(host + "/llu/auth/login", "POST", version, "", "", loginBody.toString());
                    loginJson = new JSONObject(loginRaw);
                    loginData = loginJson.optJSONObject("data");
                }

                if (loginData != null && loginData.optBoolean("redirect", false) && !loginData.optString("region", "").isEmpty()) {
                    String newRegion = loginData.optString("region");
                    host = resolveRegionalHost(newRegion, version);
                    loginRaw = httpCall(host + "/llu/auth/login", "POST", version, "", "", loginBody.toString());
                    loginJson = new JSONObject(loginRaw);
                    loginData = loginJson.optJSONObject("data");
                }

                if (loginJson.optInt("status", 0) == 4) {
                    out.put("ok", false);
                    out.put("auth_error", true);
                    out.put("error", "Abre la app oficial LibreLinkUp y acepta los Términos de Uso.");
                    return out;
                }

                JSONObject ticket = (loginData != null) ? loginData.optJSONObject("authTicket") : null;
                if (ticket == null || ticket.optString("token", "").isEmpty()) {
                    out.put("ok", false);
                    out.put("auth_error", true);
                    out.put("error", "Correo o contraseña incorrectos en LibreLinkUp. Verifica tus datos.");
                    return out;
                }

                token = ticket.optString("token", "");
                expires = ticket.optLong("expires", nowSec + 3600L);
                JSONObject userObj = loginData.optJSONObject("user");
                String userId = (userObj != null) ? userObj.optString("id", "") : "";
                accountHash = userId.isEmpty() ? "" : sha256(userId);

                prefs.edit()
                    .putString("cached_token", token)
                    .putLong("cached_expires", expires)
                    .putString("cached_account_hash", accountHash)
                    .putString("cached_host", host)
                    .putString("cached_version", version)
                    .apply();
            }

            String connRaw = httpCall(host + "/llu/connections", "GET", version, token, accountHash, null);
            JSONObject connJson = new JSONObject(connRaw);
            JSONArray connArr = connJson.optJSONArray("data");

            if (connArr == null || connArr.length() == 0) {
                prefs.edit().remove("cached_token").apply();
                out.put("ok", false);
                out.put("auth_error", true);
                out.put("error", "Esta cuenta de LibreLinkUp no tiene pacientes compartidos activos.");
                return out;
            }

            JSONObject patient = connArr.getJSONObject(0);
            for (int i = 0; i < connArr.length(); i++) {
                JSONObject cand = connArr.getJSONObject(i);
                if (cand.has("glucoseMeasurement") || cand.has("glucoseItem")) {
                    patient = cand;
                    break;
                }
            }

            String patientId = patient.optString("patientId", "");
            String patientName = (patient.optString("firstName", "") + " " + patient.optString("lastName", "")).trim();

            String graphRaw = httpCall(host + "/llu/connections/" + patientId + "/graph", "GET", version, token, accountHash, null);
            JSONObject graphJson = new JSONObject(graphRaw);
            JSONObject graphDataNode = graphJson.optJSONObject("data");
            JSONObject connNode = (graphDataNode != null) ? graphDataNode.optJSONObject("connection") : null;
            JSONArray graphPoints = (graphDataNode != null) ? graphDataNode.optJSONArray("graphData") : null;

            JSONObject current = null;
            if (connNode != null && connNode.optJSONObject("glucoseMeasurement") != null) {
                current = connNode.optJSONObject("glucoseMeasurement");
            } else if (connNode != null && connNode.optJSONObject("glucoseItem") != null) {
                current = connNode.optJSONObject("glucoseItem");
            } else if (patient.optJSONObject("glucoseMeasurement") != null) {
                current = patient.optJSONObject("glucoseMeasurement");
            } else if (patient.optJSONObject("glucoseItem") != null) {
                current = patient.optJSONObject("glucoseItem");
            }

            int glucose = extractMgDl(current);
            if (current == null || glucose <= 0) {
                out.put("ok", false);
                out.put("auth_ok", true);
                out.put("stale", true);
                out.put("error", "Sin Información de Sensor");
                return out;
            }

            int trendNum = current.optInt("TrendArrow", 3);
            String arrow = "→";
            if (trendNum == 1) arrow = "↓↓";
            else if (trendNum == 2) arrow = "↘";
            else if (trendNum == 4) arrow = "↗";
            else if (trendNum == 5) arrow = "↑↑";

            JSONArray history = new JSONArray();
            SimpleDateFormat hmFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            if (graphPoints != null) {
                int startIdx = Math.max(0, graphPoints.length() - 35);
                for (int i = startIdx; i < graphPoints.length(); i++) {
                    JSONObject pt = graphPoints.getJSONObject(i);
                    int v = extractMgDl(pt);
                    if (v > 0) {
                        long ts = parseLibreTimestamp(pt.optString("FactoryTimestamp", ""), pt.optString("Timestamp", ""));
                        JSONObject hItem = new JSONObject();
                        hItem.put("time", ts);
                        hItem.put("label", hmFmt.format(new Date(ts * 1000L)));
                        hItem.put("value", v);
                        history.put(hItem);
                    }
                }
            }

            long lastTs = parseLibreTimestamp(current.optString("FactoryTimestamp", ""), current.optString("Timestamp", ""));
            JSONObject lastPoint = new JSONObject();
            lastPoint.put("time", lastTs);
            lastPoint.put("label", hmFmt.format(new Date(lastTs * 1000L)));
            lastPoint.put("value", glucose);
            history.put(lastPoint);

            long minAgo = Math.max(0L, Math.round((nowSec - lastTs) / 60.0));
            int staleLimit = prefs.getInt("stale_minutes", 5);
            if (staleLimit <= 0) staleLimit = 5;

            if (minAgo > staleLimit) {
                out.put("ok", false);
                out.put("auth_ok", true);
                out.put("stale", true);
                out.put("minutes_ago", minAgo);
                out.put("error", "Sin Información de Sensor");
                out.put("history", history);
                return out;
            }

            out.put("ok", true);
            out.put("auth_ok", true);
            out.put("source", "librelinkup");
            out.put("patient", !patientName.isEmpty() ? patientName : prefs.getString("patient_name", "FreeStyle Libre"));
            out.put("glucose", glucose);
            out.put("delta", 0);
            out.put("arrow", arrow);
            out.put("timestamp", lastTs);
            out.put("minutes_ago", minAgo);
            out.put("history", history);
            return out;
        }

        private JSONObject fetchNightscout() throws Exception {
            JSONObject out = new JSONObject();
            String nsUrl = prefs.getString("ns_url", "").trim();
            String nsToken = prefs.getString("ns_token", "").trim();
            if (nsUrl.isEmpty()) {
                out.put("ok", false);
                out.put("auth_error", true);
                out.put("error", "Ingresa la URL de tu sitio Nightscout.");
                return out;
            }
            if (nsUrl.endsWith("/")) nsUrl = nsUrl.substring(0, nsUrl.length() - 1);
            String fullUrl = nsUrl + "/api/v1/entries/sgv.json?count=36";
            if (!nsToken.isEmpty()) fullUrl += "&token=" + nsToken;

            String raw = httpCall(fullUrl, "GET", "4.16.0", "", "", null);
            JSONArray arr = new JSONArray(raw);
            if (arr.length() == 0) {
                out.put("ok", false);
                out.put("auth_ok", true);
                out.put("stale", true);
                out.put("error", "Sin Información de Sensor");
                return out;
            }
            JSONObject latest = arr.getJSONObject(0);
            int sgv = latest.optInt("sgv", 0);
            String dir = latest.optString("direction", "Flat");
            String arrow = "→";
            if ("DoubleUp".equals(dir)) arrow = "↑↑";
            else if ("SingleUp".equals(dir)) arrow = "↑";
            else if ("FortyFiveUp".equals(dir)) arrow = "↗";
            else if ("FortyFiveDown".equals(dir)) arrow = "↘";
            else if ("SingleDown".equals(dir)) arrow = "↓";
            else if ("DoubleDown".equals(dir)) arrow = "↓↓";

            JSONArray history = new JSONArray();
            SimpleDateFormat hmFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject e = arr.getJSONObject(i);
                long ts = e.optLong("date", System.currentTimeMillis()) / 1000L;
                JSONObject hItem = new JSONObject();
                hItem.put("time", ts);
                hItem.put("label", hmFmt.format(new Date(ts * 1000L)));
                hItem.put("value", e.optInt("sgv", 0));
                history.put(hItem);
            }

            long lastTs = latest.optLong("date", System.currentTimeMillis()) / 1000L;
            long minAgo = Math.max(0L, Math.round(((System.currentTimeMillis() / 1000L) - lastTs) / 60.0));
            int staleLimit = prefs.getInt("stale_minutes", 5);
            if (staleLimit <= 0) staleLimit = 5;

            if (minAgo > staleLimit) {
                out.put("ok", false);
                out.put("auth_ok", true);
                out.put("stale", true);
                out.put("minutes_ago", minAgo);
                out.put("error", "Sin Información de Sensor");
                out.put("history", history);
                return out;
            }

            out.put("ok", true);
            out.put("auth_ok", true);
            out.put("source", "nightscout");
            out.put("patient", prefs.getString("patient_name", "Nightscout"));
            out.put("glucose", sgv);
            out.put("delta", 0);
            out.put("arrow", arrow);
            out.put("timestamp", lastTs);
            out.put("minutes_ago", minAgo);
            out.put("history", history);
            return out;
        }

        private JSONObject fetchDemo(String preset) throws Exception {
            JSONObject out = new JSONObject();
            if ("nodata".equals(preset)) {
                out.put("ok", false);
                out.put("auth_ok", true);
                out.put("error", "Sin Información de Sensor");
                return out;
            }
            int base = 112;
            String arrow = "→";
            int delta = 2;
            if ("low".equals(preset)) { base = 64; arrow = "↘"; delta = -6; }
            else if ("urgent_low".equals(preset)) { base = 49; arrow = "↓↓"; delta = -11; }
            else if ("high".equals(preset)) { base = 204; arrow = "↗"; delta = 8; }
            else if ("urgent_high".equals(preset)) { base = 268; arrow = "↑↑"; delta = 14; }

            long now = System.currentTimeMillis() / 1000L;
            JSONArray history = new JSONArray();
            SimpleDateFormat hmFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());
            for (int i = 24; i >= 0; i--) {
                long t = now - (i * 300L);
                int wave = (int) Math.round(Math.sin(i / 3.5) * 14.0);
                int drift = (int) Math.round(i * (-delta / 5.0));
                int val = Math.max(40, Math.min(350, base + drift + wave));
                if (i == 0) val = base;
                JSONObject pt = new JSONObject();
                pt.put("time", t);
                pt.put("label", hmFmt.format(new Date(t * 1000L)));
                pt.put("value", val);
                history.put(pt);
            }

            out.put("ok", true);
            out.put("auth_ok", true);
            out.put("source", "demo");
            out.put("patient", prefs.getString("patient_name", "FreeStyle Libre") + " (Demo)");
            out.put("glucose", base);
            out.put("delta", delta);
            out.put("arrow", arrow);
            out.put("timestamp", now);
            out.put("minutes_ago", 1);
            out.put("history", history);
            return out;
        }
    }
}
