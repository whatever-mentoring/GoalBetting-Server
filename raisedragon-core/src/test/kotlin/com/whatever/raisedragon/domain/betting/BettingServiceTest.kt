package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ListAssert
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@Transactional
class BettingServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var bettingService: BettingService

    @Autowired
    private lateinit var bettingRepository: BettingRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @DisplayName("userId, goalId, bettingPredictionType을 입력받아 Betting을 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        // when
        val betting = bettingService.create(
            userId = userEntity.id,
            goalId = goalEntity.id,
            bettingPredictionType = BettingPredictionType.FAIL
        )

        // then
        assertThat(betting)
            .isInstanceOfBetting()
            .extractingBettingProperties()
            .containsExactly(betting.userId, betting.goalId, betting.bettingPredictionType, betting.bettingResult)
    }

    @DisplayName("Betting을 생성하는 중 요청한 userId에 해당하는 유저가 없는경우 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy {
            bettingService.create(
                userId = -1L,
                goalId = goalEntity.id,
                bettingPredictionType = BettingPredictionType.FAIL
            )
        }.isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("Betting을 생성하는 중 요청한 goalId에 해당하는 Goal이 없는경우 예외가 발생한다.")
    @Test
    fun create3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy {
            bettingService.create(
                userId = userEntity.id,
                goalId = -1L,
                bettingPredictionType = BettingPredictionType.FAIL
            )
        }.isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId와 goalId를 갖는 Betting이 있는 경우 조회한다.")
    @Test
    fun loadUserAndGoal1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        val foundBetting = bettingService.loadUserAndGoal(userEntity.id, goalEntity.id)

        // then
        assertThat(foundBetting)
            .isInstanceOfBetting()
            .extractingBettingProperties()
            .contains(userEntity.id, goalEntity.id, BettingPredictionType.SUCCESS, BettingResult.PROCEEDING)
    }

    @DisplayName("userId와 goalId를 갖는 Betting이 없는 경우 null를 반환한다.")
    @Test
    fun loadUserAndGoal2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity1,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        val foundBetting = bettingService.loadUserAndGoal(userEntity1.id, goalEntity.id)

        // then
        assertThat(foundBetting).isNull()
    }

    @DisplayName("userId와 goalId를 갖는 Betting을 조회할 때 userId에 해당하는 유저가 없는 경우 예외가 발생한다.")
    @Test
    fun loadUserAndGoal3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity1,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingService.loadUserAndGoal(-1L, goalEntity.id) }
            .isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId와 goalId를 갖는 Betting을 조회할 때 goalId 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun loadUserAndGoal4() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity1,
            BILLING,
            PROCEEDING,
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingService.loadUserAndGoal(userEntity2.id, -1L) }
            .isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("goalId가 같은 Betting을 모두 조회한다.")
    @Test
    fun findAllByGoalId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = SUCCESS,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val foundBettingList = bettingService.findAllByGoalId(goalEntity1.id)

        // then
        assertThat(foundBettingList).hasSize(2)
            .extractingBettingProperties()
            .containsExactlyInAnyOrder(
                tuple(
                    bettingEntity1.userEntity.id,
                    bettingEntity1.goalEntity.id,
                    BettingPredictionType.FAIL,
                    BettingResult.PROCEEDING
                ),
                tuple(
                    bettingEntity2.userEntity.id,
                    bettingEntity2.goalEntity.id,
                    BettingPredictionType.SUCCESS,
                    BettingResult.NO_GIFTICON
                ),
            )
    }

    @DisplayName("goalId가 같은 Betting을 모두 조회하는 중 goalId에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun findAllByGoalId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = SUCCESS,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when // then
        assertThatThrownBy { bettingService.findAllByGoalId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("id에 해당하는 Betting이 있으면 조회한다.")
    @Test
    fun findByIdOrNull1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val foundBetting = bettingService.findByIdOrNull(bettingEntity1.id)

        // then
        assertThat(foundBetting)
            .isNotNull
            .isInstanceOfBetting()
            .extractingBettingProperties()
            .containsExactly(userEntity.id, goalEntity.id, BettingPredictionType.FAIL, BettingResult.PROCEEDING)
    }

    @DisplayName("id에 해당하는 Betting이 없으면 null을 반환한다.")
    @Test
    fun findByIdOrNull2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2))

        // when
        val foundBetting = bettingService.findByIdOrNull(-1L)

        // then
        assertThat(foundBetting)
            .isNull()
    }

    @DisplayName("요청한 userId에 해당하는 Betting을 모두 조회한다.")
    @Test
    fun findAllByUserId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val foundBettingList = bettingService.findAllByUserId(userEntity1.id)

        // then
        assertThat(foundBettingList).hasSize(2)
            .extractingBettingProperties()
            .containsExactlyInAnyOrder(
                tuple(userEntity1.id, goalEntity.id, BettingPredictionType.FAIL, BettingResult.PROCEEDING),
                tuple(userEntity1.id, goalEntity.id, BettingPredictionType.SUCCESS, BettingResult.NO_GIFTICON),
            )
    }

    @DisplayName("요청한 userId에 해당하는 Betting을 모두 조회하는 중 userId에 해당하는 사용자가 없으면 예외가 발생한다.")
    @Test
    fun findAllByUserId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusDays(7)
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when // then
        assertThatThrownBy { bettingService.findAllByUserId(-1L) }
            .isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 userId에 해당하며 Betting의 endDate가 현재보다 큰 경우의 Betting이 있으면 true를 반환한다.")
    @Test
    fun existsBettingParticipantUser1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val startDateTime = LocalDateTime.now()
        val endDateTime = startDateTime.plusDays(7L)
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime.minusSeconds(1L),
            endDateTime = startDateTime,
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val result = bettingService.existsBettingParticipantUser(userEntity1.id)

        // then
        assertThat(result).isTrue()
    }

    @DisplayName("요청한 userId에 해당하며 Betting의 endDate가 현재보다 큰 경우의 Betting이 없으면 false를 반환한다.")
    @Test
    fun existsBettingParticipantUser2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val startDateTime = LocalDateTime.now()
        val endDateTime = startDateTime.plusDays(7L)
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime.minusSeconds(1L),
            endDateTime = startDateTime,
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val result = bettingService.existsBettingParticipantUser(userEntity1.id)

        // then
        assertThat(result).isFalse()
    }

    @DisplayName("요청한 userId에 해당하며 Betting의 endDate가 현재보다 큰 경우의 Betting을 조회 시 userId에 해당하는 유저가 없으면 예외가 발생한다.")
    @Test
    fun existsBettingParticipantUser3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val startDateTime = LocalDateTime.now()
        val endDateTime = startDateTime.plusDays(7L)
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime.minusSeconds(1L),
            endDateTime = startDateTime,
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity1,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when // then
        assertThatThrownBy { bettingService.existsBettingParticipantUser(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 id에 해당하는 Betting의 BettingPredictionType을 변경한다.")
    @Test
    fun updatePredictionType1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        val foundBetting = bettingService.updatePredictionType(bettingEntity.id, BettingPredictionType.SUCCESS)

        // then
        assertThat(foundBetting.bettingPredictionType)
            .isEqualTo(BettingPredictionType.SUCCESS)
    }

    @DisplayName("요청한 id에 해당하는 Betting의 Predictiontype을 변경하는 중 해당하는 id의 Betting이 없는 경우 예외가 발생한다.")
    @Test
    fun updatePredictionType2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingService.updatePredictionType(-1L, BettingPredictionType.SUCCESS) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 id에 해당하는 Betting의 BettingResult를 변경한다.")
    @Test
    fun updateResult1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        val foundBetting = bettingService.updateResult(bettingEntity.id, BettingResult.NO_GIFTICON)

        // then
        assertThat(foundBetting.bettingResult).isEqualTo(BettingResult.NO_GIFTICON)
    }

    @DisplayName("요청한 id에 해당하는 Betting의 BettingResult를 변경하는 중 해당하는 Betting이 없는 경우 예외가 발생한다.")
    @Test
    fun updateResult2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingService.updateResult(-1L, BettingResult.NO_GIFTICON) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("ids 안에 포함된 id의 베팅의 Result를 변경한다.")
    @Test
    fun bulkModifyingByResultWhereIdInIds() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val updatedBettingCount = bettingService.bulkModifyingByResultWhereIdInIds(
            bettingResult = BettingResult.GET_GIFTICON,
            bettingIds = listOf(bettingEntity1, bettingEntity2, bettingEntity3).map { it.id }.toSet()
        )

        // then
        assertThat(updatedBettingCount)
            .isEqualTo(3)
    }

    @DisplayName("bettingId에 해당하는 Betting을 hard-delete 한다.")
    @Test
    fun hardDelete1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        bettingService.hardDelete(bettingEntity1.id)

        // then
        assertThat(bettingRepository.findByIdOrNull(bettingEntity1.id))
            .isNull()
    }

    @DisplayName("bettingId에 해당하는 Betting을 hard-delete 하는 중 bettingId 해당하는 Betting이 없는 경우 예외가 발생한다.")
    @Test
    fun hardDelete2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when // then
        assertThatThrownBy { bettingService.hardDelete(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId에 해당하는 Betting을 hard-delete한다.")
    @Test
    fun hardDeleteByUserId1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        bettingService.hardDeleteByUserId(userEntity.id)

        // then
        assertThat(
            bettingRepository.findAllById(
                setOf(
                    bettingEntity1.id,
                    bettingEntity2.id,
                    bettingEntity3.id,
                    bettingEntity4.id
                )
            )
        )
            .isEmpty()
    }

    @DisplayName("userId에 해당하는 Betting을 hard-delete하는 중 해당하는 userId가 없는 경우 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            goalResult = PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.FAIL,
            bettingResult = BettingResult.PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when // then
        assertThatThrownBy { bettingService.hardDeleteByUserId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    private fun ObjectAssert<*>.isInstanceOfBetting() = isInstanceOf(Betting::class.java)

    private fun ObjectAssert<*>.extractingBettingProperties() = extracting(
        "userId",
        "goalId",
        "bettingPredictionType",
        "bettingResult"
    )

    private fun ListAssert<*>.extractingBettingProperties() = extracting(
        "userId",
        "goalId",
        "bettingPredictionType",
        "bettingResult"
    )

    private fun createGoalEntity(
        userEntity: UserEntity,
        goalType: GoalType,
        goalResult: GoalResult,
        startDateTime: LocalDateTime = LocalDateTime.now(),
        endDateTime: LocalDateTime = startDateTime.plusDays(7L)
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