import FunSpecAssertions.Gender.FEMALE
import FunSpecAssertions.Gender.MALE
import app.HomeController
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.nondeterministic.*
import io.kotest.assertions.retry
import io.kotest.assertions.throwables.*
import io.kotest.assertions.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.*
import io.kotest.matchers.maps.*
import io.kotest.matchers.nulls.*
import io.kotest.matchers.*
import io.kotest.matchers.comparables.*
import io.kotest.matchers.compilation.shouldCompile
import io.kotest.matchers.compilation.shouldNotCompile
import io.kotest.matchers.compose.all
import io.kotest.matchers.reflection.havingProperty
import io.kotest.matchers.string.*
import io.kotest.matchers.types.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.IllegalStateException
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class FunSpecAssertions : FunSpec({
  val name = "sammy"

  context("core matchers") {
    test("general") {
      name.length shouldBe 5
      name shouldBe "sammy"
      name shouldContain "my" shouldStartWith "sa"
    }
    test("exception") {
      shouldThrow<IllegalStateException> {
        throw IllegalStateException("boom")
      }.message should startWith("bo")
    }

    test("types") {
      "obj".shouldBeInstanceOf<Any>()
      null.shouldBeNull()
    }
    test("Compare") {
      1 shouldBeLessThanOrEqualTo 5
    }
    test("Maps") {
      mapOf("one" to 1, "two" to 2, "three" to 3).shouldContainValues(1, 3)
    }
  }

  test("collection matchers") {
    val c = listOf(1, 5, 7, 9)
    c shouldContainAll listOf(1, 7)
    c shouldContainExactlyInAnyOrder listOf(1, 7)
  }

  context("clues") {
    val ex = listOf(3, 5)
    test("normal") {
      ex.shouldBeEmpty()
    }
    test("with clues") {
      withClue("Content: $ex") { ex.isEmpty() shouldBe true }
    }
  }

  context("soft assert") {
    test("normal") {
      name shouldBe "wrong"
      name shouldHaveLength 42
    }
    test("with oft assert") {
      assertSoftly {
        name shouldBe "wrong"
        name shouldHaveLength 42
      }
    }
  }

  test("mokks") {
    val home = mockk<HomeController>()
    every { home.greeting() } returns "mock"
    home.greeting() shouldBe "mock"
    verify(exactly = 1) { home.greeting() }
  }

  context("custom matcher") {
    data class Person(
      val gender: Gender,
      val age: Int,
    )

    val john = Person(MALE, 21)

    fun ofDrinkingAge(drinkingAge: Int) = Matcher<Int> {
      MatcherResult(
        it >= drinkingAge,
        { "$it should be >= $drinkingAge for drinking legally" },
        { "$it should be < $drinkingAge for not drinking legally" }
      )
    }

    test("use custom matcher") {
      john shouldBe ofDrinkingAge(16)
    }

    test("compound matcher") {
      fun freeOnLadiesNight(drinkingAge: Int) = Matcher.all(
        havingProperty(be(FEMALE) to Person::gender),
        havingProperty(ofDrinkingAge(drinkingAge) to Person::age)
      )
      john shouldNotBe freeOnLadiesNight(18)
    }


    context("false friends with wrong imports") {
//      val ex = listOf(1, 2)
//      test("wrong 1") {
//        ex should { beEmpty() } // this is wrong!
//      }
//      test("wrong 2") {
//        emptyList<Any>() shouldBe beEmpty() // this is wrong!
//      }
    }
  }

  afterEach {}
}) {
  enum class Gender { MALE, FEMALE, THIRD }
}
