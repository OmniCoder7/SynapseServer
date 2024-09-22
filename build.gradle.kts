import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.jpa") version "1.9.24"
}

group = "com.proton"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// SPRING SECURITY
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("jakarta.servlet:jakarta.servlet-api:6.1.0")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	// MONGO DB
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.mongodb:bson-kotlinx:5.1.0")
	implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.1.0")

	// TOKEN VALIDATION
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
	implementation("jakarta.validation:jakarta.validation-api:3.1.0")

	// JAVA MAIL SENDER
	implementation("org.springframework.boot:spring-boot-starter-mail:3.3.2")
	implementation("org.springframework:spring-context-support:6.1.12")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
