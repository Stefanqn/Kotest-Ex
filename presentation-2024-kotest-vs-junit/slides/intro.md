## ![Kotest](../img/kotestLogo.png) 

[//]: # (for ![JUnit]&#40;../img/junit.png&#41;<!-- data-background-opacity: 1 .element: style="transform: scale&#40;0.1&#41;" --> Developers)

---
## Agenda
* relate Kotest to JUnit
* Kotest Test Framework
* Kotest Matchers
* Kotest Property-based Testing

---

| ![JUnit](../img/junit.png)<!-- data-background-opacity: 1 .element: style="vertical-align: bottom" class="fragment" data-fragment-index="1" --> | ![Kotest](../img/kotestLogo.png)<!-- -->           |
|-------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------|
| JVM test platform + test framework <!-- .element: class="fragment" data-fragment-index="1" -->                                                  | Kotlin test framework (multi-platform)             |
| assert*-functions or e.g. Hamcrest <!-- .element: class="fragment" data-fragment-index="1" -->                                                  | standalone assertions library (Kotlin infix style) |
| e.g. junit-quickcheck, jqwik       <!-- .element: class="fragment" data-fragment-index="1" -->                                                  | integrated property testing                        |

---

[//]: # (## Test)

<table class="styled-table" style="font-size: 75%;">
  <tr>
    <th></th>
    <th><img src="../img/junit.png" alt="junit" style="vertical-align: bottom"></th>
    <th><img src="../img/kotestLogo.png" alt="kotest" style="padding-bottom: 0.5em"></th>
  </tr>
  <tr style="color: lightgreen">
    <td class="v-mid"> pro </td>
    <td class="v-mid">
      <ul>
        <li>default</li>
        <li>well-know</li>
      </ul>
    </td>
    <td class="v-mid">
      <ul>
        <li>dynamic tests</li>
        <li>concise Kotlin DSL</li>
        <li>native data-driven tests</li>
        <li>integrated support for async-testing/ coroutines</li>
      </ul>
    </td>
  </tr>
  <tr style="color: lightcoral">
    <td class="v-mid"> contra</td>
    <td class="v-mid">
      <ul>
        <li>noisy Java syntax</li>
        <li>complicated parameterized tests</li>
      </ul>
    </td>
    <td class="v-mid">
      <ul>
        <li>Developer learning curve</li>
        <li>some Specs have limited features</li>
        <li>false friends with wrong imports, e.g. beEmpty</li>
      </ul>
    </td>
  </tr>
</table> 
