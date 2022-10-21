#!/bin/bash
[  -e `dirname $0`/prepare.sh ] && . `dirname $0`/prepare.sh

if [ -z "$PID" ]; then
    echo "ERROR: The $APP_NAME does not started!"
    exit 1
fi

for SINGLEPID in $PID ; do
    kill $SINGLEPID
    echo "The $APP_NAME is stopping ..."
done

MAX_WAIT=20
COUNT=0
while [ $COUNT -le $MAX_WAIT ]; do
    sleep 1
    ((COUNT=COUNT+1))
    for SINGLEPID in $PID ; do
        PID_EXIST=`ps -f -p $SINGLEPID | grep java`
        if [ -n "$PID_EXIST" ]; then
            echo "waiting for $APP_NAME stop ..."
            if [ $COUNT -ge $MAX_WAIT ]; then
              echo "Force to terminate the $APP_NAME - $LOG_NAME [PID: ${SINGLEPID}] ..."
              kill -9 $SINGLEPID
            fi
            break
        else
            ((COUNT=MAX_WAIT+1))
        fi
    done
done

echo "PID: $PID"
echo "The $APP_NAME has been stopped!"