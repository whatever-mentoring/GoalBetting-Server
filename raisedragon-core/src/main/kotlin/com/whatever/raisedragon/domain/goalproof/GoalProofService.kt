package com.whatever.raisedragon.domain.goalproof

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GoalProofService(
    private val goalProofRepository: GoalProofRepository
)