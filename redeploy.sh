#/bin/bash


# Check if mock needs redeploying
cd /home/ubuntu/meeting-api-mock/
git pull

last="$(cat /home/ubuntu/meeting-api-mock.version)"
current="$(git rev-parse HEAD)"
if [ "$last" != "$current" ]; then
	echo "redeploying mock..."
	cd /home/ubuntu/meeting-api-mock/
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
        ./gradlew build
        sudo systemctl restart meeting-api-master.service
        echo "$current" > /home/ubuntu/meeting-api-master.version
else
        echo "master up-to-date"
fi
