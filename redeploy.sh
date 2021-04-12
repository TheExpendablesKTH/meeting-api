#/bin/bash


# Check if mock needs redeploying
cd /home/ubuntu/meeting-api-mock/
git pull

last="$(cat /home/ubuntu/meeting-api-mock.version)"
current="$(git rev-parse HEAD)"
if [ "$last" != "$current" ]; then
	echo "redeploying mock..."
	cd /home/ubuntu/meeting-api-mock/
	cp /home/ubuntu/application.properties-mock /home/ubuntu/meeting-api-mock/src/main/resources/application.properties
	./gradlew build
	sudo systemctl restart meeting-api-mock.service
	echo "$current" > /home/ubuntu/meeting-api-mock.version
else
	echo "mock up-to-date"
fi


# Check if master needs redeploying
cd /home/ubuntu/meeting-api-master/
git pull

last="$(cat /home/ubuntu/meeting-api-master.version)"
current="$(git rev-parse HEAD)"
if [ "$last" != "$current" ]; then
        echo "redeploying master..."
        cd /home/ubuntu/meeting-api-master/
        cp /home/ubuntu/application.properties-master /home/ubuntu/meeting-api-master/src/main/resources/application.properties
        ./gradlew build
        sudo systemctl restart meeting-api-master.service
        echo "$current" > /home/ubuntu/meeting-api-master.version
else
        echo "master up-to-date"
fi
