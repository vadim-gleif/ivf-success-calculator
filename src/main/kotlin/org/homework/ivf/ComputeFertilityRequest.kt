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

    val tubalFactor: Boolean = false,
    val maleFactorInfertility: Boolean = false,
    val endometriosis: Boolean = false,
    val ovulatoryDisorder: Boolean = false,
    val diminishedOvarianReserve: Boolean = false,
    val uterineFactor: Boolean = false,
    val otherReason: Boolean = false,

    val isReasonForInfertilityUnexplained: Boolean = false,

    val numberOfPriorPregnancies: Int,
    val numberOfLiveBirths: Int,
)