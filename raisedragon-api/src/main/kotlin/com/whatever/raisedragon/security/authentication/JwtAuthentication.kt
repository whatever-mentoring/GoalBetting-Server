package com.whatever.raisedragon.security.authentication

import com.whatever.raisedragon.config.SecurityConfig.Companion.DEFAULT_ROLE_NAME
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class JwtAuthentication(private val userInfo: UserInfo) : Authentication {

    private var isAuthenticated = true

    override fun getName(): String = userInfo.nickname

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(DEFAULT_ROLE_NAME))

    override fun getCredentials(): Any = userInfo.id

    override fun getDetails(): Any = userInfo.getDetails()

    override fun getPrincipal(): Any = userInfo

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}