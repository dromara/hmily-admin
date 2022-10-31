#!/bin/bash



[[ -d ./conf-ext ]] && cp -f ./conf-ext/* ./conf

DEPLOY_DIR=$(pwd)
EXT_LIB=${DEPLOY_DIR}/ext-lib

CLASS_PATH=.:${DEPLOY_DIR}/conf:${DEPLOY_DIR}/lib/*:${EXT_LIB}/*
if [ -z "${ADMIN_JVM}" ]; then
    JAVA_OPTS=" -server -Xmx2g -Xms2g -Xmn1g -Xss256k -XX:+DisableExplicitGC  -XX:LargePageSizeInBytes=128m"
    JAVA_OPTS="${JAVA_OPTS} -XX:+UseFastAccessorMethods  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly  -XX:CMSInitiatingOccupancyFraction=70"
    echo "Use default jvm options: $JAVA_OPTS"
else
    JAVA_OPTS=${ADMIN_JVM}
    echo "Start with the environment variable JAVA_OPTS set: $JAVA_OPTS"
fi

echo "Starting the dromara hmily Admin ..."
exec ${JAVA_HOME}/bin/java ${JAVA_OPTS} -classpath ${CLASS_PATH} org.dromara.hmily.admin.AdminApplication "$@"
