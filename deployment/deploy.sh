#!/usr/bin/env bash

MASTER=$1
TAG=$2
INSTANCES_COUNT=$3

sed -e "s/@@TAG@@/$TAG/" -e "s/@@INSTANCES_COUNT@@/$INSTANCES_COUNT/" marathon-config.json.template > marathon-config.json


# we want curl to output something we can use to indicate success/failure

STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://$MASTER/marathon/v2/apps -H "Content-Type: application/json" --data-binary "@marathon-config.json")
echo "HTTP code " $STATUS
if [ $STATUS == "201" ]
then exit 0
else exit 1
fi
