@title 打包预生产版本
@echo off

call  mvn clean install -Dmaven.test.skip=true -Pprep

echo.
echo 恭喜，【预生产版本】打包成功！
echo.
echo WAR包路径：%~dp0target\

echo.
pause.
