mvn -T 4 clean package -pl server-ui -am -DskipTests
SD_USER=amibdev
SD_HOST=10.28.42.12
sshpass -p $SAD_PASS scp server-ui/target/server-ui-0.0.1-SNAPSHOT.jar $SAD_USER@$SAD_HOST:/var/opt/amib/server-ui
sshpass -p $SAD_PASS ssh -o StrictHostKeyChecking=no $SAD_USER@$SAD_HOST 'kill -9 $(cat /var/run/server-ui/server-ui.pid) && /etc/init.d/server-ui restart &'
#scp server-ui/target/server-ui-0.0.1-SNAPSHOT.jar $SAD_USER@$SAD_HOST:~/amib/server-ui
#ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST 'kill -9 $(cat /var/run/amib-server-ui/amib-server-ui.pid) && /etc/init.d/amib-server-ui restart &'
ssh -o StrictHostKeyChecking=no $SAD_USER@$SAD_HOST 'tail -500f /var/log/server-ui.log'
