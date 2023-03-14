package samples

//import io.kotest.core.annotation.Ignored
//import io.kotest.core.annotation.Tags
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.timing.eventually
import io.kotest.core.Tag
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.startWith
import io.kotest.mpp.env
import java.lang.IllegalStateException
import java.time.Instant
import kotlin.time.Duration.Companion.seconds

//@Ignored
//@Tags("LongRunningTest")
class FreeSpecSample : FreeSpec({
  "String.length" - {
    "should return the length of the string" {
      "sammy".length shouldBe 5
      "".length shouldBe 0
    }
    "containers can be nested as deep as you want" - {
      "and so we nest another container" - {
        "yet another container" - {
          "finally a real test" {
            1 + 1 shouldBe 2
          }
        }
      }
    }
  }

  "exceptions" {
    shouldThrow<IllegalStateException> {
      throw IllegalStateException("boom")
    }.message should startWith("boom")
  }

  "ignored test".config(enabledIf = { env("somevalue") != null }) {
    1 shouldBe 2
  }

  "non-deterministic test".config(tags = setOf(LongRunningTest)) { // also retry/until
    eventually(duration = 1.seconds) {
      Instant.now().nano.also {
        it % 10 shouldBe 0
      }
    }.also { println("Res: $it") }
  }

  "test creation" - {
    (1..3).forEach {
      "test $it" { it % 2 shouldNotBe 0 }
    }
  }

  "data/table driven test" - {
    // needs kotest-framework-datatest
    data class PythagTriple(val a: Int, val b: Int, val c: Int)

    fun isPythagTriple(a: Int, b: Int, c: Int): Boolean = a * a + b * b == c * c
    withData( // just a nicer test name generation.
      PythagTriple(3, 4, 5),
      PythagTriple(6, 8, 10),
      PythagTriple(8, 15, 17),
      PythagTriple(7, 24, 26)
    ) { (a, b, c) ->
      isPythagTriple(a, b, c) shouldBe true
    }
  }


  beforeSpec { println("Hello ${it.rootTests().first().name.testName}") } // beforeContainer,beforeTest,..
})

object LongRunningTest : Tag()
