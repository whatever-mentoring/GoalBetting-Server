package com.whatever.raisedragon.applicationservice.betting

import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateServiceRequest
import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateUpdateResponse
import com.whatever.raisedragon.applicationservice.betting.dto.BettingRetrieveResponse
import com.whatever.raisedragon.applicationservice.betting.dto.BettingUpdateServiceRequest
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.betting.*
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@Transactional
class BettingApplicationServiceTest : ApplicationServiceTestSupport {

    @Autowired
    private lateinit var bettingApplicationService: BettingApplicationService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var bettingRepository: BettingRepository

    @DisplayName("Betting을 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1L))
        goalRepository.save(goalEntity)

        val request = BettingCreateServiceRequest(
            userId = userEntity2.id,
            goalId = goalEntity.id,
            bettingPredictionType = BettingPredictionType.SUCCESS
        )

        // when
        val bettingResponse = bettingApplicationService.create(request)

        // then
        assertThat(bettingResponse)
            .isInstanceOf(BettingCreateUpdateResponse::class.java)
        assertThat(bettingResponse.bettingRetrieveResponse)
            .isInstanceOf(BettingRetrieveResponse::class.java)
            .extracting("userId", "goalId", "bettingPredictionType", "bettingResult")
            .contains(request.userId, request.goalId, request.bettingPredictionType, BettingResult.PROCEEDING)
    }

    @DisplayName("Betting을 생성을 요청하는 사용자의 id로 만들어진 Betting이 있는 경우 생성에 실패한다.")
    @Test
    fun create2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1L))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        val request = BettingCreateServiceRequest(
            userId = userEntity2.id,
            goalId = goalEntity.id,
            bettingPredictionType = BettingPredictionType.SUCCESS
        )

        // when // then
        assertThatThrownBy { bettingApplicationService.create(request) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("이미 배팅에 참여한 다짐입니다.")
    }

    @DisplayName("Betting을 생성을 요청하는 사용자의 id로 만들어진 Goal에 Betting을 생성할 경우 실패한다.")
    @Test
    fun create3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING, LocalDateTime.now().plusDays(1L))
        goalRepository.save(goalEntity)

        val request = BettingCreateServiceRequest(
            userId = userEntity.id,
            goalId = goalEntity.id,
            bettingPredictionType = BettingPredictionType.SUCCESS
        )

        // when // then
        assertThatThrownBy { bettingApplicationService.create(request) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("자신의 다짐에는 베팅할 수 없습니다.")
    }

    @DisplayName("참여하려는 Goal이 이미 시작한 경우 생성에 실패한다.")
    @Test
    fun create4() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val request = BettingCreateServiceRequest(
            userId = userEntity2.id,
            goalId = goalEntity.id,
            bettingPredictionType = BettingPredictionType.SUCCESS
        )

        // when // then
        assertThatThrownBy { bettingApplicationService.create(request) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("이미 시작한 내기 입니다.")
    }

    @DisplayName("bettingId를 입력받아 단건 Betting을 조회합니다.")
    @Test
    fun retrieve() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        val response = bettingApplicationService.retrieve(bettingEntity.id)

        // then
        assertThat(response)
            .isInstanceOf(BettingRetrieveResponse::class.java)
        assertThat(response.id).isEqualTo(bettingEntity.id)
        assertThat(response.userId).isEqualTo(userEntity2.id)
        assertThat(response.goalId).isEqualTo(goalEntity.id)
        assertThat(response.bettingPredictionType).isEqualTo(bettingEntity.bettingPredictionType)
        assertThat(response.bettingResult).isEqualTo(bettingEntity.bettingResult)
    }

    @DisplayName("Betting을 업데이트 할 수 있다.")
    @Test
    fun update1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1L))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        val willUpdatePredictionType = BettingPredictionType.FAIL
        val request = BettingUpdateServiceRequest(
            userId = userEntity2.id,
            bettingId = bettingEntity.id,
            bettingPredictionType = willUpdatePredictionType
        )

        // when
        val response = bettingApplicationService.update(request)

        // then
        assertThat(response).isInstanceOf(BettingRetrieveResponse::class.java)
        assertThat(response.id).isEqualTo(bettingEntity.id)
        assertThat(response.userId).isEqualTo(userEntity2.id)
        assertThat(response.goalId).isEqualTo(goalEntity.id)
        assertThat(response.bettingPredictionType).isEqualTo(willUpdatePredictionType)
        assertThat(response.bettingResult).isEqualTo(bettingEntity.bettingResult)
    }

    @DisplayName("Betting을 작성한 유저가 아닌 경우 update에 실패한다.")
    @Test
    fun update2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        val willUpdatePredictionType = BettingPredictionType.FAIL
        val request = BettingUpdateServiceRequest(
            userId = userEntity1.id,
            bettingId = bettingEntity.id,
            bettingPredictionType = willUpdatePredictionType
        )

        // when // then
        assertThatThrownBy { bettingApplicationService.update(request) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("접근할 수 없는 내기 입니다.")
    }

    @DisplayName("Betting을 업데이트하는 시점에 이미 해당 다짐이 시작한 경우 실패한다.")
    @Test
    fun update3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING, LocalDateTime.now().minusDays(1))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        val willUpdatePredictionType = BettingPredictionType.FAIL
        val request = BettingUpdateServiceRequest(
            userId = userEntity.id,
            bettingId = bettingEntity.id,
            bettingPredictionType = willUpdatePredictionType
        )

        // when // then
        assertThatThrownBy { bettingApplicationService.update(request) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("이미 시작한 내기 입니다.")
    }

    @DisplayName("Betting을 삭제 할 수 있다.")
    @Test
    fun delete1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1L))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when
        bettingApplicationService.delete(userEntity2.id, bettingEntity.id)

        // then
        bettingRepository.findById(bettingEntity.id).isEmpty
    }

    @DisplayName("Betting을 작성한 유저가 아닌 경우 delete에 실패한다.")
    @Test
    fun delete2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING, LocalDateTime.now().plusDays(1))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity2,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingApplicationService.delete(userEntity1.id, bettingEntity.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("접근할 수 없는 내기 입니다.")
    }

    @DisplayName("Betting을 업데이트하는 시점에 이미 해당 다짐이 시작한 경우 실패한다.")
    @Test
    fun delete3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User1"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING, LocalDateTime.now().minusDays(1))
        goalRepository.save(goalEntity)

        val bettingEntity = BettingEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            bettingPredictionType = BettingPredictionType.SUCCESS,
            bettingResult = BettingResult.PROCEEDING
        )
        bettingRepository.save(bettingEntity)

        // when // then
        assertThatThrownBy { bettingApplicationService.delete(userEntity.id, bettingEntity.id) }
            .isInstanceOf(BaseException::class.java)
            .hasMessage("이미 시작한 내기 입니다.")
    }

    private fun createGoalEntity(
        userEntity: UserEntity,
        goalType: GoalType,
        goalResult: GoalResult,
        startDateTime: LocalDateTime = LocalDateTime.now()
    ): GoalEntity {
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