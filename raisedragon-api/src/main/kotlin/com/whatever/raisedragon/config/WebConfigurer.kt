package com.whatever.raisedragon.config

import com.whatever.raisedragon.security.resolver.UserInfoArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class WebConfigurer : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserInfoArgumentResolver())
    }
}