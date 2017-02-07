#! /bin/bash

DST="/usr/lib/cdstore"
BIN="${DST}/cdstore.sh"

bash ${BIN} stop  || { echo "Stopping service failed"; exit 1; }

git branch -r | awk -F/ '/\/release\//{print $2"/"$3}' | xargs -I {} git push origin :{} \
    || { echo "Deleting obsolete release branches failed"; exit 1; }
