package app

import io.kotest.core.spec.style.FreeSpec
import io.kotest.extensions.spring.testContextManager
import io.kotest.matchers.string.shouldContain
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerKotest(
  @Autowired private val mockMvc: MockMvc
) : FreeSpec({
  "greeting should return default message" {
    testContextManager()
    mockMvc
      .perform(get("/"))
      .andExpect(status().isOk)
      .andExpect(content().string(containsString("Hello, World")))
      .andExpect { it.response.contentAsString shouldContain "Hello, World" }
  }
})

