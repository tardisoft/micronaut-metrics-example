FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
CMD java ${JAVA_OPTS} -jar metrics-all.jar
COPY build/libs/*-all.jar metrics-all.jar