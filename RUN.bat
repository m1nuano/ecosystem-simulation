@echo off
setlocal

set SRC_DIR=src\main\java
set OUTPUT_DIR=out
set LOMBOK_JAR=lombok.jar
set MAIN_CLASS=com.test.Main

if exist %OUTPUT_DIR% (
    rmdir /S /Q %OUTPUT_DIR%
)
mkdir %OUTPUT_DIR%

echo Compiling Java files with Lombok...
javac -d %OUTPUT_DIR% -cp %LOMBOK_JAR% -sourcepath %SRC_DIR% %SRC_DIR%\com\test\*.java %SRC_DIR%\com\test\model\*.java %SRC_DIR%\com\test\prediction\*.java %SRC_DIR%\com\test\service\*.java %SRC_DIR%\com\test\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed. Check the error messages above.
    pause
    exit /b %errorlevel%
)

echo Running the Java program...
java -cp %OUTPUT_DIR%;%LOMBOK_JAR% %MAIN_CLASS%

pause
