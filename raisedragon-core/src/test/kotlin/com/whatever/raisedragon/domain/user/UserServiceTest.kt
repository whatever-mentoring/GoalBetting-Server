package com.whatever.raisedragon.domain.user

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.utils.anyNotFoundException
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@Transactional
class UserServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("기본적인 User를 생성한다.")
    @Test
    fun create1() {
        // given
        val oauthTokenPayload = "Sample oauth token payload"
        val fcmTokenPayload = "Sample fcm token payload"
        val nickname = Nickname("User")

        // when
        val foundUser = userService.create(oauthTokenPayload, fcmTokenPayload, nickname)

        // then
        assertThat(foundUser)
            .isInstanceOfUser()
            .extracting("oauthTokenPayload", "fcmTokenPayload", "nickname")
            .contains(oauthTokenPayload, fcmTokenPayload, nickname)
    }

    @DisplayName("User를 생성할 때 oauthTokenPayload와 fcmTokenPayload를 null일 수 있다.")
    @Test
    fun create2() {
        // given
        val oauthTokenPayload = null
        val fcmTokenPayload = null
        val nickname = Nickname("User")

        // when
        val foundUser = userService.create(oauthTokenPayload, fcmTokenPayload, nickname)

        // then
        assertThat(foundUser)
            .isInstanceOfUser()
            .extracting("oauthTokenPayload", "fcmTokenPayload", "nickname")
            .contains(oauthTokenPayload, fcmTokenPayload, nickname)
    }

    @DisplayName("id를 입력받아 User를 조회한다.")
    @Test
    fun findById1() {
        // given
        val oauthTokenPayload = "Sample oauth token payload"
        val fcmTokenPayload = "Sample fcm token payload"
        val nickname = Nickname("User")
        val userEntity = UserEntity(
            oauthTokenPayload = oauthTokenPayload,
            fcmTokenPayload = fcmTokenPayload,
            nickname = nickname
        )
        userRepository.save(userEntity)

        // when
        val foundUser = userService.findById(userEntity.id)

        // then
        assertThat(foundUser)
            .isInstanceOfUser()
            .extracting("oauthTokenPayload", "fcmTokenPayload", "nickname")
            .contains(oauthTokenPayload, fcmTokenPayload, nickname)
    }

    @DisplayName("User를 조회할 때 id에 해당하는 User를 조회할 수 없는경우 예외가 발생한다.")
    @Test
    fun findById2() {
        // given
        val oauthTokenPayload = "Sample oauth token payload"
        val fcmTokenPayload = "Sample fcm token payload"
        val nickname = Nickname("User")
        val userEntity = UserEntity(
            oauthTokenPayload = oauthTokenPayload,
            fcmTokenPayload = fcmTokenPayload,
            nickname = nickname
        )
        userRepository.save(userEntity)

        // when // then
        assertThatThrownBy { userService.findById(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("id set을 입력받아 set 안에 해당하는 id를 갖는 모든 User를 조회한다.")
    @Test
    fun findAllByIdInIds1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        val userEntity3 = UserEntity(nickname = Nickname("User3"))
        val userEntity4 = UserEntity(nickname = Nickname("User4"))
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3, userEntity4))

        val selectedUserEntityList = listOf(userEntity1, userEntity2, userEntity4)
        val selectedUserIds = selectedUserEntityList.map { user -> user.id }.toSet()
        val selectedUserNickname = selectedUserEntityList.map { user -> user.nickname }.toTypedArray()

        // when
        val foundUserList = userService.findAllByIdInIds(selectedUserIds)

        // then
        assertThat(foundUserList)
            .hasSize(3)
            .allMatch { user -> selectedUserIds.contains(user.id) }
            .extracting("nickname")
            .containsExactlyInAnyOrder(*selectedUserNickname)
    }

    @DisplayName("id set을 입력받아 set 안에 해당하는 id를 갖는 모든 User를 조회하는 중 해당하는 User가 없는 경우 Empty-list를 반환한다.")
    @Test
    fun findAllByIdInIds2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        val userEntity3 = UserEntity(nickname = Nickname("User3"))
        val userEntity4 = UserEntity(nickname = Nickname("User4"))
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3, userEntity4))

        // when
        val foundUserList = userService.findAllByIdInIds(setOf(-1, -2, -3))

        // then
        assertThat(foundUserList)
            .isEmpty()
    }

    @DisplayName("oAuthPayload를 갖는 user를 조회한다.")
    @Test
    fun findByOAuthPayload1() {
        // given
        val oauthTokenPayload = "Sample oauth token payload"
        val fcmTokenPayload = "Sample fcm token payload"
        val nickname = Nickname("User")
        val userEntity = UserEntity(
            oauthTokenPayload = oauthTokenPayload,
            fcmTokenPayload = fcmTokenPayload,
            nickname = nickname
        )
        userRepository.save(userEntity)

        // when
        val foundUser = userService.findByOAuthPayload(oauthTokenPayload)

        // then
        assertThat(foundUser)
            .isInstanceOfUser()
            .extracting("oauthTokenPayload", "fcmTokenPayload", "nickname")
            .contains(oauthTokenPayload, fcmTokenPayload, nickname)
    }

    @DisplayName("oAuthPayload를 갖는 user를 조회할 때 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun findByOAuthPayload2() {
        // given
        val oauthTokenPayload = "Sample oauth token payload"
        val fcmTokenPayload = "Sample fcm token payload"
        val nickname = Nickname("User")
        val userEntity = UserEntity(
            oauthTokenPayload = oauthTokenPayload,
            fcmTokenPayload = fcmTokenPayload,
            nickname = nickname
        )
        userRepository.save(userEntity)

        // when
        val foundUser = userService.findByOAuthPayload("another oauthTokenPayload")

        // then
        assertThat(foundUser)
            .isNull()
    }

    @DisplayName("nickname을 입력받아 해당 닉네임을 사용하고 있는 User가 있으면 true를 반환한다.")
    @Test
    fun isNicknameDuplicated1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        val userEntity3 = UserEntity(nickname = Nickname("User3"))
        val userEntity4 = UserEntity(nickname = Nickname("User4"))
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3, userEntity4))

        val willFindNickname = userEntity1.nickname.value

        // when
        val result = userService.isNicknameDuplicated(willFindNickname)

        // then
        assertThat(result).isTrue()
    }

    @DisplayName("nickname을 입력받아 해당 닉네임을 사용하고 있는 User가 없으면 false를 반환한다.")
    @Test
    fun isNicknameDuplicated2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        val userEntity3 = UserEntity(nickname = Nickname("User3"))
        val userEntity4 = UserEntity(nickname = Nickname("User4"))
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3, userEntity4))

        val willFindNickname = "User5"

        // when
        val result = userService.isNicknameDuplicated(willFindNickname)

        // then
        assertThat(result).isFalse()
    }

    @DisplayName("id와 nickname을 입력받아 요청한 id의 User의 nickname을 변경한다.")
    @Test
    fun updateNickname1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val willChangeNickname = "User2"

        // when
        val foundUser = userService.updateNickname(userEntity.id, willChangeNickname)

        // then
        assertThat(foundUser)
            .isInstanceOfUser()
            .extracting("nickname")
            .isEqualTo(Nickname(willChangeNickname))
    }

    @DisplayName("id와 nickname을 받아 User의 Nickname을 변경하는 중 id에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun updateNickname2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val willChangeNickname = "User2"

        // when // then
        assertThatThrownBy { userService.updateNickname(-1L, willChangeNickname) }
            .anyNotFoundException()
    }

    @DisplayName("soft-delete된 User를 다시 활성화한다.")
    @Test
    fun convertBySoftDeleteToEntity1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)
        val now = LocalDateTime.now()
        userEntity.deletedAt = now
        assertThat(userRepository.findById(userEntity.id).get().deletedAt).isEqualTo(now)

        // when
        userService.convertBySoftDeleteToEntity(userEntity.id)

        // then
        assertThat(userRepository.findById(userEntity.id).get().deletedAt).isNull()
    }

    @DisplayName("soft-delete된 User를 다시 활성화할 때 id에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun convertBySoftDeleteToEntity2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)
        val now = LocalDateTime.now()
        userEntity.deletedAt = now
        assertThat(userRepository.findById(userEntity.id).get().deletedAt).isEqualTo(now)

        // when // then
        assertThatThrownBy { userService.convertBySoftDeleteToEntity(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("id에 해당하는 User를 hard-delete한다.")
    @Test
    fun hardDeleteById1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        // when
        userService.hardDeleteById(userEntity.id)

        // then
        assertThat(userRepository.findByIdOrNull(userEntity.id)).isNull()
    }

    @DisplayName("id에 해당하는 User를 hard-delete할 때 id에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun hardDeleteById2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        // when // then
        assertThatThrownBy { userService.hardDeleteById(-1L) }
            .anyNotFoundException()
    }

    private fun ObjectAssert<*>.isInstanceOfUser() = isInstanceOf(User::class.java)
}