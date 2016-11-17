#! /bin/bash

DST=/usr/lib/cdstore
BIN=${DST}/cdstore.sh

${BIN} stop || { echo "Stopping service failed"; exit 1; }

mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:get \
    -Dartifact=pl.jojczykp:cdstore:LATEST \
    -Ddest=${DST}/cdstore.jar \
        || { echo "Deployment of jar file failed"; exit 1; }

cp cfg/cdstore.yml ${DST} || { echo "Deployment of config failed"; exit 1; }

cp bin/cdstore.sh ${DST} || { echo "Deployment of start/stop script failed"; exit 1; }
chmod +x ${BIN} || { echo "Changing permissions to start/stop script failed"; exit 1; }

bash ${BIN} start  || { echo "Starting service failed"; exit 1; }
