package org.homework.ivf.calculator

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.homework.ivf.ComputeFertilityRequest
import org.homework.ivf.ComputeFertilityResponse
import kotlin.math.pow

object FertilityCalculator {
    // Golang's std is better, yes, jdk doesn't have csv support :) https://pkg.go.dev/encoding/csv#example-Reader.ReadAll
    private val csvRaws =
        csvReader().readAll(this.javaClass.classLoader.getResourceAsStream("ivf_success_formulas.csv"))

    private val headers = csvRaws[0]
    private val indexes: Map<String, Int> = headers.withIndex().associateBy({ it.value }, { it.index })
    private fun getIndex(fieldName: String): Int = indexes[fieldName] ?: error("$fieldName is unknown formula field")

    private val formulas = csvRaws
        .asSequence()
        .drop(1)
        .associateBy { row ->
            fun parseAsBoolean(formulaColumn: String): Boolean = row[getIndex(formulaColumn)].toBoolean()

            createFormulaSelectionKey(
                usingOwnEggs = parseAsBoolean("param_using_own_eggs"),
                attemptedIvfPreviously = parseAsBoolean("param_attempted_ivf_previously"),
                isReasonForInfertilityKnown = parseAsBoolean("param_is_reason_for_infertility_known"),
            )
        }

    fun calculate(request: ComputeFertilityRequest): ComputeFertilityResponse {
        val formulaSelectionKey = request.toFormulaSelectionKey()
        val formula = formulas[formulaSelectionKey] ?: error("Can't find formula by $formulaSelectionKey")
        fun parseAsDouble(formulaColumn: String): Double = formula[getIndex(formulaColumn)].toDouble()

        val numberOfPriorPregnanciesString =
            if (request.numberOfPriorPregnancies >= 2) "2+" else request.numberOfPriorPregnancies.toString()
        val numberOfLiveBirthsString =
            if (request.numberOfLiveBirths >= 2) "2+" else request.numberOfLiveBirths.toString()

        val bmi: Double = request.weight / ((request.heightFeet * 12.0 + request.heightInch).pow(2.0)) * 703

        val score = (parseAsDouble("formula_intercept")

                + parseAsDouble("formula_age_linear_coefficient") * request.age
                + parseAsDouble("formula_age_power_coefficient") * request.age.pow(parseAsDouble("formula_age_power_factor"))

                + parseAsDouble("formula_bmi_linear_coefficient") * bmi
                + parseAsDouble("formula_bmi_power_coefficient") * bmi.pow(parseAsDouble("formula_bmi_power_factor"))

                + parseAsDouble("formula_unexplained_infertility_${request.isReasonForInfertilityUnexplained}_value")
                + parseAsDouble("formula_tubal_factor_${request.tubalFactor}_value")
                + parseAsDouble("formula_male_factor_infertility_${request.maleFactorInfertility}_value")
                + parseAsDouble("formula_endometriosis_${request.endometriosis}_value")
                + parseAsDouble("formula_ovulatory_disorder_${request.ovulatoryDisorder}_value")
                + parseAsDouble("formula_diminished_ovarian_reserve_${request.diminishedOvarianReserve}_value")
                + parseAsDouble("formula_uterine_factor_${request.uterineFactor}_value")
                + parseAsDouble("formula_other_reason_${request.otherReason}_value")

                + parseAsDouble("formula_prior_pregnancies_${numberOfPriorPregnanciesString}_value")
                + parseAsDouble("formula_prior_live_births_${numberOfLiveBirthsString}_value"))

        return ComputeFertilityResponse(
            score = score,
            successRate = Math.E.pow(score) / (1 + Math.E.pow(score))
        )
    }
}