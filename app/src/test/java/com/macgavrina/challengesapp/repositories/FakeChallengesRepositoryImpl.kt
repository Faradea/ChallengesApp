package com.macgavrina.challengesapp.repositories

import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.ChallengesRepository
import com.macgavrina.challengesapp.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeChallengesRepositoryImpl: ChallengesRepository {

    private val savedChallenges = mutableListOf<Challenge>()
    private val observableSavedChallenges = MutableStateFlow<List<Challenge>>(emptyList())

    var shouldReturnNetworkError = false

    override suspend fun getRandomChallenge(): Resource<Challenge> {
        if (shouldReturnNetworkError) {
            return Resource.Error("Some error")
        } else {
            return Resource.Success(
                Challenge(
                    id = 1,
                    name = "Random challenge from API",
                    isCompleted = false
                )
            )
        }
    }

    override suspend fun addChallenge(challenge: Challenge) {
        savedChallenges.add(challenge)
        refreshFlow()
    }

    override fun getChallengesAll(): Flow<List<Challenge>> {
        return observableSavedChallenges
    }

    override suspend fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int) {
        savedChallenges.map {
            if (it.id == id) {
                it.copy(isCompleted = isCompleted)
            } else it
        }
    }

    override suspend fun checkIfChallengeAlreadyExist(challenge: Challenge): Boolean {
        return savedChallenges.find { it.id == challenge.id } != null
    }

    private suspend fun refreshFlow() {
        observableSavedChallenges.emit(savedChallenges)
    }
}