package com.whatever.raisedragon.domain.goalgifticon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalGifticonRepository : JpaRepository<GoalGifticonEntity, Long>