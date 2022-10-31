#!/bin/bash
#
#

SERVER_NAME=Hmily-Admin

cd `dirname $0`
cd ..
DEPLOY_DIR=`pwd`

PIDS=`ps ax |grep -v tail|grep -v grep|grep "$DEPLOY_DIR"|awk '{print $1}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit 1
fi

echo -e "Stopping the $SERVER_NAME ...\c"
for PID in ${PIDS} ; do
    kill ${PID} > /dev/null 2>&1
done

COUNT=0
while [ ${COUNT} -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    for PID in ${PIDS} ; do
        PID_EXIST=`ps -f -p ${PID} | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            break
        fi
    done
done

echo "OK!"
echo "PID: $PIDS"
