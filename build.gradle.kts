import Build_gradle.Versions.kotest
import Build_gradle.Versions.kotestSpring
import Build_gradle.Versions.mockk

group = "sample.kotest"
version = "0.0.1-SNAPSHOT"

object Versions {
  const val kotest = "5.5.5"
  const val kotestSpring = "1.1.2"
  const val jvm = "17"
  const val mockk = "1.13.4"
}

plugins {
  id("org.springframework.boot") version "3.0.3" //  package executable jars , run Spring Boot apps, dep management by spring-boot-dependencies
  kotlin("jvm") version "1.8.10" // gradle check doesn't like vals (object Versions) here
  kotlin("plugin.spring") version "1.8.10" // opens classes and methods with Spring annotations
//  application // Application plugin.
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
//  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(kotlin("test"))

  testImplementation(platform("io.kotest:kotest-bom:$kotest"))
  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-extensions-htmlreporter")
  testImplementation("io.kotest:kotest-extensions-junitxml")
  testImplementation("io.kotest:kotest-framework-datatest")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:$kotestSpring")
  testImplementation("io.mockk:mockk:$mockk")
}

tasks.test {
  useJUnitPlatform()
  reports {
    junitXml.required.set(false)
    html.required.set(false)
  }
  systemProperty("gradle.build.dir", project.buildDir)
}

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict") // harder null safety checks with Java annotations
    jvmTarget = Versions.jvm
  }
}
java.sourceCompatibility = JavaVersion.VERSION_17

apply(plugin = "io.spring.dependency-management") // imports spring-boot-dep boms

repositories {
  mavenCentral()
}

//application {
//  mainClass.set("MainKt") // The main class of the application
//}
