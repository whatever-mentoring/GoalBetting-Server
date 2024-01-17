package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.BILLING
import com.whatever.raisedragon.domain.goal.GoalType.FREE
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Transactional
class GoalRepositoryTest : IntegrationTestSupport {

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("특정 UserEntity가 생성한 모든 GoalEntity를 조회한다.")
    @Test
    fun findAllByUserEntity() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, BILLING, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, FREE, SUCCESS)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, PROCEEDING)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val foundGoalEntityList = goalRepository.findAllByUserEntity(userEntity1)

        // then
        assertThat(foundGoalEntityList).hasSize(2)
            .extracting("userEntity", "goalType", "goalResult")
            .containsExactlyInAnyOrder(
                tuple(userEntity1, BILLING, PROCEEDING),
                tuple(userEntity1, FREE, SUCCESS)
            )
    }

    @DisplayName("endDate보다 적거나 같으며 특정 GoalResult인 모든 GoalEntity를 조회한다.")
    @Test
    fun findAllByEndDateLessThanEqualAndGoalResultIs() {
        // given
        val specificEndDateTime = LocalDateTime.now()
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = PROCEEDING,
            endDateTime = specificEndDateTime.minusSeconds(1L)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = PROCEEDING,
            endDateTime = specificEndDateTime.plusSeconds(1L)
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = specificEndDateTime.plusSeconds(1L)
        )
        val goalEntity4 = createGoalEntity(
            userEntity = userEntity,
            goalType = BILLING,
            goalResult = SUCCESS,
            endDateTime = specificEndDateTime.minusSeconds(1L)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3, goalEntity4))

        // when
        val foundGoalEntityList =
            goalRepository.findAllByEndDateLessThanEqualAndGoalResultIs(specificEndDateTime, PROCEEDING)

        // then
        assertThat(foundGoalEntityList).hasSize(1)
            .first()
            .isEqualTo(goalEntity1)
    }

    @DisplayName("특정 UserEntity가 생성하고 GoalResult가 같은 모든 GoalEntity를 조회한다.")
    @Test
    fun findAllByUserEntityAndGoalResult() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, BILLING, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity3 = createGoalEntity(userEntity1, FREE, SUCCESS)
        val goalEntity4 = createGoalEntity(userEntity2, FREE, FAIL)
        val goalEntity5 = createGoalEntity(userEntity2, FREE, PROCEEDING)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3, goalEntity4, goalEntity5))

        // when
        val foundGoalEntityList = goalRepository.findAllByUserEntityAndGoalResult(userEntity1, PROCEEDING)

        // then
        assertThat(foundGoalEntityList)
            .hasSize(2)
            .extracting("userEntity", "goalType", "goalResult")
            .containsExactlyInAnyOrder(
                tuple(userEntity1, BILLING, PROCEEDING),
                tuple(userEntity1, FREE, PROCEEDING)
            )
    }

    @DisplayName("특정 UserEntity가 생성했으며 endDate가 특정 시간 이후인 GoalEntity가 존재하는지 확인한다.")
    @Test
    fun existsByUserEntityAndEndDateIsAfter() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val specificEndDateTime = LocalDateTime.now()
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = PROCEEDING,
            endDateTime = specificEndDateTime.minusSeconds(1L)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = PROCEEDING,
            endDateTime = specificEndDateTime
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = specificEndDateTime.plusSeconds(1L)
        )
        val goalEntity4 = createGoalEntity(
            userEntity = userEntity2,
            goalType = BILLING,
            goalResult = SUCCESS,
            endDateTime = specificEndDateTime.plusSeconds(1L)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3, goalEntity4))

        // when
        val existsGoalEntity = goalRepository.existsByUserEntityAndEndDateIsAfter(userEntity1, specificEndDateTime)

        // then
        assertThat(existsGoalEntity).isEqualTo(true)
    }

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