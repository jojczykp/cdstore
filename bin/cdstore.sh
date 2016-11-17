#!/bin/sh

SERVICE_NAME=cdstore

PATH_TO_JAR=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.jar
PATH_TO_CFG=/usr/lib/${SERVICE_NAME}/${SERVICE_NAME}.yml
PID_PATH_NAME=/var/run/${SERVICE_NAME}.pid

STDOUT=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.out
STDERR=/var/log/${SERVICE_NAME}/${SERVICE_NAME}.err

java -version || { echo "ERROR: java executable not found" ; exit 1; }

case $1 in
    start)
        echo "Starting ${SERVICE_NAME} ..."
        if [ ! -f ${PID_PATH_NAME} ]; then
            nohup java -jar ${PATH_TO_JAR} ${PATH_TO_CFG} 2>> ${STDERR} >> ${STDOUT} &
            echo $! > ${PID_PATH_NAME}
            echo "${SERVICE_NAME} started ..."
        else
            echo "${SERVICE_NAME} is already running ..."
        fi
    ;;

    stop)
        if [ -f ${PID_PATH_NAME} ]; then
            PID=$(cat ${PID_PATH_NAME});
            echo "${SERVICE_NAME} stopping ..."
            kill ${PID};
            echo "${SERVICE_NAME} stopped ..."
            rm ${PID_PATH_NAME}
        else
            echo "${SERVICE_NAME} is not running ..."
        fi
    ;;

    restart)
        if [ -f ${PID_PATH_NAME} ]; then
            PID=$(cat ${PID_PATH_NAME});
            echo "${SERVICE_NAME} stopping ...";
            kill ${PID};
            echo "${SERVICE_NAME} stopped ...";
            rm ${PID_PATH_NAME}
            echo "${SERVICE_NAME} starting ..."
            nohup java -jar ${PATH_TO_JAR} ${PATH_TO_CFG} 2>> ${STDERR} >> ${STDOUT} &
                        echo $! > ${PID_PATH_NAME}
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

