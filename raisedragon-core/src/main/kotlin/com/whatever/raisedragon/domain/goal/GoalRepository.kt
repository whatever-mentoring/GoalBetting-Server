package com.whatever.raisedragon.domain.goal

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<GoalEntity, Long>