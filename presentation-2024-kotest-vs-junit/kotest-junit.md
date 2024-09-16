## ![Unit Tests](img/unitTests.webp)<!-- .element: class="front-pic" -->

---

| ![JUnit](img/junit.png)<!-- data-background-opacity: 1 .element: style="vertical-align: bottom" class="fragment" data-fragment-index="1" --> | ![Kotest](img/kotestLogo.png)<!-- -->              |
|----------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------|
| JVM test platform + test framework <!-- .element: class="fragment" data-fragment-index="1" -->                                               | Kotlin test framework (multi-platform)             |
| assert*-functions or e.g. Hamcrest <!-- .element: class="fragment" data-fragment-index="1" -->                                               | standalone assertions library (Kotlin infix style) |
| e.g. junit-quickcheck, jqwik       <!-- .element: class="fragment" data-fragment-index="1" -->                                               | integrated property testing                        |

---
<!-- .element: style="font-size: 75%;" -->
| JUnit                                             | Kotest                                                                                                                        |
|---------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| + default <br/> + well known                      | + dynamic tests <br/> + concise Kotlin DSL <br/> + data-driven tests <br/> + integrated support for coroutine / async testing |
| - noisy <br/> - complicated data-driven tests     | - no jump to test<br/> - Devs learning                                                                                        |

---

## Test

 <table class="styled-table">
  <tr>
    <th>Company</th>
    <th>Contact</th>
    <th>Country</th>
  </tr>
  <tr>
    <td>
    * Alfreds Futterkiste
    </td>
    <td>Maria Anders</td>
    <td>Germany</td>
  </tr>
  <tr>
    <td>Centro comercial Moctezuma</td>
    <td>Francisco Chang</td>
    <td>Mexico</td>
  </tr>
</table> 

---

## Test Framework
* nested [Testing Styles](https://kotest.io/docs/framework/testing-styles.html)
* Data Driven Tests
* [Extensions](https://kotest.io/docs/extensions/extensions.html), e.g.
  - TestContainer
  - Spring
  - JUnit XML Reporter
* Coroutines

[//]: # (* annotation-less test configuration)
[//]: # (* TestContext:  <em>`testCase.description.name`</em>)

---

## Test Framework - Styles

| Test Style	                                           | Inspired By                     | 
|-------------------------------------------------------|---------------------------------|
| Fun Spec                                              | ScalaTest                       |
| Describe Spec  <!-- .element: class="highlight" -->   | Javascript frameworks and RSpec | 
| Should Spec                                           | A Kotest original               |
| String Spec                                           | A Kotest original               |
| Behavior Spec   <!-- .element: class="highlight" -->  | BDD frameworks                  |
| Free Spec   <!-- .element: class="highlight" -->      | ScalaTest                       |
| Word Spec                                             | ScalaTest                       |
| Feature Spec   <!-- .element: class="highlight" -->   | Cucumber                        |
| Expect Spec                                           | A Kotest original               |
| Annotation Spec  <!-- .element: class="highlight" --> | JUnit                           |

<!-- .element: style="font-size: 75%;" -->
---

## Test Framework - Styles

```kotlin [1-2]
class AnnotationSpecExample : AnnotationSpec() {
  @Test
  fun test1() {
    1 shouldBe 1
  }
}
```
```kotlin [1-2,4]
class FreeSpecExampleTest : FreeSpec({
  "top level context" - {
    "a nested context" - {
      "a test" {
      }
    }
  }
})
```
---

## Test Framework - Styles

```kotlin [:1-4]
class BehaviorSpecExample : BehaviorSpec({
  given("a given") {
    `when`("a when must be backticked: keyword in kotlin") {
      then("a then") { 
          
      }
      then("a then with config").config(enabled = false) {
        // test here
      }}}})
```

```kotlin [1-3]
private class DescribeSpecExample : DescribeSpec({
  describe("some thing") {
    it("test name") {
      // test here
    }}})
```

```kotlin [1-3]
class FeatureSpecExample : FeatureSpec({
  feature("a top level feature") {
    scenario("some scenario") {
      1.shouldBeLessThan(4)
    }}})
```



---

## Assertions

* can be used independently
* Matchers
  - combinable
  - modules: JSON, Android, Compiler
  - mostly documented
* soft assertions
* Non-deterministic Testing

---

## Property-based Testing

* vs example-based tests
* generate inputs & validate property
* Seeds: <ins>deterministic</ins> input generation
* Shrinking
* Reflective Generators (`Arbs`)

---
