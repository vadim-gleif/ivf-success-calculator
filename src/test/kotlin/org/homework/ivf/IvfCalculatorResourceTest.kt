package org.homework.ivf

import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.json.put
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
class IvfCalculatorResourceTest {

    companion object {
        @JvmStatic
        fun allFormulasTestsData() = listOf(
            arrayOf(true, false, true, 0.6025999897464469),
            arrayOf(true, false, false, 0.5983496591615785),
            arrayOf(true, true, true, 0.5560748792227076),
            arrayOf(true, true, false, 0.5383176662593782),

            arrayOf(false, null, true, 0.5427121867619742),
            arrayOf(false, true, true, 0.5427121867619742),
            arrayOf(false, false, true, 0.5427121867619742),

            arrayOf(false, null, false, 0.5579550779950347),
            arrayOf(false, true, false, 0.5579550779950347),
            arrayOf(false, false, false, 0.5579550779950347),
        )

        @JvmStatic
        fun numberOfPriorPregnanciesTestsData() = listOf(
            arrayOf(0, 0.567898929954101),
            arrayOf(1, 0.539548200505518),
            arrayOf(2, 0.5383877924595409),
            arrayOf(3, 0.5383877924595409),
        )

        @JvmStatic
        fun numberOfLiveBirthsTestData() = listOf(
            arrayOf(0, 0.567898929954101),
            arrayOf(1, 0.5579550779950347),
            arrayOf(2, 0.5481198415134682),
            arrayOf(3, 0.5481198415134682),
        )
    }

    @ParameterizedTest
    @MethodSource("allFormulasTestsData")
    fun `any combination of flags (usingOwnEggs, attemptedIvfPreviously, isReasonForInfertilityKnown) -- the right formula applied`(
        usingOwnEggs: Boolean,
        attemptedIvfPreviously: Boolean?,
        isReasonForInfertilityKnown: Boolean,
        expectedSuccessRate: Double
    ) {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("usingOwnEggs", usingOwnEggs)
                put("attemptedIvfPreviously", attemptedIvfPreviously)
                put("isReasonForInfertilityKnown", isReasonForInfertilityKnown)

                if (isReasonForInfertilityKnown) put("isReasonForInfertilityUnexplained", true)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("successRate", expectedSuccessRate)
    }

    @ParameterizedTest
    @MethodSource("numberOfPriorPregnanciesTestsData")
    fun `numberOfPriorPregnancies 0,1,2+ -- all columns are supported `(
        numberOfPriorPregnancies: Int,
        expectedSuccessRate: Double
    ) {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("numberOfPriorPregnancies", numberOfPriorPregnancies)
                put("numberOfLiveBirths", 0)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("successRate", expectedSuccessRate)
    }

    @ParameterizedTest
    @MethodSource("numberOfLiveBirthsTestData")
    fun `numberOfLiveBirths 0,1,2+ -- all columns are supported `(
        numberOfLiveBirths: Int,
        expectedSuccessRate: Double
    ) {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("numberOfLiveBirths", numberOfLiveBirths)
                put("numberOfPriorPregnancies", numberOfLiveBirths)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("successRate", expectedSuccessRate)
    }


}