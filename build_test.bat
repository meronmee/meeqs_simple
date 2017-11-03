@title 打包测试版本
@echo off

call  mvn clean install -Dmaven.test.skip=true -Ptest

echo.
echo 恭喜，【测试版本】打包成功！
echo.
echo WAR包路径：%~dp0target\

echo.
pause.
