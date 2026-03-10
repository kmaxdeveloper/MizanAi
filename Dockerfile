# 1-bosqich: Build (Keshlanadigan qatlamlar bilan)
FROM gradle:7.6-jdk17-alpine AS build
WORKDIR /home/gradle/src
# Kutubxonalarni keshlab qolish uchun faqat build.gradle ni kopiya qilamiz
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon || return 0
# Kodni kopiya qilib, build qilamiz
COPY . .
RUN gradle bootJar --no-daemon -x test

# 2-bosqich: Run
FROM amazoncorretto:17-alpine
WORKDIR /app
# Build bosqichidan faqat kerakli .jar faylni olamiz
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
# Java xotira limitlarini konteyner ichida belgilaymiz
ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]