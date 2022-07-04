package com.macgavrina.challengesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ChallengeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengesDAO(): ChallengeDAO
}