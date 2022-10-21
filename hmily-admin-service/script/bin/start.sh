#!/bin/bash
#
# Usage: start.sh [debug]
#
[  -e `dirname $0`/env_app.sh ] && . `dirname $0`/env_app.sh
[  -e `dirname $0`/env_before.sh ] && . `dirname $0`/env_before.sh
[  -e `dirname $0`/env.sh ] && . `dirname $0`/env.sh
[  -e `dirname $0`/prepare.sh ] && . `dirname $0`/prepare.sh

if [ ! -d "$APP_LOG_HOME" ] ;then
    mkdir -p $APP_LOG_HOME
fi

if [ -n "$PID" ]; then
    echo "ERROR: The $APP_NAME already started!"
    echo "PID: $PID"
    exit 1
fi

echo "Starting the $APP_NAME ..."
echo "JAVA_HOME: $JAVA_HOME"
echo "APP_HOME: $APP_HOME"
echo "APP_LOG_HOME: $APP_LOG_HOME"
echo "STDOUT_FILE: $STDOUT_FILE"

#APP_STATIC_RESOURCE=$APP_HOME/static
#if [ -d "$APP_STATIC_RESOURCE" ] ;then
#    JAVA_OPTS="$JAVA_OPTS -Dspring.resources.static-locations=file://${APP_STATIC_RESOURCE}"
#    echo "Using APP_STATIC_RESOURCE:     $APP_STATIC_RESOURCE"
#fi

if [ -d "$APP_HOME" ]; then
    APP_LAUNCHER_JAR=`ls $APP_HOME | grep .jar`
    if [ -n "$APP_LAUNCHER_JAR" ]; then
        APP_LAUNCHER_JAR="$APP_HOME/$APP_LAUNCHER_JAR"
    fi
fi
echo "Using APP_LAUNCHER_JAR:     $APP_LAUNCHER_JAR"

#appoint database type
if [ $# == 1 ]; then
    dbType=$1
    sed -i "s!active.*!active: ${dbType:1}!g" config/application.yml
    echo "Use database "$1" , the configuration file location is /config/application$1.yml"
fi
#appoint database type and its info
if [ $# -ge 2 ]; then
    dbType=$1
    sed -i "s!active.*!active: ${dbType:1}!g" config/application.yml
    for var in $@
    do  if [[ "$var" == "-url"* ]]; then
            if [[ "$1" == "-mysql" ]]; then
                mysqlUrl="jdbc:mysql://${var:4}?useUnicode=true"
                sed -i "s!url.*!url: $mysqlUrl!g" config/application$1.yml
            fi;
            if [[ "$1" == "-mongo" ]]; then
                mongoUrl=${var:4}
                myArray=(${mongoUrl//// })
                sed -i "s!mongoDbUrl.*!mongoDbUrl: ${myArray[0]}!g" config/application$1.yml
                sed -i "s!mongoDbName.*!mongoDbName: ${myArray[1]}!g" config/application$1.yml
            fi;
            sleep 1
        fi
        if [[ "$var" = "-u"* ]]; then
            userName=${var:2}
            if [[ "$1" == "-mysql" ]]; then
                sed -i "s!username.*!username: $userName!g" config/application$1.yml
            fi;
            if [[ "$1" == "-mongo" ]]; then
                sed -i "s!mongoUserName.*!mongoUserName: $userName!g" config/application$1.yml
            fi;
            sleep 1
        fi
        if [[ "$var" = "-p"* ]]; then
            password=${var:2}
            if [[ "$1" == "-mysql" ]]; then
                sed -i "s!password.*!password: $password!g" config/application$1.yml
            fi;
            if [[ "$1" == "-mongo" ]]; then
                sed -i "s!mongoUserPwd.*!mongoUserPwd: $password!g" config/application$1.yml
            fi;
            sleep 1
        fi
    done
    echo "Use database $1 , the configuration file location is /config/application$1.yml"
fi
#if [ -n "$ENCRYPTED_ENABLE" ]; then
#    #export LD_LIBRARY_PATH="$APP_HOME/library"
#    CLASSES_LIBRARY_OPTS="-agentpath:$APP_HOME/library/libClassesSecurity.so"
#    echo "Using CLASSES_LIBRARY_OPTS: $CLASSES_LIBRARY_OPTS"
#fi
echo "-----------"
echo "$JAVA_HOME/bin/java $CLASSES_LIBRARY_OPTS $SGM_OPTS $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -cp $APP_CONF:$APP_LAUNCHER_JAR -jar $APP_LAUNCHER_JAR"
if [ -n "$FOREGROUND_MODE" ] ;then
    $JAVA_HOME/bin/java $CLASSES_LIBRARY_OPTS $SGM_OPTS $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -cp $APP_CONF:$APP_LAUNCHER_JAR -jar $APP_LAUNCHER_JAR
else
    nohup $JAVA_HOME/bin/java $CLASSES_LIBRARY_OPTS $SGM_OPTS $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -cp $APP_CONF:$APP_LAUNCHER_JAR -jar $APP_LAUNCHER_JAR > $STDOUT_FILE 2>&1 &
fi


COUNT=0
while [ $COUNT -lt 1 ]; do    
    sleep 1
    COUNT=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}' | wc -l`
    echo "ps check count[$COUNT]"
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PID=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}'`
#echo $PID > $PID_FILE
echo "PID: $PID"
sleep 1