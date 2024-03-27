package com.whatever.raisedragon.domain.goalgifticon

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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Transactional
class GoalGifticonServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var goalGifticonService: GoalGifticonService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalGifticonRepository: GoalGifticonRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var gifticonRepository: GifticonRepository

    @DisplayName("goalId, gifticonId, userId를 입력받아 GoalGifticon을 생성한다.")
    @Test
    fun create1() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goal = createGoalEntity(user, BILLING, SUCCESS)
        goalRepository.save(goal)

        val gifticon = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.save(gifticon)

        // when
        val foundGoalGifticon = goalGifticonService.create(goalId = goal.id, gifticonId = gifticon.id)

        // then
        assertThat(foundGoalGifticon)
            .isInstanceOfGoalGifticon()
            .extracting("goalId", "gifticonId")
            .contains(goal.id, gifticon.id)
    }

    @DisplayName("goalId, gifticonId를 입력받아 GoalGifticon을 생성하는 중 요청한 goalId에 해당하는 Goal이 없으면 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goal = createGoalEntity(user, BILLING, SUCCESS)
        goalRepository.save(goal)

        val gifticon = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.save(gifticon)

        // when // then
        assertThatThrownBy { goalGifticonService.create(goalId = -1L, gifticonId = gifticon.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId, gifticonId를 입력받아 GoalGifticon을 생성하는 중 요청한 gifticonId 해당하는 Gifticon 없으면 예외가 발생한다.")
    @Test
    fun create3() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goal = createGoalEntity(user, BILLING, SUCCESS)
        goalRepository.save(goal)

        val gifticon = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.save(gifticon)

        // when // then
        assertThatThrownBy { goalGifticonService.create(goalId = goal.id, gifticonId = -1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId에 해당하는 GoalGifticon을 조회한다.")
    @Test
    fun findByGoalId1() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goalEntity1 = createGoalEntity(user, BILLING, SUCCESS)
        val goalEntity2 = createGoalEntity(user, FREE, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticon1 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon2 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon3 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon4 = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.saveAll(listOf(gifticon1, gifticon2, gifticon3, gifticon4))

        val goalGifticonEntity1 = GoalGifticonEntity(goalEntity = goalEntity1, gifticonEntity = gifticon1)
        val goalGifticonEntity2 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon2)
        val goalGifticonEntity3 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon3)
        val goalGifticonEntity4 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon4)
        goalGifticonRepository.saveAll(
            listOf(
                goalGifticonEntity1,
                goalGifticonEntity2,
                goalGifticonEntity3,
                goalGifticonEntity4
            )
        )
        // when
        val foundGoalGifticon = goalGifticonService.findByGoalId(goalEntity1.id)

        // then
        assertThat(foundGoalGifticon)
            .isInstanceOfGoalGifticon()
            .extracting("goalId", "gifticonId")
            .contains(goalEntity1.id, gifticon1.id)
    }

    @DisplayName("goalId에 해당하는 GoalGifticon이 없으면 null을 반환한다.")
    @Test
    fun findByGoalId2() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goalEntity1 = createGoalEntity(user, BILLING, SUCCESS)
        val goalEntity2 = createGoalEntity(user, FREE, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticon1 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon2 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon3 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon4 = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.saveAll(listOf(gifticon1, gifticon2, gifticon3, gifticon4))

        val goalGifticonEntity1 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon1)
        val goalGifticonEntity2 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon2)
        val goalGifticonEntity3 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon3)
        val goalGifticonEntity4 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon4)
        goalGifticonRepository.saveAll(
            listOf(
                goalGifticonEntity1,
                goalGifticonEntity2,
                goalGifticonEntity3,
                goalGifticonEntity4
            )
        )
        // when
        val foundGoalGifticon = goalGifticonService.findByGoalId(goalEntity1.id)

        // then
        assertThat(foundGoalGifticon).isNull()
    }

    @DisplayName("goalId에 해당하는 GoalGifticon을 조회하는 중 goalId에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun findByGoalId3() {
        // given
        val user = UserEntity(nickname = Nickname("user"))
        userRepository.save(user)

        val goalEntity1 = createGoalEntity(user, BILLING, SUCCESS)
        val goalEntity2 = createGoalEntity(user, FREE, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticon1 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon2 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon3 = GifticonEntity(userEntity = user, url = URL("url"))
        val gifticon4 = GifticonEntity(userEntity = user, url = URL("url"))
        gifticonRepository.saveAll(listOf(gifticon1, gifticon2, gifticon3, gifticon4))

        val goalGifticonEntity1 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon1)
        val goalGifticonEntity2 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon2)
        val goalGifticonEntity3 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon3)
        val goalGifticonEntity4 = GoalGifticonEntity(goalEntity = goalEntity2, gifticonEntity = gifticon4)
        goalGifticonRepository.saveAll(
            listOf(
                goalGifticonEntity1,
                goalGifticonEntity2,
                goalGifticonEntity3,
                goalGifticonEntity4
            )
        )
        // when // then
        assertThatThrownBy { goalGifticonService.findByGoalId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    private fun ObjectAssert<*>.isInstanceOfGoalGifticon() = isInstanceOf(GoalGifticon::class.java)

    private fun createGoalEntity(
        userEntity: UserEntity,
        goalType: GoalType,
        goalResult: GoalResult,
        startDateTime: LocalDateTime = LocalDateTime.now(),
        endDateTime: LocalDateTime = startDateTime.plusDays(7)
    ): GoalEntity {
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