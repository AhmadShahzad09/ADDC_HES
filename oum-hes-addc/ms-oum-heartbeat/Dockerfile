FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/ms-oum-heartbeat*.jar

COPY ${JAR_FILE} ms-oum-heartbeat.jar
ENV JAVA_OPTS="-Duser.timezone=UTC"
ENTRYPOINT ["java","-jar","/ms-oum-heartbeat.jar"]