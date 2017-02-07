#!/bin/bash

cd /migration

curl -X POST http://localhost:8000 \
    -H 'Content-Type: application/x-amz-json-1.0' \
    -H 'Authorization: AWS4-HMAC-SHA256 Credential=XXX, SignedHeaders=YYY, Signature=ZZZ' \
    -H 'X-Amz-Target: DynamoDB_20120810.CreateTable' \
    --data @cdstore-Albums.json
