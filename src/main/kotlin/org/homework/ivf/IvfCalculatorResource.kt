package org.homework.ivf

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlinx.serialization.json.addAll
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray
import org.homework.ivf.calculator.FertilityCalculator

@Path("/computeFertilityMetrics")
class IvfCalculatorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun proxyRequest(request: ComputeFertilityRequest): ComputeFertilityResponse {
        val errors = ArrayList<String>()
        if (request.isReasonForInfertilityKnown && !request.atLeastOneReasonIsProvided() && !request.isReasonForInfertilityUnexplained) {
            errors.add("if the reason for infertility is known, at least one specific reason must be provided.")
        }
        if (!request.isReasonForInfertilityKnown && (request.atLeastOneReasonIsProvided() || request.isReasonForInfertilityUnexplained)) {
            errors.add("if the reason for infertility is unknown, specific reasons cannot be provided.")
        }
        if (request.isReasonForInfertilityUnexplained && request.atLeastOneReasonIsProvided()) {
            errors.add("if the reason for infertility is marked as unexplained, other specific reasons cannot be provided.")
        }

        if (request.age < 20 || request.age > 50) errors.add("age is out of range")
        if (request.weight < 80 || request.weight > 300) errors.add("weight is out of range")
        if (request.attemptedIvfPreviously == null && request.usingOwnEggs) errors.add("attemptedIvfPreviously is required when usingOwnEggs is true.")

        if (request.numberOfLiveBirths < 0) errors.add("numberOfLiveBirths should be greater than 0.")
        if (request.numberOfPriorPregnancies < 0) errors.add("numberOfPriorPregnancies should be greater than 0.")
        if (request.numberOfLiveBirths > request.numberOfPriorPregnancies) errors.add("numberOfLiveBirths can't be greater than numberOfPriorPregnancies.")

        if (errors.isNotEmpty()) throw BadRequestException(
            Response.status(400)
                .entity(buildJsonObject {
                    putJsonArray("errors") { addAll(errors) }
                })
                .build()
        )

        return FertilityCalculator.calculate(request)
    }

    fun ComputeFertilityRequest.atLeastOneReasonIsProvided(): Boolean = tubalFactor ||
            maleFactorInfertility ||
            endometriosis ||
            ovulatoryDisorder ||
            diminishedOvarianReserve ||
            uterineFactor ||
            otherReason

}