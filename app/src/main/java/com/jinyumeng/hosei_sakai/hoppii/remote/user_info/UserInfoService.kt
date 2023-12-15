package com.jinyumeng.hosei_sakai.hoppii.remote.user_info

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserInfoService {
    @GET("direct/user/current.json")
    suspend fun getCurrentUser(): Response<UserInfo>

    @GET("direct/user/{userID}.json")
    suspend fun getUser(
        @Path("userID") userID: String
    ): Response<UserInfo>
}