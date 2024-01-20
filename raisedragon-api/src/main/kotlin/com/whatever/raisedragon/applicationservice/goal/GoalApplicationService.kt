package com.whatever.raisedragon.applicationservice.goal

import com.whatever.raisedragon.applicationservice.goal.dto.*
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.domain.winner.WinnerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class GoalApplicationService(
    private val goalService: GoalService,
    private val gifticonService: GifticonService,
    private val goalGifticonService: GoalGifticonService,
    private val goalProofService: GoalProofService,
    private val winnerService: WinnerService,
    private val userService: UserService,
    private val bettingService: BettingService
) {

    @Transactional
    fun createGoal(request: GoalCreateServiceRequest): GoalResponse {
        if (isNumberOfGoalUnderOneHundred(request.userId)) throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "다짐을 생성하는 중, 생성할 수 있는 다짐 갯수를 초과하였습니다."
        )
        if (goalService.existsByUserIdAndAnyGoalResult(request.userId, GoalResult.PROCEEDING)) throw BaseException.of(
            exceptionCode = ExceptionCode.E409_CONFLICT,
            executionMessage = "다짐을 생성하는 중, 이미 생성한 다짐이 있어 생성이 불가합니다."
        )
        val goal = goalService.create(
            userId = request.userId,
            content = request.content,
            goalType = request.goalType,
            threshold = Threshold(0),
            startDate = request.startDate,
            endDate = request.endDate
        )
        if (!request.gifticonUrl.isNullOrBlank() && request.goalType == GoalType.BILLING) {
            val gifticon = gifticonService.create(request.userId, request.gifticonUrl)
            goalGifticonService.create(
                goalId = goal.id,
                gifticonId = gifticon.id
            )
        }
        val hostUser = userService.loadById(request.userId)
        return GoalResponse.of(goal, hostUser.nickname.value)
    }

    fun retrieveGoal(goalId: Long): GoalResponse {
        val goal = goalService.findById(goalId)
        val hostUser = userService.loadById(goal.userId)
        return GoalResponse.of(goal, hostUser.nickname.value)
    }

    fun retrieveGoalDetail(goalId: Long, userId: Long): GoalWithBettingResponse {
        val goal = goalService.findById(goalId)
        val hostUser = userService.loadById(goal.userId)
        val betting = bettingService.loadUserAndGoal(userId, goalId)
        val goalProofs = goalProofService.findAllByGoalIdAndUserId(goalId, userId)
        val winnerNickname = winnerService.findWinnerNicknameByGoalId(goalId)?.value
        // TODO : Number 7 must be changed after adjusting goal's threshold
        val isSuccess = goalProofs.size >= 7
        return GoalWithBettingResponse.of(
            goal = goal,
            hostUserNickname = hostUser.nickname.value,
            betting = betting,
            isSuccess = isSuccess,
            winnerNickname = winnerNickname)
    }

    fun retrieveAllByUserId(userId: Long): List<GoalResponse> {
        val goals = goalService.findAllByUserId(userId)
        val users = userService.findAllByIdInIds(goals.map { it.userId }.toSet())

        return goals.map { goal ->
            val hostUser = users.firstOrNull { user -> user.id == goal.userId } ?: throw BaseException.of(
                exceptionCode = ExceptionCode.E404_NOT_FOUND,
                executionMessage = "Goal(${goal.id}에 해당하는 유저를 찾을 수 없습니다. ${goal.userId}"
            )
            GoalResponse.of(goal, hostUser.nickname.value)
        }
    }

    fun retrieveGoalBettingParticipant(
        goalId: Long
    ): GoalRetrieveParticipantResponse {
        val goal = goalService.findById(goalId)
        val hostUser = userService.loadById(goal.userId)
        val bettingList = bettingService.findAllByGoalId(goalId)

        val hostDto = GoalBettingHostResponse(
            id = hostUser.id!!,
            nickname = hostUser.nickname.value,
            goalCreatedAt = goal.createdAt
        )

        val participants = bettingList.map {
            GoalBettingParticipantResponse(
                userId = it.userId,
                nickname = userService.loadById(it.userId).nickname.value,
                bettingId = it.id,
                bettingPredictionType = it.bettingPredictionType,
                bettingResult = it.bettingResult,
                bettingCreatedAt = it.createdAt!!
            )
        }

        return GoalRetrieveParticipantResponse(
            hostUser = hostDto,
            participants = participants
        )
    }

    fun retrieveGoalBettingParticipantNoAuth(
        goalId: Long
    ): GoalRetrieveParticipantResponse {
        val goal = goalService.findById(goalId)
        val goalHostUser = userService.loadById(goal.userId)
        val bettingList = bettingService.findAllByGoalId(goalId)

        val hostDto = GoalBettingHostResponse(
            id = goalHostUser.id!!,
            nickname = goalHostUser.nickname.value,
            goalCreatedAt = goal.createdAt
        )

        val participants = bettingList.map {
            GoalBettingParticipantResponse(
                userId = it.userId,
                nickname = userService.loadById(it.userId).nickname.value,
                bettingId = it.id,
                bettingPredictionType = it.bettingPredictionType,
                bettingResult = it.bettingResult,
                bettingCreatedAt = it.createdAt!!
            )
        }

        return GoalRetrieveParticipantResponse(
            hostUser = hostDto,
            participants = participants
        )
    }

    fun retrieveGoalWithBettingByBetUserId(userId: Long): List<GoalWithBettingResponse> {
        val bettingList = bettingService.findAllByUserId(userId)
        val betGoals = goalService.findAllByIds(bettingList.map { it.goalId }.toSet())
        val hostUsers = userService.findAllByIdInIds(betGoals.map { it.userId }.toSet())

        return betGoals.map { goal ->
            val hostUser = hostUsers.firstOrNull { user -> user.id == goal.userId } ?: throw BaseException.of(
                exceptionCode = ExceptionCode.E404_NOT_FOUND,
                executionMessage = "Goal(${goal.id}에 해당하는 유저를 찾을 수 없습니다. ${goal.userId}"
            )
            val goalProofs = goalProofService.findAllByGoalIdAndUserId(goal.id, userId)
            val winnerNickname = winnerService.findWinnerNicknameByGoalId(goal.id)?.value
            // TODO : Number 7 must be changed after adjusting goal's threshold
            val isSuccess = goalProofs.size >= 7
            GoalWithBettingResponse.of(
                goal = goal,
                hostUserNickname = hostUser.nickname.value,
                betting = bettingList.firstOrNull { betting -> betting.userId == userId },
                isSuccess = isSuccess,
                winnerNickname = winnerNickname
            )
        }
    }

    @Transactional
    fun modifyGoal(request: GoalModifyServiceRequest): GoalResponse {
        val goal = goalService.findById(request.goalId)
        isNotUsersGoal(goal, request.userId)
        isAlreadyStarted(goal)

        val modifiedGoal = goalService.updateContent(
            goalId = goal.id,
            content = request.content
        )

        val hostUser = userService.loadById(request.userId)
        return GoalResponse.of(modifiedGoal, hostUser.nickname.value)
    }

    @Transactional
    fun deleteGoal(request: GoalDeleteServiceRequest) {
        val goal = goalService.findById(request.goalId)
        isNotUsersGoal(goal, request.userId)
        isAlreadyStarted(goal)

        goalService.softDelete(goal.id)
    }

    private fun isNotUsersGoal(goal: Goal, userId: Long) {
        if (goal.userId != userId) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "다짐을 수정하는 중, 쓰기 권한이 없는 리소스에 대한 요청은 수행할 수 없습니다."
            )
        }
    }

    private fun isAlreadyStarted(goal: Goal) {
        if (goal.startDate < LocalDateTime.now()) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "다짐에 쓰기작업을 하는 중, 이미 시작된 다짐은 수정/삭제할 수 없습니다."
            )
        }
    }

    private fun isNumberOfGoalUnderOneHundred(userId: Long): Boolean {
        return goalService.findAllByUserId(userId).size > 99
    }
}