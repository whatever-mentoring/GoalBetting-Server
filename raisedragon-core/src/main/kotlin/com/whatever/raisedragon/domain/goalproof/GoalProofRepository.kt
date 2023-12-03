package com.whatever.raisedragon.domain.goalproof

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalProofRepository : JpaRepository<GoalProofEntity, Long>