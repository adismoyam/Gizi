package com.tiuho22bangkit.gizi.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ChatbotApiService {
    @POST("/chatbot")
    suspend fun sendMessage(@Body messageRequest: MessageRequest): Response<MessageResponse>

    @GET("/chatbot")
    suspend fun getMessage(): Response<MessageResponse>
}

data class MessageRequest(
    val message: String
)

data class MessageResponse(
    val reply: String
)