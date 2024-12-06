package com.tiuho22bangkit.gizi.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ChatbotApiService {
    @POST("/chatbot/")
    suspend fun sendMessage(@Body chatRequest: ChatRequest): Response<ChatResponse>
}