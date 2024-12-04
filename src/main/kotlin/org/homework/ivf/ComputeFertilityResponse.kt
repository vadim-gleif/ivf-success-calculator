package org.homework.ivf

import kotlinx.serialization.Serializable

@Serializable
data class ComputeFertilityResponse(
    val score: Double,
    val successRate: Double,
)
