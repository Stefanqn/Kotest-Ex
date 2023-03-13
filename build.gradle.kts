import Build_gradle.Versions.kotest

group = "sample.kotest"
version = "0.0.1-SNAPSHOT"

object Versions {
  const val kotest = "5.5.5"
  const val jvm = "17"
}

plugins {
//  id("org.springframework.boot") version "3.0.3"
//  id("io.spring.dependency-management") version "1.1.0"
  kotlin("jvm") version "1.8.10" // gradle check doesn't like var here
//  kotlin("plugin.spring") version "1.8.10"
//  application // Application plugin.
}

repositories {
  mavenCentral()
}



dependencies {
//  implementation("org.springframework.boot:spring-boot-starter")
//  implementation("org.jetbrains.kotlin:kotlin-reflect")
//  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(kotlin("test"))
  testImplementation(platform("io.kotest:kotest-bom:$kotest"))
  testImplementation("io.kotest:kotest-runner-junit5")
}

tasks.test {
  useJUnitPlatform()
}

//application {
//  mainClass.set("MainKt") // The main class of the application
//}

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = Versions.jvm
  }
}
java.sourceCompatibility = JavaVersion.VERSION_17


