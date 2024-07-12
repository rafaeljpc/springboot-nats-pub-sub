import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.IOException
import java.util.Properties

// from gradle.properties
val testContainerVersion: String by ext
val javaVersion = JavaVersion.VERSION_21

val props = Properties()
try {
    props.load(file("$projectDir/.env").inputStream())
} catch (e: IOException) {
    println(e.message)
}

plugins {
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.allopen") version "1.9.24"

    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "io.github.soat7"
version = "0.0.1-SNAPSHOT"

if (!javaVersion.isCompatibleWith(JavaVersion.current())) {
    error(
        """
        =======================================================
        RUN WITH JAVA $javaVersion
        =======================================================
        """.trimIndent(),
    )
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.0")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.nats:jnats:2.+")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.google.guava:guava:33.2.1-jre")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.+")

    
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.ninja-squad:springmockk:4.+")
    testImplementation("io.mockk:mockk:1.+")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.awaitility:awaitility-kotlin:4.+")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = javaVersion.toString()
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
//    forkEvery = 0
    environment.putAll(
        props.entries.associate { it.key.toString() to it.value.toString() },
    )
    testLogging {
        showStandardStreams = true
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}