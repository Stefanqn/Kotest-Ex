package samples


import app.HomeController
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.retry
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.timing.eventually
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.core.Tag
import io.kotest.core.spec.style.FreeSpec
import io.kotest.framework.concurrency.until
import io.kotest.matchers.*
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.maps.shouldContainValues
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.reflection.compose
import io.kotest.matchers.should
import io.kotest.matchers.string.*
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.IllegalStateException
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalKotest::class) // due to until
class FreeSpecAssertions : FreeSpec({
  val name = "sammy"
  "core matchers" - {
    "general" {
      name.length shouldBe 5
      name shouldBe "sammy"
      name shouldContain "my" shouldStartWith "sa"
    }
    "exceptions" {
      shouldThrow<IllegalStateException> {
        throw IllegalStateException("boom")
      }.message should startWith("bo")
    }
    "types" {
      "obj".shouldBeInstanceOf<Any>()
      null.shouldBeNull()
    }
    "Compare" {
      1 shouldBeLessThanOrEqualTo 5
    }
    "Maps" {
      mapOf("one" to 1, "two" to 2, "three" to 3).shouldContainValues(1,3)
    }
    // ...
  }

  "collections matchers " - {
    val c = listOf(1, 5, 7, 9)
    "ex 1" {
      c shouldContainAll listOf(1,7)
    }
    "ex 2" {
      c shouldContainExactlyInAnyOrder listOf(1,7)
    }
  }

  "clues" - {
    val ex = listOf(3, 5)
    "without" {
      ex.shouldBeEmpty()
    }
    "with" {
      withClue("List content: $ex") { ex.isEmpty() shouldBe true }
    }
  }

  "soft assert" - {
    "without" {
      name shouldBe "wrong"
      name shouldHaveLength 42
    }
    "with" {
      assertSoftly {
        name shouldBe "wrong"
        name shouldHaveLength 42
      }
    }
  }
  "non-deterministic tests" - {
    "eventually" { // also retry/until
      eventually(duration = 1.seconds) {
        Instant.now().nano.also {
          it % 10 shouldBe 0
        }
      }.also { println("Res: $it") }
    }
    "until" {
      until(1.seconds) { Instant.now().nano % 10 == 0 }
      println("great timing")
    }
    "retry up to 4 times" {
      retry(4, timeout = 10.minutes) {
        Instant.now().nano % 10 shouldBe 0
      }
    }
  }

  "mokk" - {
    val home = mockk<HomeController>()
    (1..2).forEach {
      "ex ($it)" {
        every { home.greeting() } returns "mock"
        home.greeting() shouldBe "mock"
        verify(exactly = 1) { home.greeting() }
      }
    }
  }

  "matchers" - {
    class Person(
      val name: String,
      val age: Int,
    )
    val john = Person("John", 21)

    val ofDrinkingAge = Matcher<Int> { value ->
      val drinkingAge = 18
      MatcherResult(
        value >= drinkingAge,
        { "$value should be >= $drinkingAge for drinking legally" },
        { "$value should be < $drinkingAge for not drinking legally" }
      )
    }
    "custom" {
      123 shouldNotBe ofDrinkingAge
    }

    "compose class" {
      fun theBarCustomer(name: String) = Matcher.compose(
        be(name) to Person::name,
        ofDrinkingAge to Person::age
      )
      john shouldNotBe theBarCustomer("John")
    }
  }


  "grouping tests" - {
    "should run on Windows".config(tags = setOf(Windows)) {}
    "should run on Linux".config(tags = setOf(Linux)) {}
    "should run on Windows and Linux".config(tags = setOf(Windows, Linux)) {}
  }

  "glitches empty" - {
    val ex = listOf(1, 2)
    "wrong 1" {
      ex should { beEmpty() } // this is wrong!
    }
    "wrong 2" {
      emptyList<Any>() shouldBe beEmpty() // this is wrong!
    }
    "ok 1" {
      ex.shouldBeEmpty()
    }
    "ok 2" {
      ex.isEmpty() shouldBe false
    }
  }
//  afterTest { clearAllMocks() }
}

)

object Linux : Tag()
object Windows : Tag()
