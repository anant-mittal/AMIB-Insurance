mvn -T 4 clean package -pl server-ui -am -DskipTests
scp server-ui/target/server-ui-0.0.1-SNAPSHOT.jar $SD_USER@$SD_HOST:~/jax/server-ui-ib
ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST 'kill -9 $(cat /var/run/server-ui-ib/server-ui-ib.pid)'
ssh -o StrictHostKeyChecking=no $SD_USER@$SD_HOST '/etc/init.d/server-ui-ib restart &'
