package com.whatever.raisedragon.domain.betting

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BettingRepository : JpaRepository<BettingEntity, Long>