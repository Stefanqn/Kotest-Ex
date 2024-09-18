import io.kotest.assertions.nondeterministic.continually
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.assertions.nondeterministic.until
import io.kotest.assertions.retry
import io.kotest.core.Tag
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.mpp.env
import java.time.Instant
import java.time.Instant.now
import kotlin.test.fail
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class FunSpecFramework : FunSpec() { init {
  test("simple") { 1 shouldBe 1 }
  context("outer") {
    test("test") { }
  }
  test("disabled test").config(
    enabledIf = { env("somevalue") != null },
    // object MyIntegrationTest : Tag()
    tags = setOf(MyIntegrationTest)
    // gradle test -Dkotest.tags="MyIntegrationTest & !OtherTag"
  ) { fail("oops") }


  context("Pythag data driven tests") {
    withData(
      PythagTriple(3, 4, 5),
      PythagTriple(6, 8, 10),
    ) { (a, b, c) -> isPythagTriple(a, b, c) shouldBe true }
  }

  context("Pythag native tests") {
    listOf(
      PythagTriple(9, 12, 15),
      PythagTriple(15, 20, 25),
    ).forEach { (a, b, c) ->
      test("dynamic pythag test name: [$a,$b,$c]") {
        isPythagTriple(a, b, c) shouldBe true
      }
    }
  }

  context("non-deterministic tests") {
    val fast = 1.seconds
    val slow = 10.minutes
    test("eventually") {
      eventually(duration = 1.seconds) {
        now().nano % 10 shouldBe 0
      }
    }
    test("until as guard") {
      until(fast) { now().nano % 10 == 0 } // equivalent to eventually
      println("great timing")
    }
    test("retry up to 10 times") {
      retry(10, timeout = slow) {
        now().nano % 10 shouldBe 0
      }
    }
    test("passes for 3 seconds") {
      continually(3.seconds) {
        now().nano % 9999 shouldNotBe 0
      }
    }
  }
}

  data class PythagTriple(val a: Int, val b: Int, val c: Int)

  fun isPythagTriple(a: Int, b: Int, c: Int): Boolean = a * a + b * b == c * c

  object MyIntegrationTest : Tag()
}
