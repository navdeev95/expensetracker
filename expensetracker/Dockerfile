FROM openjdk:24-jdk-slim
WORKDIR /app

COPY build.gradle .
COPY --chmod=+x gradlew .
COPY src ./src

RUN ./gradlew build -x test
CMD ["java", "-jar", "build/libs/app.jar"]
