FROM openjdk:24-jdk-slim

WORKDIR /app

COPY ../../../build/libs/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]