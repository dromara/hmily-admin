@echo off
set APP_NAME=hmily-admin
echo %APP_NAME%
for /f "delims=" %%i in ('cd') do  set APP_PATH=%%i
echo %APP_PATH%
set FD_DIR=%APP_PATH:~0,-3%
for /R %FD_DIR% %%f in (hmily*.jar) do (
    SET FULL_PATH=%%f
    ECHO a: %%f
    ECHO FULL_PATH: %FULL_PATH%
)
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit
java -jar  %FULL_PATH%  >../console.log  2>&1 &