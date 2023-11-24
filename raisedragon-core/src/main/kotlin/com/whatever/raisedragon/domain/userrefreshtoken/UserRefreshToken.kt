package com.whatever.raisedragon.domain.userrefreshtoken

import com.whatever.raisedragon.domain.BaseEntity
import com.whatever.raisedragon.domain.refreshtoken.RefreshToken
import com.whatever.raisedragon.domain.user.User
import jakarta.persistence.*

@Table(name = "user_refresh_token")
@Entity
class UserRefreshToken(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    val refreshToken: RefreshToken,
) : BaseEntity()