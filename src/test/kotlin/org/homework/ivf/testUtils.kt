package org.homework.ivf

import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

inline fun buildJsonString(builderAction: JsonObjectBuilder.() -> Unit): String =
    buildJsonObject(builderAction).toString()

fun JsonObjectBuilder.populatedWithFalseValues() {
    put("usingOwnEggs", false)
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
}