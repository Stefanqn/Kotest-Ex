package junit

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

enum class Day {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

class ParameterizedJunit {

    @ParameterizedTest
    @DisplayName("Requesting known province by ID should result in 200 error code")
    @ValueSource(strings = ["AB", "BC", "MB", "NB", "NL", "NS", "NT", "NU", "ON", "PE", "QC", "SK", "YTx"])
    fun checkAllProvincesCanBeFetchedById(provinceId: String) {
        assertTrue(provinceId.length == 2, "province [$provinceId] length not 2")
    }

    @ParameterizedTest
    @DisplayName("Requesting known enum province by ID should result in 200 error code")
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = ["FRIDAY"])
    fun checkAllEnumProvincesCanBeFetchedById(province: Day) {
        assertNotEquals(province, Day.FRIDAY)
    }

    @ParameterizedTest(name = "{index} - Check the {0} province holidays for {1} year can be fetched")
    @MethodSource("provincesAndYearsProvider")
    fun checkMethodProvincesCanBeFetchedById(province: String, year: String) {
        assertTrue(year.startsWith("20"))
    }


    companion object {
        @JvmStatic
        fun provincesAndYearsProvider(): Stream<Arguments> {
            return Stream.of(
                arguments("AB", "2023"),
                arguments("BC", "2022"),
                arguments("XX", "1922")
            )
        }
    }
}

