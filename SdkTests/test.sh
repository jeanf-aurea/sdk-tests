#!/bin/sh

if [ "$BUILD" = "" ]; then
	BUILD=$HOME/dev/ss.mainline
fi

# JVM_ARGS=-Dcom.actional.lg.interceptor.debug=true

# JVM_ARGS="$JVM_ARGS -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7777,suspend=y"


java -classpath bin:$BUILD/ss/build/debug/interceptor/actional-sdk.jar -Dcom.actional.lg.interceptor.config=$BUILD/ss/profiles/deved/LG.Interceptor $JVM_ARGS $*

