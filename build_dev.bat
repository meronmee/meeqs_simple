@title 打包开发版本
@echo off

call  mvn clean install -Dmaven.test.skip=true -Pdev

echo.
echo 恭喜，【开发版本】打包成功！
echo.
echo WAR包路径：%~dp0target\

echo.
pause.
