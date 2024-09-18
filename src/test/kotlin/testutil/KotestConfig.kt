package testutil

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.names.TestNameCase
import io.kotest.extensions.htmlreporter.HtmlReporter
import io.kotest.extensions.junitxml.JunitXmlReporter
import io.kotest.extensions.spring.SpringExtension

class KotestConfig: AbstractProjectConfig() {
  override fun extensions(): List<Extension> = listOf(
    SpringExtension, // before/after callbacks, constructor injection, TestContext(Manager)
    JunitXmlReporter(),
    HtmlReporter() // depends on JunitXmlReporter
  )
//  override val failOnEmptyTestSuite = true
}
