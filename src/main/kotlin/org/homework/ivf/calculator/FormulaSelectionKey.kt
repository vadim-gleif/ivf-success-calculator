package org.homework.ivf.calculator

import org.homework.ivf.ComputeFertilityRequest

data class FormulaSelectionKey(
    val usingOwnEggs: Boolean,
    val attemptedIvfPreviously: Boolean?,
    val isReasonForInfertilityKnown: Boolean
)

fun ComputeFertilityRequest.toFormulaSelectionKey() = createFormulaSelectionKey(
    usingOwnEggs = usingOwnEggs,
    attemptedIvfPreviously = attemptedIvfPreviously,
    isReasonForInfertilityKnown = isReasonForInfertilityKnown
)

fun createFormulaSelectionKey(
    usingOwnEggs: Boolean,
    attemptedIvfPreviously: Boolean?,
    isReasonForInfertilityKnown: Boolean
) = FormulaSelectionKey(
    usingOwnEggs = usingOwnEggs,
    attemptedIvfPreviously = if (!usingOwnEggs) null else attemptedIvfPreviously,
    isReasonForInfertilityKnown = isReasonForInfertilityKnown
)