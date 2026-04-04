package com.example.focusapp.data.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface FocusLockApi {
    @POST("focus-lock/apps")
    suspend fun addBlockedApp(@Body app: BlockedAppDto): ApiEnvelope<BlockedAppDto>

    @GET("focus-lock/apps")
    suspend fun getBlockedApps(): ApiEnvelope<List<BlockedAppDto>>

    @DELETE("focus-lock/apps/{id}")
    suspend fun deleteBlockedApp(@Path("id") id: String): ApiEnvelope<Any?>

    @GET("focus-lock/session-config")
    suspend fun getSessionConfig(): ApiEnvelope<SessionConfigDto?>

    @PUT("focus-lock/session-config")
    suspend fun updateSessionConfig(@Body config: SessionConfigDto): ApiEnvelope<SessionConfigDto>

    @POST("focus-lock/sessions")
    suspend fun startSession(@Body body: StartSessionRequest): ApiEnvelope<FocusSessionDto>

    @PATCH("focus-lock/sessions/{id}/complete")
    suspend fun completeSession(
        @Path("id") sessionId: String,
        @Body body: CompleteSessionRequest
    ): ApiEnvelope<FocusSessionDto>

    @GET("focus-lock/reminders")
    suspend fun getReminders(): ApiEnvelope<List<ReminderDto>>

    @POST("focus-lock/reminders")
    suspend fun createReminder(@Body body: ReminderDto): ApiEnvelope<ReminderDto>
}

