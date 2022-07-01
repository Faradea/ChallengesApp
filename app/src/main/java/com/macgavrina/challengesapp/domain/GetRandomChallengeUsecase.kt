package com.macgavrina.challengesapp.domain

import javax.inject.Inject

class GetRandomChallengeUsecase @Inject constructor(
    private val challengesRepository: ChallengesRepository
) {

    suspend fun execute(): ResultOf<Challenge> {
        return challengesRepository.getRandomChallenge()
    }
}