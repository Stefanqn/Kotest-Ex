package app

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
class HomeControllerJunit(
  @Autowired private val mockMvc: MockMvc
) {
  @Test
  fun `greeting should return default message`() {
    mockMvc
      .perform(get("/"))
      .andExpect(status().isOk)
      .andExpect(content().string(containsString("Hello, World")))
  }
}
