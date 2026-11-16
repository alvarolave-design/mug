@echo off
chcp 65001 >nul
powershell -NoProfile -ExecutionPolicy Bypass -File "c:\laragon\www\mug\builds\compilar-apk.ps1"
pause
