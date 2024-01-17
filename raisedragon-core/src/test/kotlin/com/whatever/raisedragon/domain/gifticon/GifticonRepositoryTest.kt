package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class GifticonRepositoryTest : IntegrationTestSupport {

    @Autowired
    private lateinit var gifticonRepository: GifticonRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("특정 사용자가 등록한 기프티콘을 모두 조회한다.")
    @Test
    fun findAllByUserEntity() {
        // given
        val user1 = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        val user2 = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname2")
        )
        val (savedUser1, savedUser2) = userRepository.saveAll(listOf(user1, user2))

        val gifticon1 = GifticonEntity(userEntity = savedUser1, url = URL("url1"))
        val gifticon2 = GifticonEntity(userEntity = savedUser1, url = URL("url2"))
        val gifticon3 = GifticonEntity(userEntity = savedUser2, url = URL("url3"))
        gifticonRepository.saveAll(listOf(gifticon1, gifticon2, gifticon3))

        // when
        val gifticonEntityList = gifticonRepository.findAllByUserEntity(user1)

        // then
        assertThat(gifticonEntityList)
            .hasSize(2)
            .extracting("userEntity", "url")
            .containsExactlyInAnyOrder(
                tuple(savedUser1, URL("url1")),
                tuple(savedUser1, URL("url2"))
            )
    }

}