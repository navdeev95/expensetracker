FROM openjdk:24-jdk-slim
WORKDIR /app

# Копируем сначала файлы для зависимостей (для лучшего кеширования)
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

# Копируем исходный код
COPY src src

# Собираем проект
RUN ./gradlew build -x test

# Копируем собранный JAR
COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
