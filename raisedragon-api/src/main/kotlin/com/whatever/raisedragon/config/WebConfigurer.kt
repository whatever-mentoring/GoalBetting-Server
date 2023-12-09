package com.whatever.raisedragon.config

import com.whatever.raisedragon.security.resolver.UserInfoArgumentResolver
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configurable
class WebConfigurer : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserInfoArgumentResolver())
    }
}