package com.macgavrina.challengesapp.data.remote

data class ChallengeModel (
    val activity: String,
    val type: String?,
    val participants: Int?,
    val price: Double?,
    val link: String?,
    val key: String?,
)