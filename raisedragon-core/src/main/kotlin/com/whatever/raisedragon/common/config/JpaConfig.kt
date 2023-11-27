package com.whatever.raisedragon.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}