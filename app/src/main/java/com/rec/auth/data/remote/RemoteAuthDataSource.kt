package com.rec.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.rec.auth.data.TokenHolder
import com.rec.auth.data.User
import com.rec.core.Api
import com.rec.core.Result

object RemoteAuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/auth/login")
        suspend fun login(@Body user: User): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(
        AuthService::class.java)

    suspend fun login(user: User): Result<TokenHolder> {
        try {
            return Result.Success(TokenHolder("12345"))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}

