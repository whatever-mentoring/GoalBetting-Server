package com.whatever.raisedragon.domain.betting

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BettingService(
    private val bettingRepository: BettingRepository
)