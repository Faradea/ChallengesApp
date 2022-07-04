package com.macgavrina.challengesapp.domain

interface ChallengesRepository {

    suspend fun getRandomChallenge(): ResultOf<Challenge>

    suspend fun addChallenge(challenge: Challenge)
}