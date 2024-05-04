package com.macgavrina.challengesapp.domain

object ChallengeValidationUtils {

    fun isChallengeValid(challenge: Challenge): Boolean {
        return challenge.id > 0 && challenge.name.isNotEmpty()
    }
}