#!/bin/bash

hbase shell -n << EOF
create 'cdstore-Tracks', 'data'
EOF
