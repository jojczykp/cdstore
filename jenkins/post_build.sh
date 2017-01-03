#! /bin/bash

DST="/usr/lib/cdstore"
BIN="${DST}/cdstore.sh"

ENVIRONMENT=${1}


echo "Deploying to ${ENVIRONMENT}, ${DST}"

${BIN} stop || { echo "Stopping service failed"; exit 1; }

mvn org.apache.maven.plugins:maven-dependency-plugin:2.10:get \
    -Dartifact=pl.jojczykp:cdstore:LATEST \
    -Ddest=${DST}/cdstore.jar \
        || { echo "Deployment of jar file failed"; exit 1; }

cp cfg/${ENVIRONMENT}.yml ${DST}/cdstore.yml || { echo "Error copying configuration file"; exit 1; }

cp bin/cdstore.sh ${DST} || { echo "Deployment of start/stop script failed"; exit 1; }
sed -i "s/%ENVIRONMENT%/${ENVIRONMENT}/" ${BIN} || { echo "Update start/stop script failed"; exit 1; }
chmod +x ${BIN} || { echo "Changing permissions to start/stop script failed"; exit 1; }

rm -rf ~/.m2/repository/pl/jojczykp/cdstore || { echo "Failed clean local maven repo"; exit 1; }

${BIN} start  || { echo "Starting service failed"; exit 1; }
