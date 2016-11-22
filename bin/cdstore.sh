#!/bin/sh

SERVICE_NAME=cdstore

# To be set during deployment
ENVIRONMENT=%ENVIRONMENT%

PATH_TO_CFG=/usr/lib/${SERVICE_NAME}/${ENVIRONMENT}.yml
PATH_TO_JAR=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.jar
PATH_TO_PID=/var/run/${SERVICE_NAME}/${SERVICE_NAME}.pid

PATH_TO_OUT=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.out
PATH_TO_ERR=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.err

java -version 2>/dev/null > /dev/null || { echo "ERROR: java executable not found"; exit 1; }

get_admin_port() {
    local PORT_STR=$(cat ${PATH_TO_CFG} | grep -A10 "server:" | grep -A10 "adminConnectors:" | grep "port:" | cut -d ':' -f 2)
    if [[ $! -ne 0 ]]
    then
        echo "ERROR: can't read admin port"
        exit 1
    fi

    ADMIN_PORT=${PORT_STR//[[:blank:]]/}
    echo "Admin port: '${ADMIN_PORT}'"
}

write_pid() {
    echo ${1} >  ${PATH_TO_PID}
}

read_pid() {
    echo $(cat ${PATH_TO_PID} || { echo "ERROR: cannot read PID from ${PATH_TO_PID}"; exit 1; } )
}

start() {
    echo "jar: ${PATH_TO_JAR}"
    echo "out: ${PATH_TO_OUT}"
    echo "err: ${PATH_TO_ERR}"

    BUILD_ID=dontKillMe nohup java -jar ${PATH_TO_JAR} server ${PATH_TO_CFG} 2> ${PATH_TO_ERR} > ${PATH_TO_OUT} &

    PID=$!
    echo "Started ${SERVICE_NAME}, PID: ${PID}"
    echo ${PID} > ${PATH_TO_PID}
    echo "Wrote PID file ${PATH_TO_PID}"
}

stop() {
    local PID=$(read_pid)
    echo "Stopping ${SERVICE_NAME}, PID: ${PID}..."
    kill ${PID} || exit 1
    echo "Deleting PID file ${PATH_TO_PID}"
    rm ${PATH_TO_PID}
}

wait_for_start() {
    local i=0
    while [ $(curl -s http://localhost:${ADMIN_PORT}/healthcheck?pretty=true | grep "healthy" | grep "true" | wc -l) -ne 2 ]
    do
        ((i++)) && ((i>10)) && break
        echo "Waiting for ${SERVICE_NAME} health check (${i}) ..."
        sleep 1
    done
    echo "${SERVICE_NAME} started"
}

wait_for_stop() {
    local i=0
    while [ $(netstat -ano | grep ${ADMIN_PORT} | grep LISTEN | wc -l) -gt 0 ]
    do
        ((i++)) && ((i>10)) && break
        echo "Waiting for ${SERVICE_NAME} admin port (${ADMIN_PORT}) to close (${i}) ..."
        sleep 1
    done

    local PID=$(read_pid)
    echo "Waiting for ${SERVICE_NAME} process (${PID}) to terminate ..."
    wait ${PID}

    echo "${SERVICE_NAME} stopped"
}

print_outs() {
    echo "===== STDOUT ====="
    cat ${PATH_TO_OUT}
    echo "===== STDERR ====="
    cat ${PATH_TO_ERR}
    echo "=================="
}

case $1 in
    start)
        echo "Starting ${SERVICE_NAME} ..."
        if [ ! -f ${PATH_TO_PID} ]
        then
            get_admin_port
            start
            wait_for_start
            print_outs
        else
            PID=$(read_pid)
            echo "${SERVICE_NAME} is already running, PID: ${PID}"
        fi
    ;;

    stop)
        if [ -f ${PATH_TO_PID} ]
        then
            get_admin_port
            stop
            wait_for_stop
        else
            echo "${SERVICE_NAME} is not running"
        fi
    ;;

    *)
        echo "Usage: $0 [start|stop]"
    ;;

esac
