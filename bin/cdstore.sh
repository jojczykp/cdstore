#!/bin/sh

SERVICE_NAME=cdstore

PATH_TO_JAR=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.jar
PATH_TO_CFG=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.yml
PATH_TO_PID=/var/run/${SERVICE_NAME}/${SERVICE_NAME}.pid

PATH_TO_OUT=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.out
PATH_TO_ERR=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.err

java -version > /dev/null || { echo "ERROR: java executable not found" ; exit 1; }

case $1 in
    start)
        echo "Starting ${SERVICE_NAME} ..."
        if [ ! -f ${PATH_TO_PID} ]; then
            nohup \
                java -jar ${PATH_TO_JAR} server ${PATH_TO_CFG} 2> ${PATH_TO_ERR} > ${PATH_TO_OUT} && \
                PID=$! & \
                echo "Process PID: ${PID}"
                echo ${PID} > ${PATH_TO_PID} \
                    &
            echo "${SERVICE_NAME} waiting ..."
            while [ $(curl -s http://localhost:8081/healthcheck?pretty=true | grep "healthy" | grep "true" | wc -l) -ne 2 ]
            do
                echo "${SERVICE_NAME} waiting for healthcheck ..."
                sleep 1
            done
            echo "${SERVICE_NAME} started ..."
            echo "===== STDOUT ====="
            cat ${PATH_TO_OUT}
            echo "===== STDERR ====="
            cat ${PATH_TO_OUT}
            echo "=================="
        else
            PID=$(cat ${PATH_TO_PID});
            echo "${SERVICE_NAME} is already running (${PID}) ..."
        fi
    ;;

    stop)
        if [ -f ${PATH_TO_PID} ]; then
            PID=$(cat ${PATH_TO_PID});
            echo "${SERVICE_NAME} stopping (${PID})..."
            kill ${PID};
            while [ $(netstat -ano | grep 8081 | grep LISTEN | wc -l) -gt 0 ]
            do
                echo "${SERVICE_NAME} waiting for stop ..."
                sleep 1
            done
            echo "${SERVICE_NAME} stopped ..."
            rm ${PATH_TO_PID}
        else
            echo "${SERVICE_NAME} is not running ..."
        fi
    ;;

    restart)
        if [ -f ${PATH_TO_PID} ]; then
            PID=$(cat ${PATH_TO_PID});
            echo "${SERVICE_NAME} stopping ...";
            kill ${PID};
            echo "${SERVICE_NAME} stopped ...";
            rm ${PATH_TO_PID}
            echo "${SERVICE_NAME} starting ..."
            nohup java -jar ${PATH_TO_JAR} ${PATH_TO_CFG} 2>> ${PATH_TO_ERR} >> ${PATH_TO_OUT} &
                        echo $! > ${PATH_TO_PID}
            echo "${SERVICE_NAME} started ..."
        else
            echo "${SERVICE_NAME} is not running ..."
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

esac
