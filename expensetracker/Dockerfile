FROM openjdk:24-jdk-slim

WORKDIR /app

COPY build.gradle .
COPY src ./src

RUN ./gradlew build -x test

FROM openjdk:24-jdk-slim

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
