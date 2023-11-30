package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.user.UserEntity

data class Betting(
    val userEntity: UserEntity,
    val goal: Goal,
    val predictionType: PredictionType,
    val result: Result
) {


}