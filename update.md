* [power assert](https://kotlinlang.org/docs/whatsnew20.html#experimental-kotlin-power-assert-compiler-plugin)
```build.gradle
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
   id("kotlin-conventions")
   id("kotest-jvm-conventions")
   kotlin("plugin.power-assert") version libs.versions.kotlin
}

kotlin {
   sourceSets {
      val jvmTest by getting {
         dependencies {
            implementation(projects.kotestAssertions.kotestAssertionsShared)
            implementation(projects.kotestFramework.kotestFrameworkEngine)
            implementation(projects.kotestRunner.kotestRunnerJunit5)
         }
      }
   }
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
   functions = listOf("io.kotest.matchers.shouldBe")
}
```

```settings.gradle
":kotest-tests:kotest-tests-power-assert",
```

* power assert
* java  data driven object examples
* [block hound example](https://github.com/kotest/kotest/commit/d78edd2f0f85d32132e0ca8e64abf6b489e2db30)
