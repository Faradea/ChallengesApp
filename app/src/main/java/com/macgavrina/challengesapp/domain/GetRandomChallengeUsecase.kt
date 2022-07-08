package com.macgavrina.challengesapp.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomChallengeUsecase @Inject constructor(
    private val challengesRepository: ChallengesRepository
) {

    suspend fun execute(): ResultOf<Challenge> {
        return withContext(Dispatchers.IO) {
            for (i in 0.. 20) {
                val newChallenge = challengesRepository.getRandomChallenge()
                if (newChallenge is ResultOf.Error) {
                    return@withContext newChallenge
                }
                if (!challengesRepository.checkIfChallengeAlreadyExist(
                    (newChallenge as ResultOf.Success).data
                )) {
                    return@withContext newChallenge
                }
            }
            return@withContext ResultOf.Error<Challenge>(message = "Probably there is no new challenges left")
        }
    }
}