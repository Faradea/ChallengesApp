package com.macgavrina.challengesapp.data.remote

import javax.inject.Inject

class ChallengeRemoteStore @Inject constructor(private val api: ChallengesAPI) {

    suspend fun getRandomChallenge() = api.getRandomChallenge()
}