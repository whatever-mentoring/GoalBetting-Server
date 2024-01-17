package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.IntegrationTestSupport
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class UserRepositoryTest : IntegrationTestSupport {

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("특정 OAuthTokenPayload를 갖는 UserEntity를 조회한다.")
    @Test
    fun findByOauthTokenPayload() {
        // given
        val oAuthTokenPayload = "testTokenPayload"
        val userEntity1 = UserEntity(nickname = Nickname("User1"), oauthTokenPayload = oAuthTokenPayload)
        val userEntity2 = UserEntity(nickname = Nickname("User2"), oauthTokenPayload = "sampleTokenPlayoad")
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        // when
        val foundUserEntity = userRepository.findByOauthTokenPayload(oAuthTokenPayload)

        // then
        assertThat(foundUserEntity).isEqualTo(userEntity1)
    }

    @DisplayName("특정 닉네임을 갖고 있는 UserEntity가 있는지 조회한다.")
    @Test
    fun existsByNickname() {
        // given
        val nickname = Nickname("User1")
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        // when
        val isExist = userRepository.existsByNickname(nickname)

        // then
        assertThat(isExist).isTrue()
    }

}