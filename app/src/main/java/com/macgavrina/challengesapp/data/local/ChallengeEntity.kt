package com.macgavrina.challengesapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class ChallengeEntity (
    @PrimaryKey
    val name: String
)