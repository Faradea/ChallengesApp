package com.macgavrina.challengesapp.data

import android.util.Log
import com.macgavrina.challengesapp.data.local.ChallengeLocalStore
import com.macgavrina.challengesapp.data.remote.ChallengeModel
import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.ChallengeValidationUtils
import com.macgavrina.challengesapp.domain.ChallengesRepository
import com.macgavrina.challengesapp.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

class ChallengesRepositoryImpl @Inject constructor(
    private val remoteStore: ChallengeRemoteStore,
    private val localStore: ChallengeLocalStore
): ChallengesRepository {

    companion object {
        private const val LOG_TAG = "ChallengesRepository"
    }

    override suspend fun getRandomChallenge(): Resource<Challenge> {
       return try {
            val apiResponse = remoteStore.getRandomChallenge()
            if (apiResponse.isSuccessful && apiResponse.body() != null) {
                val challengeDomain = (apiResponse.body() as ChallengeModel).toDomain()
                Resource.Success(challengeDomain)
            } else {
                Resource.Error(message = apiResponse.message())
            }
        } catch (e: HttpException) {
            Log.e(LOG_TAG, e.message())
            Resource.Error(message = e.message())
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.message ?: e.toString())
            Resource.Error(message = e.message ?: "Couldn't load data")
        }
    }

    override suspend fun addChallenge(challenge: Challenge) {
        localStore.addChallenge(challenge.toEntity())
    }

    override fun getChallengesAll(): Flow<List<Challenge>> {
        return localStore.getChallengesAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int) {
        localStore.updateChallengeIsCompleted(isCompleted, id)
    }

    override suspend fun checkIfChallengeAlreadyExist(challenge: Challenge): Boolean =
        localStore.checkIfChallengeExist(challenge.toEntity())
}