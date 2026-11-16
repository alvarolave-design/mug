@echo off
chcp 65001 >nul
echo ========================================================
echo   MUG Monitor - Inicializador de Proyecto Capacitor
echo ========================================================
cd /d "c:\laragon\www\mug"

set "PATH=C:\laragon\bin\nodejs\node-v22;%PATH%"

echo [1/3] Instalando dependencias de Capacitor con Node.js...
call npm install

echo [2/3] Creando contenedor nativo Android...
if not exist "android" (
    call npx cap add android
) else (
    call npx cap sync android
)

echo [3/3] Listo. La carpeta android\ ha sido generada en c:\laragon\www\mug\android
echo Para compilar el .apk localmente abre la carpeta android en Android Studio,
echo o sube el repositorio a GitHub para que GitHub Actions compile mug-monitor.apk.
pause
