package com.macgavrina.challengesapp.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ChallengesAPI {
    @GET("activity")
    suspend fun getRandomChallenge(): Response<ChallengeModel>
}