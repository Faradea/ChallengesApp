package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomChallengeUsecase @Inject constructor(
    private val challengesRepository: ChallengesRepository
) {

    suspend fun execute(): ResultOf<Challenge> {
        return withContext(Dispatchers.IO) {
            challengesRepository.getRandomChallenge()
        }
    }
}