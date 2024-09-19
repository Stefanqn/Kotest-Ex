package samples

import io.github.serpro69.kfaker.Faker
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beEmpty
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.property.*
import io.kotest.property.arbitrary.*
import io.kotest.property.resolution.GlobalArbResolver
import java.math.BigDecimal
import java.math.MathContext
import java.net.URI
import java.time.Instant

@OptIn(ExperimentalKotest::class)
class FunSpecPropertyBased : FunSpec() {
  init {
    test("forAll --> boolean result). Err, shrinking, seed") {
      forAll<String, String> { a, b ->
        (a + b).length == a.length - b.length
      }
    }
    test("checkAll --> Unit. Fails on exception") {
      checkAll<String, String>(1_000) { a, b ->
        a + b shouldHaveLength a.length + b.length
      }
    }

    context("generator operations") {
      test("next() returns sample ${Arb.int().next()}") {}
      test("filter discards: ${Arb.int().filter { it % 2 == 0 }.next()}") {}
      test("map ${Arb.int().map { "add 1 ${it + 1}" }.next()}") {}
      val fooBar = Arb.of("foo", "bar")
      val oneToTen = Arb.int(1..10)
      test(
        "flatmap chains Arbs: ${
          fooBar.flatMap { prefix ->
            oneToTen.map { integer ->
              "${prefix}-${integer}"
            }
          }.next()
        }"
      ) {}
      test("bind chains better: ${Arb.bind(fooBar, oneToTen) { a, b -> "$a-$b" }.next()}") {}
      test("merge (random left or right): ${arbitrary { "a" }.merge(arbitrary { "b" }).next()}") {}
    }

    context("assumptions discard") {
      test("fails due too many discards") {
        checkAll<Int, Int> { a, b ->
          assume(a + b > 0) // filters / discards
          a + b shouldBeGreaterThan 0
        }
      }
      test("bump discard %") {
        checkAll<Int, Int>(PropTestConfig(maxDiscardPercentage = 55)) { a, b -> // default 10%
          assume(a + b > 0)
          a + b shouldBeGreaterThan 0
        }
      }
    }

    context("custom generators") {
      // also bind and flatMap
      test("simple Arb.int") {
        forAll(Arb.int(18..150)) { it >= 18 }
      }
      test("Arb.class") {
        val personArb = arbitrary {
          val name = Arb.string(10..12).bind()
          val age = Arb.int(21, 150).bind()
          Person(name, age)
        }
        checkAll(personArb) { p ->
          withClue("Person: $p") { p.age shouldBeGreaterThan 21 }
        }
      }
      test("reflective Arb.class") {
        checkAll<Person> { p ->
          withClue("Person: $p") { p.age shouldBeGreaterThan 21 }
        }
      }
      test("reflective Arb with GlobalArbResolver") {
        data class SomeComplex(val dir: Direction, val instant: Instant, val uri: URI)

        val arbUri = arbitrary { URI.create(
            Arb.string().filter("[a-zA-Z][\\p{Alnum}+.-]*".toRegex()::matches).bind() + "://" + Arb.string()
              .filter("[\\w-.&/]+".toRegex()::matches).bind())
        }

        val partialReflectiveArb = Arb.bind<SomeComplex>(mapOf(URI::class to arbUri))
        GlobalArbResolver.register<SomeComplex>(partialReflectiveArb)

        checkAll<SomeComplex> { t -> withClue("t: $t") { t.toString() shouldNot beEmpty() } }
      }
    }
  }

  class Person(val name: String, val age: Int)
  enum class Direction { NORTH, SOUTH, WEST, EAST }
}
