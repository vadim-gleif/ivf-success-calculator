package org.homework.ivf

import io.kotest.assertions.json.shouldContainJsonKeyValue
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test

@QuarkusTest
class IvfCalculatorResourceReadmeTests {

    @Test
    fun `Using Own Eggs, Did Not Previously Attempt IVF, Known Infertility Reason -- 62,2 percentage`() {
        val response = Given {
            body(buildJsonString {
                put("usingOwnEggs", true)
                put("attemptedIvfPreviously", false)
                put("isReasonForInfertilityKnown", true)

                put("age", 32)
                put("weight", 150)
                put("heightFeet", 5)
                put("heightInch", 8)
                put("tubalFactor", false)
                put("maleFactorInfertility", false)
                put("endometriosis", true)
                put("ovulatoryDisorder", true)
                put("diminishedOvarianReserve", false)
                put("uterineFactor", false)
                put("otherReason", false)
                put("isReasonForInfertilityUnexplained", false)
                put("numberOfPriorPregnancies", 1)
                put("numberOfLiveBirths", 1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("score", 0.49827679376137024)
        response.shouldContainJsonKeyValue("successRate", 0.6220542859653846)
    }

    @Test
    fun `Using Own Eggs, Did Not Previously Attempt IVF, Unknown Infertility Reason -- 59,8 percentage`() {
        val response = Given {
            body(buildJsonString {
                put("usingOwnEggs", true)
                put("attemptedIvfPreviously", false)
                put("isReasonForInfertilityKnown", false)

                put("age", 32)
                put("weight", 150)
                put("heightFeet", 5)
                put("heightInch", 8)
                put("tubalFactor", false)
                put("maleFactorInfertility", false)
                put("endometriosis", false)
                put("ovulatoryDisorder", false)
                put("diminishedOvarianReserve", false)
                put("uterineFactor", false)
                put("otherReason", false)
                put("isReasonForInfertilityUnexplained", false)
                put("numberOfPriorPregnancies", 1)
                put("numberOfLiveBirths", 1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("score", 0.39859338617378465)
        response.shouldContainJsonKeyValue("successRate", 0.5983496591615785)
    }

    @Test
    fun `Using Own Eggs, Previously Attempted IVF, Known Infertility Reason -- 40,8 percentage`() {
        val response = Given {
            body(buildJsonString {
                put("usingOwnEggs", true)
                put("attemptedIvfPreviously", true)
                put("isReasonForInfertilityKnown", true)

                put("age", 32)
                put("weight", 150)
                put("heightFeet", 5)
                put("heightInch", 8)
                put("tubalFactor", true)
                put("maleFactorInfertility", false)
                put("endometriosis", false)
                put("ovulatoryDisorder", false)
                put("diminishedOvarianReserve", true)
                put("uterineFactor", false)
                put("otherReason", false)
                put("isReasonForInfertilityUnexplained", false)
                put("numberOfPriorPregnancies", 1)
                put("numberOfLiveBirths", 1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(200)
        } Extract {
            body().asString()
        }

        response.shouldContainJsonKeyValue("score", -0.368320961268025)
        response.shouldContainJsonKeyValue("successRate", 0.4089467987369167)
    }

}