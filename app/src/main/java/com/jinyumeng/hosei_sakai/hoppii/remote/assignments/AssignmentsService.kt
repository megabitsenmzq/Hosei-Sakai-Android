package com.jinyumeng.hosei_sakai.hoppii.remote.assignments

import retrofit2.Response
import retrofit2.http.GET

interface AssignmentsService {
    @GET("direct/assignment/my.json")
    suspend fun getAssignments(): Response<Assignments>
}