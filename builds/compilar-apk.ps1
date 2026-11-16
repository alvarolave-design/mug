$ErrorActionPreference = "Stop"
$ProjectRoot = "c:\laragon\www\mug"
$BuildsDir   = "$ProjectRoot\builds"
$ToolsDir    = "$BuildsDir\.tools"
$JdkDir      = "$ToolsDir\jdk-21"
$SdkDir      = "$ToolsDir\android-sdk"

New-Item -ItemType Directory -Force -Path $BuildsDir | Out-Null
New-Item -ItemType Directory -Force -Path $ToolsDir | Out-Null

$env:PATH = "C:\laragon\bin\nodejs\node-v22;$env:PATH"
Set-Location $ProjectRoot

Write-Host "==> [1/5] Sincronizando interfaz web hacia www\index.html..." -ForegroundColor Cyan
New-Item -ItemType Directory -Force -Path "$ProjectRoot\www" | Out-Null
Copy-Item "$ProjectRoot\index.php" "$ProjectRoot\www\index.html" -Force
Copy-Item "$ProjectRoot\manifest.json" "$ProjectRoot\www\manifest.json" -Force
Copy-Item "$ProjectRoot\icon.svg" "$ProjectRoot\www\icon.svg" -Force
if (Test-Path "$ProjectRoot\logo.png") {
    Copy-Item "$ProjectRoot\logo.png" "$ProjectRoot\www\logo.png" -Force
}

Write-Host "==> [2/5] Instalando paquetes de Capacitor..." -ForegroundColor Cyan
if (-not (Test-Path "$ProjectRoot\node_modules\@capacitor\android")) {
    & npm.cmd install
}

if (-not (Test-Path "$ProjectRoot\android")) {
    & npx.cmd cap add android
} else {
    & npx.cmd cap sync android
}

# Asegurar que MainActivity.java no tenga BOM UTF-8
$MainAct = Get-ChildItem -Path "$ProjectRoot\android\app\src\main\java" -Filter "MainActivity.java" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
if ($MainAct) {
    $rawJava = [System.IO.File]::ReadAllText($MainAct.FullName).TrimStart([char]0xFEFF)
    [System.IO.File]::WriteAllText($MainAct.FullName, $rawJava, (New-Object System.Text.UTF8Encoding($false)))
}

Write-Host "==> [3/5] Verificando JDK 21 portable..." -ForegroundColor Cyan
$JavacExe = Get-ChildItem -Path $JdkDir -Filter "javac.exe" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
if (-not $JavacExe) {
    $JdkZip = "$ToolsDir\jdk21.zip"
    Write-Host "    Descargando Eclipse Temurin JDK 21 portable..."
    Invoke-WebRequest -Uri "https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse" -OutFile $JdkZip
    Expand-Archive -Path $JdkZip -DestinationPath $JdkDir -Force
    Remove-Item $JdkZip -Force
    $JavacExe = Get-ChildItem -Path $JdkDir -Filter "javac.exe" -Recurse | Select-Object -First 1
}
$env:JAVA_HOME = $JavacExe.Directory.Parent.FullName
$env:PATH = "$($JavacExe.Directory.FullName);$env:PATH"

Write-Host "==> [4/5] Verificando Android SDK Command-Line Tools..." -ForegroundColor Cyan
$SdkManager = "$SdkDir\cmdline-tools\latest\bin\sdkmanager.bat"
if (-not (Test-Path $SdkManager)) {
    $CmdZip = "$ToolsDir\cmdline-tools.zip"
    Write-Host "    Descargando Android Command-Line Tools..."
    Invoke-WebRequest -Uri "https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip" -OutFile $CmdZip
    New-Item -ItemType Directory -Force -Path "$SdkDir\cmdline-tools" | Out-Null
    Expand-Archive -Path $CmdZip -DestinationPath "$SdkDir\cmdline-tools" -Force
    if (Test-Path "$SdkDir\cmdline-tools\cmdline-tools") {
        Rename-Item -Path "$SdkDir\cmdline-tools\cmdline-tools" -NewName "latest" -Force
    }
    Remove-Item $CmdZip -Force
}

$env:ANDROID_HOME = $SdkDir
$env:ANDROID_SDK_ROOT = $SdkDir

# Aceptar licencias de Android SDK automáticamente creando los hashes oficiales
$LicensesDir = "$SdkDir\licenses"
New-Item -ItemType Directory -Force -Path $LicensesDir | Out-Null
@"
8933bad161af4178b1185d1a37fbf41ea5269c55
d56f5187479451eabf01fb78af6dfcb131a6481e
24333f8a63b6825ea9c5514f83c2829b004d1fee
859f317696f67ef3d7f30a50a5560e7834b43903
"@ | Set-Content -Path "$LicensesDir\android-sdk-license" -Encoding ASCII

@"
84831b9409646a918e30573bab4c9c91346d8abd
504667f4c0de7af1a06de9f4b1727b84351f2910
"@ | Set-Content -Path "$LicensesDir\android-sdk-preview-license" -Encoding ASCII

Write-Host "==> [5/5] Compilando APK con Gradle..." -ForegroundColor Cyan
Set-Location "$ProjectRoot\android"
"sdk.dir=$($SdkDir.Replace('\','\\'))" | Set-Content -Path "$ProjectRoot\android\local.properties" -Encoding ASCII

& .\gradlew.bat assembleDebug

$ApkOut = "$ProjectRoot\android\app\build\outputs\apk\debug\app-debug.apk"
if (Test-Path $ApkOut) {
    Copy-Item $ApkOut "$BuildsDir\mug-monitor.apk" -Force
    Write-Host ""
    Write-Host "========================================================" -ForegroundColor Green
    Write-Host " APK GENERADO EXITOSAMENTE EN:" -ForegroundColor Green
    Write-Host " c:\laragon\www\mug\builds\mug-monitor.apk" -ForegroundColor Yellow
    Write-Host "========================================================" -ForegroundColor Green
} else {
    Write-Error "No se encontró el archivo APK compilado."
}
