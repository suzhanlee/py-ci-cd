# Stage 1: Build
FROM gradle:8.8-jdk17 as build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

COPY src ./src

RUN ./gradlew build -x test

# Stage 2: Run
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar dev.jar

ENTRYPOINT ["java", "-jar", "dev.jar"]
EXPOSE 8080
