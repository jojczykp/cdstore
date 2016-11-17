#! /bin/bash

git branch -r | awk -F/ '/\/release\//{print $2"/"$3}' | xargs -I {} git push origin :{} \
    || { echo "Deleting obsolete release branches failed"; exit 1; }
