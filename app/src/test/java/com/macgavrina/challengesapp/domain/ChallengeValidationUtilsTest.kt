package com.macgavrina.challengesapp.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ChallengeValidationUtilsTest {

    @Test
    fun `challenge id is zero returns false`() {
        val result = ChallengeValidationUtils.isChallengeValid(
            Challenge(
                id = 0,
                name = "name",
                isCompleted = false,
            )
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `challenge id is negative returns false`() {
        val result = ChallengeValidationUtils.isChallengeValid(
            Challenge(
                id = -1,
                name = "name",
                isCompleted = false,
            )
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `challenge name is empty returns false`() {
        val result = ChallengeValidationUtils.isChallengeValid(
            Challenge(
                id = 123,
                name = "",
                isCompleted = false,
            )
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `challenge is ok returns true`() {
        val result = ChallengeValidationUtils.isChallengeValid(
            Challenge(
                id = 123,
                name = "name",
                isCompleted = false,
            )
        )
        assertThat(result).isTrue()
    }
}