package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.flow.Flow

interface ChallengesRepository {

    suspend fun getRandomChallenge(): Resource<Challenge>

    suspend fun addChallenge(challenge: Challenge)

    fun getChallengesAll(): Flow<List<Challenge>>

    suspend fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int)

    suspend fun checkIfChallengeAlreadyExist(challenge: Challenge): Boolean
}