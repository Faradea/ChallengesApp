package com.macgavrina.challengesapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChallengesAllUsecase @Inject constructor(
    private val repository: ChallengesRepository
) {

    fun execute(): Flow<Resource<List<Challenge>>> = repository.getChallengesAll()
}