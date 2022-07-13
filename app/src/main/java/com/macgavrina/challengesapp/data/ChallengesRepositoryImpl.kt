package com.macgavrina.challengesapp.data

import com.macgavrina.challengesapp.data.local.ChallengeLocalStore
import com.macgavrina.challengesapp.data.remote.ChallengeModel
import com.macgavrina.challengesapp.data.remote.ChallengeRemoteStore
import com.macgavrina.challengesapp.domain.Challenge
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

    override suspend fun getRandomChallenge(): Resource<Challenge> {
       return try {
            val apiResponse = remoteStore.getRandomChallenge()
            if (apiResponse.isSuccessful && apiResponse.body() != null) {
                Resource.Success((apiResponse.body() as ChallengeModel).toDomain())
            } else {
                Resource.Error(message = apiResponse.message())
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.message())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "Couldn't load data")
        }
    }

    override suspend fun addChallenge(challenge: Challenge) {
        localStore.addChallenge(challenge.toEntity())
    }

    override fun getChallengesAll(): Flow<Resource<List<Challenge>>> {
        return localStore.getChallengesAll().map { list ->
            list.map { it.toDomain() }
        }.map {
            Resource.Success(it)
        }
    }

    override suspend fun updateChallengeIsCompleted(isCompleted: Boolean, id: Int) {
        localStore.updateChallengeIsCompleted(isCompleted, id)
    }

    override suspend fun checkIfChallengeAlreadyExist(challenge: Challenge): Boolean =
        localStore.checkIfChallengeExist(challenge.toEntity())
}