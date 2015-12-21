#!/bin/sh

source $HOME/.bashrc

# admin does full DNS lookup on FLEX_OMHOST which sometimes doesn't work.
# In this product it's always running on localhost -- override it to point there.
export FLEX_OMHOST=localhost


echo Starting Audit log service

if [ ! -n "$FLEX_SYSLOG" ]; then
        echo "Env FLEX_SYSLOG is not set, Can not proceed"
        exit 1
fi

if [ ! -n "$FLEXSYS" ]; then
        echo "Env FLEXSYS is not set, Can not proceed"
        exit 1
fi

DATE=`date +%Y%m%d`

LOG_DIR=$FLEX_SYSLOG/trades/$DATE

if [ ! -d $LOG_DIR ]; then
        mkdir -p $LOG_DIR
fi

if [ ! -d $LOG_DIR ]; then
        echo "Log directory $LOG_DIR does not exist"
        exit 1
fi

JAVA_8_HOME=$HOME/jdk1.8.0_60
PROFILE=-Dspring.profiles.active=ctfx-development
EXECUTABLE=$FLEXPRODUCT/jars/audit-log-service.jar
ENCODING=-Dfile.encoding=UTF-8
XMX=-Xmx500m
XMS=-Xms500m

echo "Using Java $JAVA_8_HOME"
echo "Using profile $PROFILE"
echo "Using file encoding $ENCODING"
echo "Using executable $EXECUTABLE"

${JAVA_8_HOME}/bin/java -jar $XMX $XMS $PROFILE $ENCODING $EXECUTABLE > $LOG_DIR/audit$$.log 2>&1 &

echo "Audit log started"
