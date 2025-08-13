FROM openjdk:24-jdk-slim
WORKDIR /app

# Копируем сначала файлы для зависимостей (для лучшего кеширования)
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

ARG REBUILD_CACHE=$(date +%s)
RUN echo "Forcing rebuild at ${REBUILD_CACHE}"

# Копируем исходный код
COPY src src

# Собираем проект
RUN ./gradlew clean build -x test

ARG REBUILD_CACHE=$(date +%s)
RUN echo "Forcing rebuild at ${REBUILD_CACHE}"

# Проверяем структуру
RUN ls -la build/libs/

# Копируем собранный JAR
COPY build/libs/app.jar app.jar

ARG REBUILD_CACHE=$(date +%s)
RUN echo "Forcing rebuild at ${REBUILD_CACHE}"

ENTRYPOINT ["java","-jar","app.jar"]
