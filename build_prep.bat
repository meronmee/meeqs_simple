@title ���Ԥ�����汾
@echo off

call  mvn clean install -Dmaven.test.skip=true -Pprep

echo.
echo ��ϲ����Ԥ�����汾������ɹ���
echo.
echo WAR��·����%~dp0target\

echo.
pause.
