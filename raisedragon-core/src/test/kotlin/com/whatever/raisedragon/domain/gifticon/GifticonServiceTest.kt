package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull

@Transactional
class GifticonServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var gifticonService: GifticonService

    @Autowired
    private lateinit var gifticonRepositry: GifticonRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("userId와 url를 입력받아 Gifticon을 생성한다.")
    @Test
    fun create1() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = "url"

        // when
        val foundGifticon = gifticonService.create(user.id, url)

        // then
        assertThat(foundGifticon)
            .isInstanceOfGifticon()
            .extracting("userId", "url")
            .contains(user.id, URL(url))
    }

    @DisplayName("userId와 url를 입력받아 Gifticon을 생성할 때 요청한 userId에 해당하는 유저가 없는 경우 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = "url"

        // when // then
        assertThatThrownBy { gifticonService.create(-1L, url) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 id에 해당하는 Gifticon을 조회한다.")
    @Test
    fun findById1() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = URL("url1")
        val gifticon1 = GifticonEntity(userEntity = user, url = url)
        gifticonRepositry.save(gifticon1)

        // when
        val foundGifticon = gifticonService.findById(gifticon1.id)

        // then
        assertThat(foundGifticon)
            .isInstanceOfGifticon()
            .extracting("userId", "url")
            .contains(user.id, url)
    }

    @DisplayName("요청한 id에 해당하는 Gifticon을 조회하는 중 해당하는 Gifticon이 없는 경우 예외가 발생한다.")
    @Test
    fun findById2() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = URL("url1")
        val gifticon1 = GifticonEntity(userEntity = user, url = url)
        gifticonRepositry.save(gifticon1)

        // when // then
        assertThatThrownBy { gifticonService.findById(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 userId를 갖는 Gifticon을 hard-delete한다.")
    @Test
    fun hardDeleteByUserId1() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = URL("url1")
        val gifticon1 = GifticonEntity(userEntity = user, url = url)
        gifticonRepositry.save(gifticon1)

        // when
        gifticonService.hardDeleteByUserId(user.id)

        // then
        assertThat(gifticonRepositry.findByIdOrNull(gifticon1.id))
            .isNull()
    }

    @DisplayName("요청한 userId를 갖는 Gifticon을 hard-delete 하는 중 userId에 해당하는 User가 없으면 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId2() {
        // given
        val user = UserEntity(
            oauthTokenPayload = null,
            fcmTokenPayload = null,
            nickname = Nickname("UserNickname1")
        )
        userRepository.save(user)

        val url = URL("url1")
        val gifticon1 = GifticonEntity(userEntity = user, url = url)
        gifticonRepositry.save(gifticon1)

        // when // then
        assertThatThrownBy { gifticonService.hardDeleteByUserId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    private fun ObjectAssert<*>.isInstanceOfGifticon() = isInstanceOf(Gifticon::class.java)

}