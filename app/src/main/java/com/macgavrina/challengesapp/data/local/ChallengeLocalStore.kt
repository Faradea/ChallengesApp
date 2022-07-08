package com.macgavrina.challengesapp.data.local

import javax.inject.Inject

class ChallengeLocalStore @Inject constructor(private val challengeDAO: ChallengeDAO) {

    suspend fun addChallenge(challengeEntity: ChallengeEntity) =
        challengeDAO.addChallenge(challengeEntity)

    fun getChallengesAll() = challengeDAO.getChallengesAll()

    suspend fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int) =
        challengeDAO.updateChallengeIsCompleted(isCompleted, id)

    suspend fun checkIfChallengeExist(challengeEntity: ChallengeEntity): Boolean =
        (challengeDAO.checkIfChallengeExist(challengeEntity.id) > 0)
}