package com.whatever.raisedragon.domain.betting

import com.whatever.raisedragon.RepositoryTestSupport
import com.whatever.raisedragon.domain.betting.BettingPredictionType.FAIL
import com.whatever.raisedragon.domain.betting.BettingPredictionType.SUCCESS
import com.whatever.raisedragon.domain.betting.BettingResult.*
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class BettingRepositoryTest : RepositoryTestSupport() {

    @Autowired
    private lateinit var bettingRepository: BettingRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @DisplayName("User와 Goal이 같은 Betting 조회한다.")
    @Test
    fun findByUserEntityAndGoalEntity() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))


        val goalEntity1 = createGoalEntity(userEntity1, GoalType.BILLING, GoalResult.PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, GoalType.FREE, GoalResult.SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity1,
            bettingPredictionType = FAIL,
            bettingResult = PROCEEDING
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity1,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity2,
            bettingPredictionType = SUCCESS,
            bettingResult = GET_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3))

        // when
        val foundBettingEntity = bettingRepository.findByUserEntityAndGoalEntity(userEntity1, goalEntity1)

        // then
        assertThat(foundBettingEntity)
            .isNotNull
            .isEqualTo(foundBettingEntity)
    }

    @DisplayName("Goal이 같은 Betting을 모두 조회한다.")
    @Test
    fun findAllByGoalEntity() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(
            userEntity = userEntity,
            goalType = GoalType.FREE,
            goalResult = GoalResult.PROCEEDING
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity,
            goalType = GoalType.BILLING,
            goalResult = GoalResult.FAIL
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = FAIL,
            bettingResult = PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity1,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity2,
            bettingPredictionType = SUCCESS,
            bettingResult = GET_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3))

        // when
        val foundBettingEntityList = bettingRepository.findAllByGoalEntity(goalEntity1)

        // then
        assertThat(foundBettingEntityList)
            .hasSize(2)
            .extracting("userEntity", "goalEntity", "bettingPredictionType", "bettingResult")
            .containsExactlyInAnyOrder(
                tuple(userEntity, goalEntity1, FAIL, PROCEEDING),
                tuple(userEntity, goalEntity1, SUCCESS, NO_GIFTICON)
            )
    }

    @DisplayName("User가 같은 Betting을 모두 조회한다.")
    @Test
    fun findAllByUserEntity() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("nickname1"))
        val userEntity2 = UserEntity(nickname = Nickname("nickname2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(
            userEntity = userEntity1,
            goalType = GoalType.FREE,
            goalResult = GoalResult.PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = FAIL,
            bettingResult = PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity1,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = GET_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = GET_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val foundBettingEntityList = bettingRepository.findAllByUserEntity(userEntity1)

        // then
        assertThat(foundBettingEntityList)
            .hasSize(3)
            .extracting("userEntity", "goalEntity", "bettingPredictionType", "bettingResult")
            .containsExactlyInAnyOrder(
                tuple(userEntity1, goalEntity, FAIL, PROCEEDING),
                tuple(userEntity1, goalEntity, SUCCESS, NO_GIFTICON),
                tuple(userEntity1, goalEntity, SUCCESS, GET_GIFTICON),
            )
    }

    @DisplayName("Ids 안에 포함된 id의 베팅의 Result를 변경한다.")
    @Test
    fun bulkModifyingByResultWhereIdInIds() {
        // given
        val userEntity = UserEntity(nickname = Nickname("nickname1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(
            userEntity = userEntity,
            goalType = GoalType.FREE,
            goalResult = GoalResult.PROCEEDING
        )
        goalRepository.save(goalEntity)

        val bettingEntity1 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = FAIL,
            bettingResult = PROCEEDING
        )
        val bettingEntity2 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON
        )
        val bettingEntity3 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON,
        )
        val bettingEntity4 = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = SUCCESS,
            bettingResult = NO_GIFTICON
        )
        bettingRepository.saveAll(listOf(bettingEntity1, bettingEntity2, bettingEntity3, bettingEntity4))

        // when
        val updatedBettingCount = bettingRepository.bulkModifyingByBettingResultWhereIdInIds(
            bettingResult = GET_GIFTICON,
            ids = listOf(bettingEntity1, bettingEntity2, bettingEntity3).map { it.id }.toSet()
        )

        // then
        assertThat(updatedBettingCount)
            .isEqualTo(3)
    }

    private fun createGoalEntity(
        userEntity: UserEntity,
        goalType: GoalType,
        goalResult: GoalResult
    ): GoalEntity {
        val startDateTime = LocalDateTime.now()
        val endDateTime = startDateTime.plusDays(7)
        return GoalEntity(
            userEntity = userEntity,
            type = goalType,
            content = Content("sampleContent"),
            goalResult = goalResult,
            startDate = startDateTime,
            endDate = endDateTime
        )
    }
}