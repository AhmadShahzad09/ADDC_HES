FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/ms-oum-task-listener*.jar

COPY ${JAR_FILE} ms-oum-task-listener.jar
ENV JAVA_OPTS="-Duser.timezone=UTC"
ENTRYPOINT ["java","-jar","/ms-oum-task-listener.jar"]

