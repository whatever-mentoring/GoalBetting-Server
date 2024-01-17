package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Transactional
class GoalServiceTest : IntegrationTestSupport {

    @Autowired
    private lateinit var goalService: GoalService

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @DisplayName("기본적인 Goal를 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val content = Content("Sample content")
        val goalType = FREE
        val threshold = Threshold(5)
        val startDate = LocalDateTime.now()
        val endDate = startDate.plusDays(7)

        // when
        val goal = goalService.create(
            userId = userEntity.id,
            content = content,
            goalType = goalType,
            threshold = threshold,
            startDate = startDate,
            endDate = endDate
        )

        // then
        assertThat(goal)
            .isInstanceOf(Goal::class.java)
            .isEqualTo(
                Goal(
                    id = goal.id,
                    userId = userEntity.id,
                    type = goalType,
                    content = content,
                    threshold = threshold,
                    goalResult = PROCEEDING,
                    startDate = startDate,
                    endDate = endDate,
                    createdAt = goal.createdAt,
                    updatedAt = goal.updatedAt,
                    deletedAt = goal.deletedAt
                )
            )
    }

    @DisplayName("잘못된 userId를 가지고 요청한 경우 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val content = Content("Sample content")
        val goalType = FREE
        val threshold = Threshold(5)
        val startDate = LocalDateTime.now()
        val endDate = startDate.plusDays(7)

        // when // then
        assertThatThrownBy {
            goalService.create(
                userId = -1,
                content = content,
                goalType = goalType,
                threshold = threshold,
                startDate = startDate,
                endDate = endDate
            )
        }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("id에 해당하는 Goal를 조회한다.")
    @Test
    fun loadById1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when
        val foundGoal = goalService.loadById(goalEntity.id)

        // then
        assertThat(foundGoal)
            .isInstanceOf(Goal::class.java)
            .isEqualTo(
                Goal(
                    id = goalEntity.id,
                    userId = userEntity.id,
                    type = goalEntity.goalType,
                    content = goalEntity.content,
                    threshold = goalEntity.threshold,
                    goalResult = goalEntity.goalResult,
                    startDate = goalEntity.startDate,
                    endDate = goalEntity.endDate,
                    createdAt = goalEntity.createdAt,
                    updatedAt = goalEntity.updatedAt,
                    deletedAt = goalEntity.deletedAt
                )
            )
    }

    @DisplayName("잘못된 goalId를 가지고 요청한 경우 예외가 발생한다.")
    @Test
    fun loadById2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy { goalService.loadById(-1L) }.isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId와 GoalResult가 같은 Goal이 있으면 true를 반환한다.")
    @Test
    fun existsByUserIdAndAnyGoalResult1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val isExist = goalService.existsByUserIdAndAnyGoalResult(userEntity.id, PROCEEDING)

        // then
        assertThat(isExist).isTrue()
    }

    @DisplayName("userId와 GoalResult가 같은 Goal가 없으면 false를 반환한다.")
    @Test
    fun existsByUserIdAndAnyGoalResult2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val isExist = goalService.existsByUserIdAndAnyGoalResult(userEntity2.id, PROCEEDING)

        // then
        assertThat(isExist).isFalse()
    }

    @DisplayName("userId와 GoalResult가 같은 Goal이 있는지 확인할 때 요청된 id로 저장된 유저를 찾을 수 없는 경우 예외가 발생한다.")
    @Test
    fun existsByUserIdAndAnyGoalResult3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when // then
        assertThatThrownBy {
            goalService.existsByUserIdAndAnyGoalResult(-1L, PROCEEDING)
        }.isInstanceOf(BaseException::class.java).hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId가 같은 Goal를 모두 조회한다.")
    @Test
    fun loadAllByUserId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val goalList = goalService.loadAllByUserId(userEntity1.id)

        // then
        assertThat(goalList).hasSize(2)
            .extracting("userId", "type", "goalResult")
            .containsExactlyInAnyOrder(
                tuple(userEntity1.id, FREE, PROCEEDING),
                tuple(userEntity1.id, BILLING, FAIL),
            )
    }

    @DisplayName("userId가 같은 Goal를 모두 찾는 중 해당하는 유저가 없는 경우 예외가 발생한다.")
    @Test
    fun loadAllByUserId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when // then
        assertThatThrownBy { goalService.loadAllByUserId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("ids에 포함되는 id를 갖는 모든 Goal를 모두 조회한다.")
    @Test
    fun findAllByIds() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val goals = goalService.findAllByIds(setOf(goalEntity1.id, goalEntity3.id))

        // then
        assertThat(goals).hasSize(2)
            .extracting("id", "userId", "type", "goalResult")
            .containsExactlyInAnyOrder(
                tuple(goalEntity1.id, userEntity1.id, FREE, PROCEEDING),
                tuple(goalEntity3.id, userEntity2.id, FREE, SUCCESS),
            )
    }

    @DisplayName("endDate가 요청한 endDate보다 적거나 같으며 GoalResult가 PROCEEDING인 모든 Goal를 조회한다.")
    @Test
    fun findAllByEndDateLessThanEqualAndGoalResultIsProceeding() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val specificEndDate = LocalDateTime.now()
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            endDateTime = specificEndDate.minusSeconds(1)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = specificEndDate.minusSeconds(1)
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity2,
            goalType = FREE,
            goalResult = PROCEEDING,
            endDateTime = specificEndDate.plusSeconds(1)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val foundGoal = goalService.findAllByEndDateLessThanEqualAndGoalResultIsProceeding(specificEndDate)

        // then
        assertThat(foundGoal).hasSize(1)
            .first()
            .isEqualTo(goalEntity1.toDto())
    }

    @DisplayName("요청된 id의 Goal의 GoalResult를 수정한다.")
    @Test
    fun updateResult1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity1)

        // when
        val foundGoal = goalService.updateResult(goalEntity1.id, FAIL)

        // then
        assertThat(foundGoal).isInstanceOf(Goal::class.java)
        assertThat(foundGoal.goalResult).isEqualTo(FAIL)
    }

    @DisplayName("요청된 id의 Goal의 GoalResult를 수정할 때 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun updateResult2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity1 = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity1)

        // when // then
        assertThatThrownBy { goalService.updateResult(-1L, FAIL) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 id의 Goal의 content를 변경한다")
    @Test
    fun updateContent1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when
        val foundGoal = goalService.updateContent(goalEntity.id, Content("Modified"))

        // then
        assertThat(foundGoal).isInstanceOf(Goal::class.java)
        assertThat(foundGoal.content).isEqualTo(Content("Modified"))
    }

    @DisplayName("요청한 id의 Goal의 content를 변경할 때 id에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun updateContent2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy { goalService.updateContent(-1L, Content("Modified")) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 id의 Goal를 soft-delete 한다.")
    @Test
    fun softDelete1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when
        val foundGoal = goalService.softDelete(goalEntity.id)

        // then
        assertThat(foundGoal).isInstanceOf(Goal::class.java)
        assertThat(foundGoal.deletedAt).isNotNull()
    }

    @DisplayName("요청한 id의 Goal를 soft-delete 할 때 요청한 id의 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun softDelete2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy { goalService.softDelete(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("userId를 갖는 모든 Goal를 hard-delete 한다.")
    @Test
    fun hardDeleteByUserId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        goalService.hardDeleteByUserId(userEntity1.id)

        // then
        assertThat(
            goalRepository.findAllById(listOf(goalEntity1.id, goalEntity2.id))
        ).isEmpty()
    }

    @DisplayName("userId를 갖는 모든 Goal를 hard-delete 할 때 userId에 해당하는 User가 없으면 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity1, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when // then
        assertThatThrownBy { goalService.hardDeleteByUserId(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("id에 해당하는 Goal의 Threshold = 1 증가시킨다.")
    @Test
    fun increaseThreshold1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val threshold = Threshold(7)
        val goalEntity = GoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            content = Content("sampleContent"),
            threshold = threshold,
            goalResult = PROCEEDING,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )
        goalRepository.save(goalEntity)

        // when
        goalService.increaseThreshold(goalEntity.id)

        // then
        assertThat(goalRepository.findById(goalEntity.id).get().threshold).isEqualTo(Threshold(8))
    }

    @DisplayName("id에 해당하는 Goal의 Threshold = 1 증가시킬 때 id에 해당하는 Goal이 없을 때 예외가 발생한다.")
    @Test
    fun increaseThreshold2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val threshold = Threshold(7)
        val goalEntity = GoalEntity(
            userEntity = userEntity,
            goalType = FREE,
            content = Content("sampleContent"),
            threshold = threshold,
            goalResult = PROCEEDING,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now()
        )
        goalRepository.save(goalEntity)

        // when // then
        assertThatThrownBy { goalService.increaseThreshold(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
    }

    @DisplayName("요청한 User가 생성한 Goal 중 endDate가 현재 시간 이후인 Goal이 있으면 true를 반환합니다.")
    @Test
    fun existsByUserAndEndDateIsAfterThanNow1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val now = LocalDateTime.now()
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            endDateTime = now.plusSeconds(1)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = now.minusSeconds(1)
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity2,
            goalType = FREE,
            goalResult = SUCCESS,
            endDateTime = now.plusSeconds(1)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val result = goalService.existsByUserAndEndDateIsAfterThanNow(userEntity1.id)

        // then
        assertThat(result).isTrue()
    }

    @DisplayName("요청한 User가 생성한 Goal 중 endDate가 현재 시간 이후인 Goal이 없으면 false를 반환합니다.")
    @Test
    fun existsByUserAndEndDateIsAfterThanNow2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val now = LocalDateTime.now()
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            endDateTime = now.minusSeconds(1)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = now.minusSeconds(1)
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity2,
            goalType = FREE,
            goalResult = SUCCESS,
            endDateTime = now.plusSeconds(1)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        val result = goalService.existsByUserAndEndDateIsAfterThanNow(userEntity1.id)

        // then
        assertThat(result).isFalse()
    }

    @DisplayName("요청한 User가 생성한 Goal 중 endDate가 현재 시간 이후인 Goal이 있는지 확인할 때 userId에 해당하는 User가 없으면 예외가 발생한다.")
    @Test
    fun existsByUserAndEndDateIsAfterThanNow3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val now = LocalDateTime.now()
        val goalEntity1 = createGoalEntity(
            userEntity = userEntity1,
            goalType = FREE,
            goalResult = PROCEEDING,
            endDateTime = now.minusSeconds(1)
        )
        val goalEntity2 = createGoalEntity(
            userEntity = userEntity1,
            goalType = BILLING,
            goalResult = FAIL,
            endDateTime = now.minusSeconds(1)
        )
        val goalEntity3 = createGoalEntity(
            userEntity = userEntity2,
            goalType = FREE,
            goalResult = SUCCESS,
            endDateTime = now.plusSeconds(1)
        )
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        // when
        assertThatThrownBy { goalService.existsByUserAndEndDateIsAfterThanNow(-1L) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage(ExceptionCode.E404_NOT_FOUND.message)
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