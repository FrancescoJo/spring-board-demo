@ECHO OFF
SET "CURRENT_DIR=%cd%"

PUSHD %CURRENT_DIR%

CD src\frontend

cmd /c flutter build web %1

IF /I "%errorlevel%" NEQ "0" (
    POPD
    ECHO ERROR: Please follow instructions below to troubleshoot if:
    ECHO   - 'flutter' command is not found, read https://flutter.dev/docs/get-started/install/windows
    ECHO   - 'flutter build web' is failed , read https://flutter.dev/docs/get-started/web
    EXIT /B 1
)

xcopy /E build\web\* %CURRENT_DIR%\src\main\resources\static

POPD
