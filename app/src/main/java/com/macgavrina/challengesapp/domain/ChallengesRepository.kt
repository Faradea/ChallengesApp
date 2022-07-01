package com.macgavrina.challengesapp.domain

import retrofit2.Response

interface ChallengesRepository {

    suspend fun getRandomChallenge(): Challenge
}