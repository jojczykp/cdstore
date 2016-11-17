#!/bin/sh

SERVICE_NAME=cdstore

PATH_TO_JAR=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.jar
PATH_TO_CFG=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.yml
PATH_TO_PID=/var/run/${SERVICE_NAME}/${SERVICE_NAME}.pid

PATH_TO_OUT=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.out
PATH_TO_ERR=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.err

ADMIN_PORT=8081

java -version > /dev/null || { echo "ERROR: java executable not found"; exit 1; }

write_pid() {
    echo ${1} >  ${PATH_TO_PID}
}

read_pid() {
    echo $(cat ${PATH_TO_PID})
}

start() {
    echo "jar: ${PATH_TO_JAR}"
    echo "out: ${PATH_TO_OUT}"
    echo "err: ${PATH_TO_ERR}"
    nohup \
        java -jar ${PATH_TO_JAR} server ${PATH_TO_CFG} 2> ${PATH_TO_ERR} > ${PATH_TO_OUT} & \
        PID=$! ; \
        echo "Started ${SERVICE_NAME}, PID: ${PID}" ; \
        echo ${PID} > ${PATH_TO_PID} ; \
        echo "Wrote PID file ${PATH_TO_PID}"
}

stop() {
    local PID=$(read_pid)
    echo "Stopping ${SERVICE_NAME}, PID: ${PID}..."
    kill -SIGTERM ${PID}
    echo "Deleting PID file ${PATH_TO_PID}"
    rm ${PATH_TO_PID}
}

wait_for_start() {
    while [ $(curl -s http://localhost:${ADMIN_PORT}/healthcheck?pretty=true | grep "healthy" | grep "true" | wc -l) -ne 2 ]
    do
        echo "Waiting for ${SERVICE_NAME} health check ..."
        sleep 1
    done
    echo "${SERVICE_NAME} started"
}

wait_for_stop() {
    while [ $(netstat -ano | grep ${ADMIN_PORT} | grep LISTEN | wc -l) -gt 0 ]
    do
        echo "Waiting for ${SERVICE_NAME} admin port (${ADMIN_PORT}) to close ..."
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
            stop
        else
            echo "${SERVICE_NAME} is not running"
        fi
    ;;

#    install)
#        mkdir -p /usr/lib/${SERVICE_NAME}
#        mkdir -p /var/run
#        mkdir -p /var/log/${SERVICE_NAME}
#    ;;
#
#    uninstall)
#        rm -rf /usr/lib/${SERVICE_NAME}
#        rm -rf /var/run
#        rm -rf /var/log/${SERVICE_NAME}
#    ;;

    *)
        echo "Usage: $0 [start|stop]"
    ;;

esac
