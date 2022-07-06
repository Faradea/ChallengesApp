package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.flow.Flow

interface ChallengesRepository {

    suspend fun getRandomChallenge(): ResultOf<Challenge>

    suspend fun addChallenge(challenge: Challenge)

    fun getChallengesAll(): Flow<List<Challenge>>

    fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int)
}