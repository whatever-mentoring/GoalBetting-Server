package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.domain.refreshtoken.RefreshTokenService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RefreshTokenApplicationService(
    private val refreshTokenService: RefreshTokenService
)