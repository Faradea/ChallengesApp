package com.macgavrina.challengesapp.domain

import javax.inject.Inject

class AcceptChallengeUsecase @Inject constructor(
    private val challengesRepository: ChallengesRepository
) {

    suspend fun execute(challenge: Challenge) {
        challengesRepository.addChallenge(challenge)
    }
}