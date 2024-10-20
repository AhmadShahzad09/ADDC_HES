FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/ms-oum-monitor-rest*.jar

COPY ${JAR_FILE} ms-oum-monitor-rest.jar
ENV JAVA_OPTS="-Duser.timezone=UTC"
ENTRYPOINT ["java","-jar","/ms-oum-monitor-rest.jar"]