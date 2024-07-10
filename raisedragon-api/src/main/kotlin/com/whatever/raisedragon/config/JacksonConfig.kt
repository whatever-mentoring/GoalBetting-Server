package com.whatever.raisedragon.config

import com.whatever.raisedragon.jackson.GoalBettingObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun goalBettingObjectMapperBuilderCustomizer(): GoalBettingObjectMapperBuilderCustomizer {
        return GoalBettingObjectMapperBuilderCustomizer()
    }
}