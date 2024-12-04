package org.homework.ivf

import kotlinx.serialization.Serializable

@Serializable
data class ComputeFertilityRequest(
    val usingOwnEggs: Boolean,
    val attemptedIvfPreviously: Boolean?,
    val isReasonForInfertilityKnown: Boolean,

    val age: Double,
    val weight: Double,
    val heightFeet: Int,
    val heightInch: Int,

    val tubalFactor: Boolean,
    val maleFactorInfertility: Boolean,
    val endometriosis: Boolean,
    val ovulatoryDisorder: Boolean,
    val diminishedOvarianReserve: Boolean,
    val uterineFactor: Boolean,
    val otherReason: Boolean,

    val isReasonForInfertilityUnexplained: Boolean,

    val numberOfPriorPregnancies: Int,
    val numberOfLiveBirths: Int,
)