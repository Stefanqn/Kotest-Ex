import Build_gradle.Versions.V_KOTEST
import Build_gradle.Versions.V_KOTEST_SPRING
import Build_gradle.Versions.V_KOTEST_COMPILE
import Build_gradle.Versions.V_MOCKK
import Build_gradle.Versions.V_KOTLIN_COMPILE_TESTING

group = "sample.kotest"
version = "0.0.1-SNAPSHOT"

object Versions {
  const val V_KOTEST = "5.9.1"
  const val V_KOTEST_SPRING = "1.3.0" //for kotest 5.8
  const val V_KOTEST_COMPILE = "1.0.0"
  const val V_KOTLIN_COMPILE_TESTING = "1.6.0"
  const val V_JVM = "17"
  const val V_MOCKK = "1.13.12"
}

plugins {
  id("org.springframework.boot") version "3.3.3" //  package executable jars , run Spring Boot apps, dep management by spring-boot-dependencies
  kotlin("jvm") version "2.0.20" // gradle check doesn't like vals (object Versions) here
  kotlin("plugin.spring") version "2.0.20" // opens classes and methods with Spring annotations
//  application // Application plugin.
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation(kotlin("test"))

  testImplementation(platform("io.kotest:kotest-bom:$V_KOTEST"))
  testImplementation("io.kotest:kotest-runner-junit5")
  testImplementation("io.kotest:kotest-extensions-htmlreporter")
  testImplementation("io.kotest:kotest-extensions-junitxml")
  testImplementation("io.kotest:kotest-framework-datatest")
  testImplementation("io.kotest:kotest-property")
  testImplementation("io.kotest.extensions:kotest-extensions-spring:$V_KOTEST_SPRING")
  testImplementation("io.mockk:mockk:$V_MOCKK")
//  testImplementation("io.kotest.extensions:kotest-assertions-compiler:$V_KOTEST_COMPILE")
//  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:$V_KOTLIN_COMPILE_TESTING")
}

tasks.test {
  useJUnitPlatform()
  reports {
    junitXml.required.set(false) // no gradle default JUnit XML reports
    html.required.set(false)
  }
  systemProperty("gradle.build.dir", project.layout.buildDirectory)

//  testLogging {
//    showStandardStreams = true
//  }
//  systemProperty("kotest.framework.dump.config", "true")
}

tasks.compileKotlin {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict") // harder null safety checks with Java annotations
    jvmTarget = Versions.V_JVM
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
