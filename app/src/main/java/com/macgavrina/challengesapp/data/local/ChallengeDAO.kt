package com.macgavrina.challengesapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDAO {

    @Insert
    suspend fun addChallenge(challengeEntity: ChallengeEntity)
}