@title ��������汾
@echo off

rd /S /Q  %~dp0target

call  mvn clean install -Dmaven.test.skip=true -Pprod

echo.
echo ��ϲ���������汾������ɹ���
echo.
echo WAR��·����%~dp0target\

echo.
pause.
