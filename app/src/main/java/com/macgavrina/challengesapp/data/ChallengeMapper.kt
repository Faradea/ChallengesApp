package com.macgavrina.challengesapp.data

import com.macgavrina.challengesapp.data.local.ChallengeEntity
import com.macgavrina.challengesapp.data.remote.ChallengeModel
import com.macgavrina.challengesapp.domain.Challenge

fun ChallengeModel.toDomain(): Challenge =
    Challenge(id = this.key, name = this.activity, isCompleted = false)

fun Challenge.toEntity(): ChallengeEntity =
    ChallengeEntity(id = id, name = this.name, isCompleted = this.isCompleted)

fun ChallengeEntity.toDomain(): Challenge =
    Challenge(id = this.id, name = this.name, isCompleted = isCompleted)