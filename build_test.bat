@title ������԰汾
@echo off

call  mvn clean install -Dmaven.test.skip=true -Ptest

echo.
echo ��ϲ�������԰汾������ɹ���
echo.
echo WAR��·����%~dp0target\

echo.
pause.
