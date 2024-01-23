package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.gifticon.GifticonEntity
import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Transactional
class WinnerServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var winnerService: WinnerService

    @Autowired
    private lateinit var winnerRepository: WinnerRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var gifticonRepository: GifticonRepository

    @DisplayName("goalId, userId, gifticonId를 입력받아 Winner를 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        // when
        val foundWinner = winnerService.create(
            goalId = goalEntity.id,
            userId = userEntity.id,
            gifticonId = gifticonEntity.id
        )

        // then
        assertThat(foundWinner)
            .isInstanceOfWinner()
            .extracting("goalId", "userId", "gifticonId")
            .contains(goalEntity.id, userEntity.id, gifticonEntity.id)
    }

    @DisplayName("goalId, userId, gifticonId를 입력받아 Winner를 생성할 때 goalId에 해당하는 Goal이 없다면 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        // when
        assertThatThrownBy { winnerService.create(-1L, userEntity.id, gifticonEntity.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId, userId, gifticonId를 입력받아 Winner를 생성할 때 userId에 해당하는 User가 없다면 예외가 발생한다.")
    @Test
    fun create3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        // when
        assertThatThrownBy { winnerService.create(goalEntity.id, -1L, gifticonEntity.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId, userId, gifticonId를 입력받아 Winner를 생성할 때 gifticonId를 해당하는 Gifticon이 없다면 예외가 발생한다.")
    @Test
    fun create4() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        // when
        assertThatThrownBy { winnerService.create(goalEntity.id, userEntity.id, -1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId와 userId에 해당하는 Winner를 조회한다.")
    @Test
    fun findByGoalIdAndUserId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when
        val foundWinner = winnerService.findByGoalIdAndUserId(goalEntity.id, userEntity.id)

        // then
        assertThat(foundWinner)
            .isNotNull
            .isInstanceOfWinner()
            .extracting("goalId", "userId", "gifticonId")
            .contains(goalEntity.id, userEntity.id, gifticonEntity.id)
    }

    @DisplayName("goalId와 userId에 해당하는 Winner를 조회하는 중 goalId에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun findByGoalIdAndUserId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when // then
        assertThatThrownBy { winnerService.findByGoalIdAndUserId(-1L, userEntity.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId와 userId에 해당하는 Winner를 조회하는 중 userId에 해당하는 User가 없는 경우 예외가 발생한다.")
    @Test
    fun findByGoalIdAndUserId3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, FAIL)
        goalRepository.save(goalEntity)

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when // then
        assertThatThrownBy { winnerService.findByGoalIdAndUserId(goalEntity.id, -1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId와 userId에 해당하는 Winner를 조회하는 중 조건에 해당하는 Winner가 없는 경우 null를 반환한다.")
    @Test
    fun findByGoalIdAndUserId4() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity1, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when
        val foundWinner = winnerService.findByGoalIdAndUserId(goalEntity2.id, userEntity.id)

        // then
        assertThat(foundWinner).isNull()
    }

    @DisplayName("goalId에 해당하는 Winner의 nickname을 조회한다.")
    @Test
    fun findWinnerNicknameByGoalId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, SUCCESS)
        val goalEntity2 = createGoalEntity(userEntity, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity1, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when
        val foundWinnerNickname = winnerService.findWinnerNicknameByGoalId(goalEntity1.id)

        // then
        assertThat(foundWinnerNickname)
            .isNotNull
            .isEqualTo(Nickname("User1"))
    }

    @DisplayName("goalId에 해당하는 Winner의 nickname을 조회하는 중 goalId에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun findWinnerNicknameByGoalId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, SUCCESS)
        val goalEntity2 = createGoalEntity(userEntity, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity1, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when // then
        assertThatThrownBy { winnerService.findWinnerNicknameByGoalId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId에 해당하는 Winner의 nickname을 조회하는 중 goalId에 해당하는 Winner가 없는 경우 null를 반환한다.")
    @Test
    fun findWinnerNicknameByGoalId3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, SUCCESS)
        val goalEntity2 = createGoalEntity(userEntity, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity = GifticonEntity(userEntity = userEntity, url = URL("URL"))
        gifticonRepository.save(gifticonEntity)

        val winnerEntity = WinnerEntity(goalEntity1, userEntity, gifticonEntity)
        winnerRepository.save(winnerEntity)

        // when
        val foundNickname = winnerService.findWinnerNicknameByGoalId(goalEntity2.id)

        //then
        assertThat(foundNickname).isNull()
    }

    private fun ObjectAssert<*>.isInstanceOfWinner() = isInstanceOf(Winner::class.java)

    private fun createGoalEntity(
        userEntity: UserEntity,
        goalType: GoalType,
        goalResult: GoalResult
    ): GoalEntity {
        val startDateTime = LocalDateTime.now()
        val endDateTime = startDateTime.plusDays(7)
        return GoalEntity(
            userEntity = userEntity,
            goalType = goalType,
            content = Content("sampleContent"),
            goalResult = goalResult,
            startDate = startDateTime,
            endDate = endDateTime
        )
    }
}