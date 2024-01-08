package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.RepositoryTestSupport
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.PROCEEDING
import com.whatever.raisedragon.domain.goal.GoalResult.SUCCESS
import com.whatever.raisedragon.domain.goal.GoalType.BILLING
import com.whatever.raisedragon.domain.goal.GoalType.FREE
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class GoalProofRepositoryTest : RepositoryTestSupport() {

    @Autowired
    private lateinit var goalProofRepository: GoalProofRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @DisplayName("특정 UserEntity와 특정 GoalEntity를 포함하는 모든 GoalProof를 조회한다.")
    @Test
    fun findAllByUserEntityAndGoalEntity() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalProofEntity(userEntity1, BILLING, PROCEEDING)
        val goalEntity2 = createGoalProofEntity(userEntity1, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1)
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2)
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1)
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2)
        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2, goalProofEntity3, goalProofEntity4))

        // when
        val foundGoalProofEntityList = goalProofRepository.findAllByUserEntityAndGoalEntity(userEntity1, goalEntity1)

        // then
        assertThat(foundGoalProofEntityList).hasSize(1)
            .first()
            .isEqualTo(goalProofEntity1)
    }

    @DisplayName("특정 UserEntity를 포함한 모든 GoalProof를 조회한다.")
    @Test
    fun findAllByUserEntity() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalProofEntity(userEntity1, BILLING, PROCEEDING)
        goalRepository.save(goalEntity)

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity)
        val goalProofEntity2 = createGoalProofEntity(userEntity2, goalEntity)
        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2))

        // when
        val foundGoalProofEntityList = goalProofRepository.findAllByUserEntity(userEntity1)

        // then
        assertThat(foundGoalProofEntityList).hasSize(1)
            .first()
            .isEqualTo(goalProofEntity1)
    }

    // TODO: created_at이 변경 불가능한 상태라 오늘 시간대가 아니면 테스트가 깨지는 이슈가 있음.
    @Disabled
    @DisplayName("특정 GoalEntity를 포함하며 CreatedAt이 특정 시간대 범위 밖이면 false를 반환한다.")
    @Test
    fun existsByGoalEntityAndCreatedAtBetween() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalProofEntity(userEntity, BILLING, PROCEEDING)
        goalRepository.save(goalEntity)

        val todayStartDateTime = LocalDateTime.now().minusDays(2)
        val todayEndDateTime = todayStartDateTime.plusDays(2)

        val goalProofEntity1 = createGoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            createdAt = todayStartDateTime.minusSeconds(1L)
        )
        val goalProofEntity2 = createGoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            createdAt = todayEndDateTime.plusSeconds(1L)
        )
        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2))

        // when
        val result = goalProofRepository.existsByGoalEntityAndCreatedAtBetween(
            goalEntity = goalEntity,
            todayStartDateTime = todayStartDateTime,
            todayEndDateTime = todayEndDateTime
        )

        // then
        assertThat(result).isEqualTo(false)
    }

    // TODO: created_at이 변경 불가능한 상태라 오늘 시간대가 아니면 테스트가 깨지는 이슈가 있음.
    @DisplayName("특정 GoalEntity를 포함하며 CreatedAt이 특정 시간대 범위 안이면 true를 반환한다.")
    @Test
    fun existsByGoalEntityAndCreatedAtBetween2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalProofEntity(userEntity, BILLING, PROCEEDING)
        goalRepository.save(goalEntity)

        val targetDateTime = LocalDateTime.now()
        val todayStartDateTime = LocalDateTime.of(
            targetDateTime.year,
            targetDateTime.month,
            targetDateTime.dayOfMonth,
            0,
            0,
            0
        )
        val todayEndDateTime = LocalDateTime.of(
            targetDateTime.year,
            targetDateTime.month,
            targetDateTime.dayOfMonth,
            23,
            59,
            59
        )

        val goalProofEntity1 = createGoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            createdAt = todayStartDateTime.plusSeconds(1L)
        )
        val goalProofEntity2 = createGoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            createdAt = todayEndDateTime.minusSeconds(1L)
        )
        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2))

        // when
        val result = goalProofRepository.existsByGoalEntityAndCreatedAtBetween(
            goalEntity = goalEntity,
            todayStartDateTime = todayStartDateTime,
            todayEndDateTime = todayEndDateTime
        )

        // then
        assertThat(result).isEqualTo(true)
    }

    @DisplayName("특정 GoalEntity를 포함한 모든 GoalProof의 개수를 반환한다.")
    @Test
    fun countAllByGoalEntity() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalProofEntity(userEntity, BILLING, PROCEEDING)
        val goalEntity2 = createGoalProofEntity(userEntity, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val goalProofEntity1 = createGoalProofEntity(userEntity, goalEntity1)
        val goalProofEntity2 = createGoalProofEntity(userEntity, goalEntity2)
        val goalProofEntity3 = createGoalProofEntity(userEntity, goalEntity1)
        val goalProofEntity4 = createGoalProofEntity(userEntity, goalEntity2)
        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2, goalProofEntity3, goalProofEntity4))

        // when
        val foundGoalProofCount = goalProofRepository.countAllByGoalEntity(goalEntity1)

        // then
        assertThat(foundGoalProofCount).isEqualTo(2)
    }

    private fun createGoalProofEntity(
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

    private fun createGoalProofEntity(
        userEntity: UserEntity,
        goalEntity: GoalEntity,
        createdAt: LocalDateTime? = null
    ) = GoalProofEntity(
        userEntity = userEntity,
        goalEntity = goalEntity,
        url = URL("sample url"),
        comment = Comment("sample comment")
    ).also { goalProof -> createdAt?.let { goalProof.createdAt = it } }
}