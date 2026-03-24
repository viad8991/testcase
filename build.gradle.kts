plugins {
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"

    id("java")
}

group = "ru.test"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.slf4j:slf4j-api:2.0.17")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.2.1")
    // testImplementation("org.wiremock:wiremock-standalone:3.13.2")
    testImplementation("org.mockito:mockito-core:5.21.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()

    jvmArgs("-Dspring.profiles.active=test")
}
