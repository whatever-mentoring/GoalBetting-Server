package com.whatever.raisedragon.domain.refreshtoken

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.utils.anyInternalException
import com.whatever.raisedragon.utils.anyNotFoundException
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class RefreshTokenServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var refreshTokenService: RefreshTokenService

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("userId와 payload를 입력받아 RefreshToken을 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"

        // when
        val refreshToken = refreshTokenService.create(userId = userEntity.id, payload = payload)

        // then
        assertThat(refreshToken)
            .isInstanceOfRefreshToken()
            .extracting("userId", "payload")
            .contains(userEntity.id, payload)
    }

    @DisplayName("userId와 null인 payload를 입력받아 RefreshToken을 생성한다.")
    @Test
    fun create2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = null

        // when
        val refreshToken = refreshTokenService.create(userId = userEntity.id, payload = payload)

        // then
        assertThat(refreshToken)
            .isInstanceOfRefreshToken()
            .extracting("userId")
            .isEqualTo(userEntity.id)
        assertThat(refreshToken.payload)
            .isNull()
    }

    @DisplayName("userId와 payload를 입력받아 RefreshToken을 생성할때 userId에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun create3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"

        // when // then
        assertThatThrownBy { refreshTokenService.create(userId = -1L, payload = payload) }
            .anyNotFoundException()
    }

    @DisplayName("payload를 입력받아 RefreshToken을 조회한다.")
    @Test
    fun findByPayload1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshToken = refreshTokenService.findByPayload(payload)

        // then
        assertThat(foundRefreshToken)
            .isInstanceOfRefreshToken()
            .extracting("userId", "payload")
            .contains(userEntity.id, payload)
    }

    @DisplayName("payload를 입력받아 RefreshToken을 조회할 때 해당하는 RefreshToken이 없는 경우 null를 반환한다.")
    @Test
    fun findByPayload2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshToken = refreshTokenService.findByPayload("Another payload")

        // then
        assertThat(foundRefreshToken)
            .isNull()
    }

    @DisplayName("userId를 입력받아 RefreshTokenEntity를 조회한다.")
    @Test
    fun findByUserId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshToken = refreshTokenService.findByUserId(userEntity.id)

        // then
        assertThat(foundRefreshToken)
            .isInstanceOfRefreshToken()
            .extracting("userId", "payload")
            .contains(userEntity.id, payload)
    }

    @DisplayName("userId를 입력받아 RefreshTokenEntity를 조회할 때 해당하는 RefreshToken이 없는 경우 null를 반환한다.")
    @Test
    fun findByUserId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val payload = "Sample user payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity1, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshToken = refreshTokenService.findByUserId(userEntity2.id)

        // then
        assertThat(foundRefreshToken)
            .isNull()
    }

    @DisplayName("userId를 입력받아 RefreshTokenEntity를 조회할 때 userId에 해당하는 User가 없으면 예외가 발생한다.")
    @Test
    fun findByUserId3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when // then
        assertThatThrownBy { refreshTokenService.findByUserId(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("userId,payload를 입력받아 userId에 해당하는 RefreshToken의 payload를 수정한다.")
    @Test
    fun updatePayloadByUserId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val toBePayload = "Modified payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        val foundRefreshToken = refreshTokenService.updatePayloadByUserId(userId = userEntity.id, payload = toBePayload)

        // then
        assertThat(foundRefreshToken)
            .isInstanceOfRefreshToken()
            .extracting("userId", "payload")
            .contains(userEntity.id, toBePayload)
    }

    @DisplayName("userId, payload를 입력받아 userId에 해당하는 RefreshToken의 payload를 수정할 때 userId에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun updatePayloadByUserId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val payload = "Sample user payload"
        val toBePayload = "Modified payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when // then
        assertThatThrownBy {
            refreshTokenService.updatePayloadByUserId(userId = -1L, payload = toBePayload)
        }.anyNotFoundException()
    }

    @DisplayName("userId, payload를 입력받아 userId에 해당하는 RefreshToken의 payload를 수정할 때 userId에 해당하는 RefreshToken이 없는 경우 예외가 발생한다.")
    @Test
    fun updatePayloadByUserId3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val payload = "Sample user payload"
        val toBePayload = "Modified payload"
        val refreshTokenEntity = RefreshTokenEntity(userEntity1, payload)
        refreshTokenRepository.save(refreshTokenEntity)

        // when // then
        assertThatThrownBy {
            refreshTokenService.updatePayloadByUserId(userId = userEntity2.id, payload = toBePayload)
        }.anyNotFoundException()
    }

    @DisplayName("userId를 입력받아 userId에 해당하는 RefreshToken을 hard-delete한다.")
    @Test
    fun hardDeleteByUserId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val refreshTokenEntity = RefreshTokenEntity(userEntity, "Sample payload")
        refreshTokenRepository.save(refreshTokenEntity)

        // when
        refreshTokenService.hardDeleteByUserId(userEntity.id)

        // then
        assertThat(refreshTokenRepository.findByUserEntity(userEntity)).isNull()
    }

    @DisplayName("userId를 입력받아 userId에 해당하는 RefreshToken을 hard-delete할 때 userId에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val refreshTokenEntity = RefreshTokenEntity(userEntity, "Sample payload")
        refreshTokenRepository.save(refreshTokenEntity)

        // when // then
        assertThatThrownBy {
            refreshTokenService.hardDeleteByUserId(-1L)
        }.anyNotFoundException()
    }

    @DisplayName("userId를 입력받아 userId에 해당하는 RefreshToken을 hard-delete할 때 userId에 해당하는 RefreshToken이 없는 경우 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val refreshTokenEntity = RefreshTokenEntity(userEntity1, "Sample payload")
        refreshTokenRepository.save(refreshTokenEntity)

        // when // then
        assertThatThrownBy {
            refreshTokenService.hardDeleteByUserId(userEntity2.id)
        }.anyInternalException()
    }

    private fun ObjectAssert<*>.isInstanceOfRefreshToken() = isInstanceOf(RefreshToken::class.java)
}