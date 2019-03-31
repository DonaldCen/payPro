@echo off
if '%1=='## goto ENVSET

SET APPHOME=%~dp0
SET LIBDIR=%APPHOME%lib
SET CLSPATH=conf
SET SERVER_LOG=./logs/server.out
SET JAVA_OPTS=

if not exist ./logs (mkdir logs)

if exist ./conf/server.conf (for /f "tokens=* delims=server.java.opts=" %%i in ('findstr "server.java.opts=*" %APPHOME%conf\server.conf') do set JAVA_OPTS=%%i)

FOR %%c in (%LIBDIR%\*.jar) DO Call %0 ## %%c

if "%1" == "h" goto RUN
echo Ams-Server is started.
echo Press any key to backend run.
pause
mshta vbscript:createobject("wscript.shell").run("%~nx0 h",0)(window.close)&&exit

:RUN
echo (Using JAVA_OPTS=%JAVA_OPTS%)...
echo Ams-Server is started.
java %JAVA_OPTS% -cp %CLSPATH% yx.pay.FebsApplication > %SERVER_LOG% 2>&1 &
goto END

:ENVSET
set CLSPATH=%CLSPATH%;%2
goto END

:END