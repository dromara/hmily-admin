#!/bin/bash
if [[ ! -d "$JAVA_HOME" ]] ;then
    export JAVA_HOME=`find / -name jdk1.8* | head -1`
    if [[ ! -d "$JAVA_HOME" ]] ;then
        echo "ERROR: Cannot Found JAVA Installed in $JAVA_HOME" >&2
        exit 1
    fi
    export PATH=$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
fi
if [[ -n "$APP_NAME" ]] ;then
    SPRINT_BOOT_OPTS="$SPRINT_BOOT_OPTS -Dspring.application.name=$APP_NAME"
fi
if [[ -n "$LOCAL_PROFILE_ACTIVE" ]] ;then
    SPRINT_BOOT_OPTS="$SPRINT_BOOT_OPTS -Dspring.profiles.active=$LOCAL_PROFILE_ACTIVE"
fi
#if [[ -n "$ARIES_SERVICE_URL" ]] ;then
#    ARIES_OPTS="-Deureka.client.service-url.defaultZone=$ARIES_SERVICE_URL"
#fi
if [[ -n "$REMOTE_PROFILE_ACTIVE" ]] ;then
    REMOTE_CONFIG_OPTS="-Dspring.cloud.config.profile=$REMOTE_PROFILE_ACTIVE"
fi
if [[ -n "$MAX_JAVA_HEAP" ]] ;then
    JAVA_HEAP_OPTS="-Xms$MAX_JAVA_HEAP -Xmx$MAX_JAVA_HEAP"
fi


echo "SPRINT_BOOT_OPTS:      $SPRINT_BOOT_OPTS"
echo "ARIES_OPTS:            $ARIES_OPTS"
echo "REMOTE_CONFIG_OPTS:    $REMOTE_CONFIG_OPTS"
echo "JAVA_HEAP_OPTS:        $JAVA_HEAP_OPTS"

JAVA_OPTS="$JAVA_OPTS $JAVA_HEAP_OPTS $ARIES_OPTS $REMOTE_CONFIG_OPTS $SPRINT_BOOT_OPTS"
