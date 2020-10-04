echo "installing a new version of Anime Ninja..."
sleep 2
rm Anime.Ninja.jar
rm "Anime Ninja.jar"
mv ~/Desktop/Anime.Ninja.Latest.jar ~/Desktop/Anime.Ninja.jar
java -jar ~/Desktop/Anime.Ninja.jar & rm "updater.sh"
