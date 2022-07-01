package com.macgavrina.challengesapp.domain

interface ChallengesRepository {

    suspend fun getRandomChallenge(): ResultOf<Challenge>
}