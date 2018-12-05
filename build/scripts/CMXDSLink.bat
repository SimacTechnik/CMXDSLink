@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  CMXDSLink startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and CMXDS_LINK_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\CMXDSLink-1.2.0.jar;%APP_HOME%\lib\dslink-0.20.1.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\commons-cli-1.4.jar;%APP_HOME%\lib\logging-0.20.1.jar;%APP_HOME%\lib\runtime_shared-0.20.1.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\jackson-core-2.9.6.jar;%APP_HOME%\lib\jackson-dataformat-msgpack-0.8.16.jar;%APP_HOME%\lib\bcprov-jdk15on-1.60.jar;%APP_HOME%\lib\netty-all-4.1.28.Final.jar;%APP_HOME%\lib\jcommander-1.72.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\msgpack-core-0.8.16.jar;%APP_HOME%\lib\jackson-databind-2.8.11.1.jar;%APP_HOME%\lib\jackson-annotations-2.8.0.jar

@rem Execute CMXDSLink
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %CMXDS_LINK_OPTS%  -classpath "%CLASSPATH%" cz.simac.cmxdslink.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable CMXDS_LINK_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%CMXDS_LINK_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega