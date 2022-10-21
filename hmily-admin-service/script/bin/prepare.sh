#!/bin/bash
cd `dirname $0`
export APP_BIN=`pwd`
cd ..
export APP_NAME=hmily-admin
export APP_HOME=`pwd`
export APP_HOME_REAL=`readlink -f $APP_HOME`
export APP_CONF=$APP_HOME/config
export APP_LIB=$APP_HOME/lib
export APP_LOG_HOME=/export/log/$APP_NAME
#export APP_STATIC_RESOURCE=$APP_HOME/static/
export STDOUT_FILE=$APP_LOG_HOME/console.log
export PID_FILE="$APP_HOME/pid"
PID=`ps -ef | grep java | grep "$APP_HOME" | awk '{print $2}'`
export PID