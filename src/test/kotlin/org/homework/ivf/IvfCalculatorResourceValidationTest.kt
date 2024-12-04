package org.homework.ivf

import io.kotest.matchers.string.shouldContain
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import kotlinx.serialization.json.put
import org.junit.jupiter.api.Test

@QuarkusTest
class IvfCalculatorResourceValidationTest {

    @Test
    fun `reason is known, but none specific reason is provided -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("isReasonForInfertilityKnown", true)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("if the reason for infertility is known, at least one specific reason must be provided.")
    }

    @Test
    fun `reason is unknown, but a specific reason is provided -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("isReasonForInfertilityKnown", false)
                put("tubalFactor", true)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("if the reason for infertility is unknown, specific reasons cannot be provided.")
    }

    @Test
    fun `reason is unknown, but a specific reason is provided (unexplained) -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("isReasonForInfertilityKnown", false)
                put("isReasonForInfertilityUnexplained", true)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("if the reason for infertility is unknown, specific reasons cannot be provided.")
    }

    @Test
    fun `reason is marked as unexplained along with a specific reason -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("isReasonForInfertilityKnown", true)
                put("isReasonForInfertilityUnexplained", true)
                put("tubalFactor", true)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("if the reason for infertility is marked as unexplained, other specific reasons cannot be provided.")
    }

    @Test
    fun `invalid age -- bad request`() {
        sequenceOf(19, 51).forEach { age ->
            val response = Given {
                body(buildJsonString {
                    populatedWithFalseValues()

                    put("age", age)
                })
                header("Content-Type", "application/json")
            } When {
                post("/computeFertilityMetrics")
            } Then {
                statusCode(400)
            } Extract {
                body().asString()
            }

            response.shouldContain("age is out of range")
        }
    }

    @Test
    fun `invalid weight -- bad request`() {
        sequenceOf(79, 301).forEach { age ->
            val response = Given {
                body(buildJsonString {
                    populatedWithFalseValues()

                    put("weight", age)
                })
                header("Content-Type", "application/json")
            } When {
                post("/computeFertilityMetrics")
            } Then {
                statusCode(400)
            } Extract {
                body().asString()
            }

            response.shouldContain("weight is out of range")
        }
    }

    @Test
    fun `usingOwnEggs is true, but attemptedIvfPreviously is null -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("usingOwnEggs", true)
                put("attemptedIvfPreviously", null)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("attemptedIvfPreviously is required when usingOwnEggs is true.")
    }

    @Test
    fun `numberOfLiveBirths is negative -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("numberOfLiveBirths", -1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("numberOfLiveBirths should be greater than 0.")
    }

    @Test
    fun `numberOfPriorPregnancies is negative -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("numberOfPriorPregnancies", -1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("numberOfPriorPregnancies should be greater than 0.")
    }

    @Test
    fun `numberOfPriorPregnancies less than numberOfLiveBirths -- bad request`() {
        val response = Given {
            body(buildJsonString {
                populatedWithFalseValues()

                put("numberOfLiveBirths", 2)
                put("numberOfPriorPregnancies", 1)
            })
            header("Content-Type", "application/json")
        } When {
            post("/computeFertilityMetrics")
        } Then {
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("numberOfLiveBirths can't be greater than numberOfPriorPregnancies.")
    }

    @Test
    fun `some fields are missed -- bad request`() {
        val response = Given {
            body(buildJsonString {
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
            statusCode(400)
        } Extract {
            body().asString()
        }

        response.shouldContain("Field 'usingOwnEggs' is required")
    }
}