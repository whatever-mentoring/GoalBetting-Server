package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class RefreshTokenRepositoryTest : IntegrationTestSupport {

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("payload를 입력받아 RefreshTokenEntity를 조회한다.")
    @Test
    fun findByPayload1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "refresh token payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity = userEntity, payload = payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshTokenEntity = refreshTokenRepository.findByPayload(payload)

        // then
        assertThat(foundRefreshTokenEntity)
            .isInstanceOf(RefreshTokenEntity::class.java)
            .extracting("payload")
            .isEqualTo(payload)

        assertThat(foundRefreshTokenEntity?.userEntity)
            .isNotNull
            .isInstanceOf(UserEntity::class.java)
            .extracting("id")
            .isEqualTo(userEntity.id)
    }

    @DisplayName("payload를 입력받아 RefreshTokenEntity를 조회하는 중 해당하는 RefreshTokenEntity가 없는 경우 null를 반환한다.")
    @Test
    fun findByPayload2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "refresh token payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity = userEntity, payload = payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshTokenEntity = refreshTokenRepository.findByPayload("payload")

        // then
        assertThat(foundRefreshTokenEntity)
            .isNull()
    }

    @DisplayName("UserEntity를 입력받아 RefreshTokenEntity를 조회한다.")
    @Test
    fun findByUserEntity1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val payload = "refresh token payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity = userEntity1, payload = payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshTokenEntity = refreshTokenRepository.findByUserEntity(userEntity1)

        // then
        assertThat(foundRefreshTokenEntity)
            .isInstanceOf(RefreshTokenEntity::class.java)
            .extracting("payload")
            .isEqualTo(payload)

        assertThat(foundRefreshTokenEntity?.userEntity)
            .isNotNull
            .isInstanceOf(UserEntity::class.java)
            .extracting("id")
            .isEqualTo(userEntity1.id)
    }

    @DisplayName("UserEntity를 입력받아 RefreshTokenEntity를 조회할 때 해당하는 RefreshTokenEntity가 없는 경우 null를 반환한다.")
    @Test
    fun findByUserEntity2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val payload = "refresh token payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity = userEntity1, payload = payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshTokenEntity = refreshTokenRepository.findByUserEntity(userEntity2)

        // then
        assertThat(foundRefreshTokenEntity)
            .isNull()
    }
}