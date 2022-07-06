package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AcceptChallengeUsecase @Inject constructor(
    private val challengesRepository: ChallengesRepository
) {

    suspend fun execute(challenge: Challenge) {
        withContext(Dispatchers.IO) {
            challengesRepository.addChallenge(challenge)
        }
    }
}