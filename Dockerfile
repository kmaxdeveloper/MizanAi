# 1-bosqich: Build
FROM gradle:7.6-jdk17-alpine AS build
WORKDIR /home/gradle/src

# Keshlanishni to'g'ri ishlashi uchun ikkala faylni ham kopiya qilamiz
COPY build.gradle settings.gradle.kts ./
# Kutubxonalarni yuklab olamiz
RUN gradle dependencies --no-daemon || return 0

# Qolgan kodni kopiya qilib, build qilamiz
COPY . .
RUN gradle bootJar --no-daemon -x test

# 2-bosqich: Run
FROM amazoncorretto:17-alpine
WORKDIR /app
# Build bosqichidan faqat kerakli .jar faylni olamiz
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Java xotira limitlari
ENV JAVA_OPTS="-Xmx256m -Xms128m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]