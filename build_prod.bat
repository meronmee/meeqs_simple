@title 打包生产版本
@echo off

rd /S /Q  %~dp0target

call  mvn clean install -Dmaven.test.skip=true -Pprod

echo.
echo 恭喜，【生产版本】打包成功！
echo.
echo WAR包路径：%~dp0target\

echo.
pause.
