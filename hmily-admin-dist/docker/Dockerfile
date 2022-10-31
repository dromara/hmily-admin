

FROM openjdk:8-jre-alpine

ARG APP_NAME

ENV LOCAL_PATH /opt/hmily-admin
ENV ADMIN_JVM ""

RUN apk --no-cache add procps

ADD target/${APP_NAME}.tar.gz /opt
RUN mv /opt/${APP_NAME} ${LOCAL_PATH}

COPY docker/logback.xml ${LOCAL_PATH}/conf/logback.xml
COPY docker/entrypoint.sh ${LOCAL_PATH}/entrypoint.sh
RUN chmod +x ${LOCAL_PATH}/entrypoint.sh

WORKDIR /opt/hmily-admin

EXPOSE 8888

ENTRYPOINT ["/bin/sh", "entrypoint.sh"]