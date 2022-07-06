package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateChallengeIsCompletedUsecase @Inject constructor(
    private val repository: ChallengesRepository
) {
    suspend fun execute(isCompleted: Boolean, id: Int) {
        withContext(Dispatchers.IO) {
            repository.updateChallengeIsCompleted(isCompleted, id)
        }
    }
}