FROM openjdk:24-jdk-slim
WORKDIR /app

COPY build.gradle .
COPY gradlew .
COPY src ./src

RUN chmod +x gradlew
CMD ["java", "-jar", "build/libs/app.jar"]
