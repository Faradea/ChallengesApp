package com.macgavrina.challengesapp.data

import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.ChallengesRepository
import javax.inject.Inject

class ChallengesRepositoryImpl @Inject constructor(
    private val remoteStore: ChallengeRemoteStore
): ChallengesRepository {

    override suspend fun getRandomChallenge(): Challenge {
        return Challenge(name = remoteStore.getRandomChallenge().body()!!.activity)
    }
}