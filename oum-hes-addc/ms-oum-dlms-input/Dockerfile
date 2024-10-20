FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/ms-oum-dlms-input*.jar

COPY ${JAR_FILE} ms-oum-dlms-input.jar
ENV JAVA_OPTS="-Duser.timezone=UTC"
ENTRYPOINT ["java","-jar","/ms-oum-dlms-input.jar"]