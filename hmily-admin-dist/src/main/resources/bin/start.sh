#!/bin/bash
#


SERVER_NAME=Hmily-Admin

cd `dirname $0`
cd ..
DEPLOY_DIR=`pwd`

LOGS_DIR=${DEPLOY_DIR}/logs
if [ ! -d ${LOGS_DIR} ]; then
    mkdir ${LOGS_DIR}
fi

LOG_FILES=${LOGS_DIR}/hmily-admin.log
EXT_LIB=${DEPLOY_DIR}/ext-lib

CLASS_PATH=.:${DEPLOY_DIR}/conf:${DEPLOY_DIR}/lib/*:${EXT_LIB}/*
if [ -z "${ADMIN_JVM}" ]; then
    JAVA_OPTS=" -server -Xmx2g -Xms2g -Xmn1g -Xss256k -XX:+DisableExplicitGC  -XX:LargePageSizeInBytes=128m"
    version=`java -version 2>&1 | sed '1!d' | sed -e 's/"//g' | awk '{print $3}'`
    echo "current jdk version:${version}"
    if [[ "$version" =~ "1.8" ]];then
        JAVA_OPTS="${JAVA_OPTS} -XX:+UseFastAccessorMethods  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly  -XX:CMSInitiatingOccupancyFraction=70"
    elif [[ "$version" =~ "11" ]];then
        JAVA_OPTS="${JAVA_OPTS}"
    elif [[ "$version" =~ "17" ]];then
        JAVA_OPTS="${JAVA_OPTS}"
    fi
    echo "Use default jvm param: $JAVA_OPTS"
else
    JAVA_OPTS=${ADMIN_JVM}
    echo "Start with the environment variable JAVA_OPTS set: $JAVA_OPTS"
fi

MAIN_CLASS=org.dromara.hmily.admin.AdminApplication
echo "Starting the $SERVER_NAME ..."

nohup java ${JAVA_OPTS} -classpath ${CLASS_PATH} ${MAIN_CLASS} >> ${LOG_FILES} 2>&1 &
sleep 1
echo "Please check the log files: $LOG_FILES"
