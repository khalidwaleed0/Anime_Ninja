@echo off
echo installing a new version of Anime Ninja
timeout 2 >nul
TASKKILL /im javaw.exe
DEL Anime.Ninja.exe
DEL "Anime Ninja.exe"
move %USERPROFILE%\Desktop\Anime.Ninja(Latest).exe %USERPROFILE%\Desktop\Anime.Ninja.exe
%USERPROFILE%\Desktop\Anime.Ninja.exe
del updater.bat
