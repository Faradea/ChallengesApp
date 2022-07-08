package com.macgavrina.challengesapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class ChallengeEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val isCompleted: Boolean,
    val createdAt: Long? = System.currentTimeMillis()
)