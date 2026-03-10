# Build bosqichi (Gradle orqali .jar yasash)
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Run bosqichi (Olingan .jar ni ishlatish)
FROM openjdk:17-jdk-slim
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]