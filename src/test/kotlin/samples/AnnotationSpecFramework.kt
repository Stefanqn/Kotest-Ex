import io.kotest.core.Tag
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.mpp.env
import kotlinx.coroutines.test.runTest
import kotlin.test.fail

class AnnotationSpecFramework : AnnotationSpec() {


  @Test
  fun `test square of a number`() = runTest { // this doesn't work as data driven test!
    forAll(
      row(2, 4),
      row(3, 9),
      row(4, 16),
      row(5, 25)
    ) { input, expected ->
      input * input shouldBe expected
    }
  }
}
