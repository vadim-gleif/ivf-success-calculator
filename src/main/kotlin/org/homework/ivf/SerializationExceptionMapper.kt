package org.homework.ivf

import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

@Provider
class SerializationExceptionMapper : ExceptionMapper<SerializationException> {
    override fun toResponse(exception: SerializationException): Response {

        return Response.status(Response.Status.BAD_REQUEST)
            .entity(
                buildJsonObject {
                    putJsonArray("errors", { add(exception.message) })
                }
            )
            .type(MediaType.APPLICATION_JSON)
            .build()
    }
}