plugins {
    kotlin("jvm") version "2.2.20"
    id("org.springframework.boot") version "3.2.2" // Spring Boot versiyasi
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("plugin.spring") version "1.9.22"
}

group = "uz.mizanai"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Office va PDF tahlili uchun
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    // Bazaga ulanish (Hozircha H2 yoki PostgreSQL)
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-webflux") // AI API uchun
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}