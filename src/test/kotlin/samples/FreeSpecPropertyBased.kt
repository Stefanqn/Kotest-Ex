package samples


import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beEmpty
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.property.*
import io.kotest.property.arbitrary.*
import io.kotest.property.arbitrary.bind
import io.kotest.property.resolution.GlobalArbResolver
import java.math.BigDecimal
import java.math.MathContext
import java.net.URI
import java.time.Instant

class FreeSpecPropertyBased : FreeSpec({
  "String size" - {
    "forall" {
      forAll<String, String> { a, b ->
        (a + b).length == a.length - b.length
      }
    }
    "check all" {
      checkAll<String, String>(1_000) { a, b ->
        a + b shouldHaveLength a.length + b.length
      }
    }
  }
  "gen operations" - {
    "next ${Arb.int().next()}" {}
    "filter (even) ${Arb.int().filter { it % 2 == 0 }.next()}" {}
    "map ${Arb.int().map { "add 1 ${it + 1}" }.next()}" {}
    "flatmap ${
      Arb.of("foo", "bar").flatMap { prefix ->
        Arb.int(1..10).map { integer ->
          "${prefix}-${integer}"
        }
      }.next()
    }" {}
    "merge ${arbitrary { "a" }.merge(arbitrary { "b" }).next()}" {}

    data class Triangle(val a: BigDecimal, val b: BigDecimal, val c: BigDecimal)

    val posBigDecimal = Arb.bigDecimal().filter { it > BigDecimal.ZERO }
    val arbRectengularTriangle = Arb.bind(posBigDecimal, posBigDecimal) { a, b ->
      Triangle(a, b, (a * a + b * b).sqrt(MathContext(10)))
    }
    "bind ${arbRectengularTriangle.next()}" {} // mention vs filter
  }

  "Assumptions" - {
    "basic" {
      checkAll<Int, Int> { a, b ->
        assume(a + b > 0) // don't filter all inputs
        a + b shouldBeGreaterThan 0
      }
    }
    "discard %" {
      checkAll<Int, Int>(PropTestConfig(maxDiscardPercentage = 55)) { a, b -> // default 10%
        assume(a + b > 0) // don't filter all inputs
        a + b shouldBeGreaterThan 0
      }
    }
  }

  "custom generator" - {
    "simple" {
      forAll(Arb.int(18..150)) { a ->
        a >= 18
      }
    }
    class Person(val name: String, val age: Int)

    "class arb" {
      val personArb = arbitrary {
        val name = Arb.string(10..12).bind()
        val age = Arb.int(21, 150).bind()
        Person(name, age)
      }
      checkAll(personArb) { p ->
        withClue("Person: $p") { p.age shouldBeGreaterThan 21 }
      }
    }

    "reflective arb" - {
      "person sample" {
        checkAll<Person>() { p ->
          withClue("Person: $p") { p.age shouldBeGreaterThan 21 }
        }
      }
      "complex" {
        data class SomeComplex(val dir: Direction, val instant: Instant, val uri: URI)
        val arbUri = arbitrary {
          URI.create(
            Arb.string().filter("[a-zA-Z][\\p{Alnum}+.-]*".toRegex()::matches).bind()
              + "://" + Arb.string().filter("[\\w-.&/]+".toRegex()::matches).bind()
          )
        }
        GlobalArbResolver.register<SomeComplex>(Arb.bind<SomeComplex>(mapOf(URI::class to arbUri)))
        checkAll<SomeComplex>() { t ->
          withClue("t: $t") { t.toString() shouldNot beEmpty() }
        }
      }
    }

  }

})

enum class Direction { NORTH, SOUTH, WEST, EAST }
