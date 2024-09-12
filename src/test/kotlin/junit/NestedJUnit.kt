package junit

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@DisplayName("Canada Holidays API - Provinces")
class NestedJUnit {


    // Nested JUnit 5 test
    @Nested
    @DisplayName("Negative checks")
    inner class JunitExampleNestedTest {

        @Test
        @DisplayName("Requesting unknown province by ID should result in 400 error code")
        fun checkUnknownProvinceIsNotReturned() {
            assertTrue(true, "ok")
        }
    }
}