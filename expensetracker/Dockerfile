FROM openjdk:24-jdk-slim
WORKDIR /app

ARG FORCE_REBUILD=1

# Копируем сначала файлы для зависимостей (для лучшего кеширования)
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

# Копируем исходный код
COPY src src

# Собираем проект
RUN ./gradlew clean build -x test

# Проверяем структуру
RUN ls -la build/libs/

# Копируем собранный JAR
COPY build/libs/app.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
