package com.macgavrina.challengesapp.data

import com.macgavrina.challengesapp.data.local.ChallengeEntity
import com.macgavrina.challengesapp.data.local.ChallengeLocalStore
import com.macgavrina.challengesapp.data.remote.ChallengeModel
import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.ChallengesRepository
import com.macgavrina.challengesapp.domain.ResultOf
import javax.inject.Inject

class ChallengesRepositoryImpl @Inject constructor(
    private val remoteStore: ChallengeRemoteStore,
    private val localStore: ChallengeLocalStore
): ChallengesRepository {

    override suspend fun getRandomChallenge(): ResultOf<Challenge> {
        val resultOf = handleApiRequest {
            remoteStore.getRandomChallenge()
        }
        return when (resultOf) {
            is ResultOf.Success -> ResultOf.Success(resultOf.data.toDomain())
            is ResultOf.Error -> ResultOf.Error(resultOf.message)
        }
    }

    override suspend fun addChallenge(challenge: Challenge) {
        localStore.addChallenge(challenge.toEntity())
    }

    private fun ChallengeModel.toDomain(): Challenge =
        Challenge(name = this.activity)

    private fun Challenge.toEntity(): ChallengeEntity =
        ChallengeEntity(name = this.name)
}