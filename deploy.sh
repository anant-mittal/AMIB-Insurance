mvn -T 4 clean package -pl server-ui -am -DskipTests
SD_USER=amxdev
SD_HOST=10.28.42.36
scp server-ui/target/server-ui-0.0.1-SNAPSHOT.jar $SD_USER@$SD_HOST:~/amib/server-ui
ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST 'kill -9 $(cat /var/run/amib-server-ui/amib-server-ui.pid)'
ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST '/etc/init.d/amib-server-ui restart &'
