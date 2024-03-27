package com.whatever.raisedragon.security

import com.whatever.raisedragon.security.authentication.JwtAuthentication
import com.whatever.raisedragon.security.authentication.UserInfo
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class CustomSecurityContextFactory : WithSecurityContextFactory<WithCustomUser> {
    override fun createSecurityContext(withCustomUser: WithCustomUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val user = JwtAuthentication(
            UserInfo(withCustomUser.id, withCustomUser.nickname)
        )
        context.authentication = user
        return context
    }
}