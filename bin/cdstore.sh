#!/bin/bash

SERVICE_NAME=CdStore

# To be set during deployment
ENVIRONMENT=%ENVIRONMENT%

PATH_TO_CFG=/usr/lib/${SERVICE_NAME}/${ENVIRONMENT}.yml
PATH_TO_JAR=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.jar
PATH_TO_PID=/var/run/${SERVICE_NAME}/${SERVICE_NAME}.pid

PATH_TO_OUT=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.out
PATH_TO_ERR=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.err

ADMIN_PORT=""
PID=""

java -version 2>/dev/null > /dev/null || { echo "ERROR: java executable not found"; exit 1; }

get_admin_port() {
    local PORT_STR=$(cat ${PATH_TO_CFG} | grep -A10 "server:" | grep -A10 "adminConnectors:" | grep "port:" | cut -d ':' -f 2)
    $! || { echo "ERROR: can't read admin port"; exit 1; }

    ADMIN_PORT=${PORT_STR//[[:blank:]]/}
    echo "Admin port: ${ADMIN_PORT}"
}

write_pid() {
    echo "Writing running process PID ${PID} to ${PATH_TO_PID}"
    echo ${PID} > ${PATH_TO_PID} || { echo "ERROR writing PID"; exit 1; }
}

read_pid() {
    PID=$(cat ${PATH_TO_PID}) || { echo "ERROR: cannot read PID from ${PATH_TO_PID}"; exit 1; }
}

start() {
    BUILD_ID=dontKillMe nohup java -jar ${PATH_TO_JAR} server ${PATH_TO_CFG} 2> ${PATH_TO_ERR} > ${PATH_TO_OUT} &
    PID=$!

    echo "Started ${SERVICE_NAME}, PID: ${PID}"
}

stop() {
    if [ -z ${PID} ]
    then
        echo "Running process not found. Skipping stopping ${SERVICE_NAME}"
    else
        echo "Stopping ${SERVICE_NAME}, PID: ${PID}..."
        kill ${PID} || echo "WARNING: cannot kill ${PID}"
    fi
    rm ${PATH_TO_PID} || echo "WARNING: cannot delete PID file ${PATH_TO_PID}"
}

wait_for_start() {
    local i=0
    while [ $(curl -s http://localhost:${ADMIN_PORT}/healthcheck?pretty=true | grep "healthy" | grep "true" | wc -l) -ne 2 ]
    do
        ((i++)) && ((i>10)) && break
        echo "Waiting for ${SERVICE_NAME} health check (${i}) ..."
        sleep 1
    done
}

wait_for_stop_admin_port() {
    local i=0
    while [ $(netstat -ano | grep ${ADMIN_PORT} | grep LISTEN | wc -l) -gt 0 ]
    do
        ((i++)) && ((i>10)) && { echo "ERROR: Waiting for ${SERVICE_NAME} to close admin port timeout"; exit 1; }
        echo "Waiting for ${SERVICE_NAME} to close admin port ${ADMIN_PORT} (${i}) ..."
        sleep 1
    done
}

wait_for_stop_pid() {
    if [ -z ${PID} ]
    then
        echo "Skipping waiting for process to close (unknown PID)"
    else
        local i=0
        while [ -e /proc/${PID} ]
        do
            ((i++)) && ((i>10)) && { echo "ERROR: Waiting for ${SERVICE_NAME} to stop timeout"; exit 1; }
            echo "Waiting for ${SERVICE_NAME} to terminate (${i}) ..."
            sleep 1
        done
    fi
}

wait_for_stop() {
    wait_for_stop_admin_port
    wait_for_stop_pid
}

print_outs() {
    echo "===== STDOUT ====="
    cat ${PATH_TO_OUT}
    echo "===== STDERR ====="
    cat ${PATH_TO_ERR}
    echo "=================="
}

case ${1} in
    start)
        if [ ! -f ${PATH_TO_PID} ]
        then
            echo "Starting ${SERVICE_NAME}"
            get_admin_port
            start
            write_pid
            wait_for_start
            print_outs
            echo "${SERVICE_NAME} started"
        else
            echo "${SERVICE_NAME} already running"
            read_pid
        fi
    ;;

    stop)
        if [ -f ${PATH_TO_PID} ]
        then
            echo "Stopping ${SERVICE_NAME}"
            read_pid
            get_admin_port
            stop
            wait_for_stop
            echo "${SERVICE_NAME} stopped"
        else
            echo "${SERVICE_NAME} is not running"
        fi
    ;;

    *)
        echo "Usage: $0 [start|stop]"
    ;;

esac
