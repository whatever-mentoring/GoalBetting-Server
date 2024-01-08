package com.whatever.raisedragon.applicationservice.user

import com.whatever.raisedragon.applicationservice.user.dto.UserNicknameDuplicatedResponse
import com.whatever.raisedragon.applicationservice.user.dto.UserNicknameDuplicatedServiceRequest
import com.whatever.raisedragon.applicationservice.user.dto.UserNicknameUpdateServiceRequest
import com.whatever.raisedragon.applicationservice.user.dto.UserRetrieveResponse
import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.refreshtoken.RefreshTokenService
import com.whatever.raisedragon.domain.user.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserApplicationService(
    private val userService: UserService,
    private val refreshTokenService: RefreshTokenService,
    private val goalService: GoalService,
    private val goalProofService: GoalProofService,
    private val gifticonService: GifticonService,
    private val bettingService: BettingService,
) {

    fun retrieve(id: Long): UserRetrieveResponse {
        val user = userService.loadById(id)
        return UserRetrieveResponse(
            userId = user.id!!,
            nickname = user.nickname,
            nicknameIsModified = user.createdAt!! < user.updatedAt
        )
    }

    @Transactional
    fun updateNickname(request: UserNicknameUpdateServiceRequest): UserRetrieveResponse {
        val user = userService.updateNickname(request.userId, request.nickname)
        return UserRetrieveResponse(
            userId = user.id!!,
            nickname = user.nickname,
            nicknameIsModified = user.createdAt!! < user.updatedAt
        )
    }

    @Transactional
    fun delete(id: Long) {
        val user = userService.loadById(id)
        if (goalService.findProceedingGoalIsExistsByUser(user)) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "아직 진행중인 다짐이 있어 회원탈퇴에 실패했습니다."
            )
        }
        else if (bettingService.existsBettingParticipantUser(id)) {
            throw BaseException.of(
                exceptionCode = ExceptionCode.E400_BAD_REQUEST,
                executionMessage = "아직 진행중인 다짐에 대한 내기가 있어 회원탈퇴에 실패했습니다."
            )
        }
        bettingService.hardDeleteByUserId(id)
        gifticonService.hardDeleteByUserId(id)
        goalProofService.hardDeleteByUserId(id)
        goalService.hardDeleteByUserId(id)
        refreshTokenService.hardDeleteByUserId(id)
        userService.hardDeleteById(id)
    }

    fun isNicknameDuplicated(
        request: UserNicknameDuplicatedServiceRequest
    ): UserNicknameDuplicatedResponse {
        return UserNicknameDuplicatedResponse(
            nicknameIsDuplicated = userService.isNicknameDuplicated(request.nickname)
        )
    }
}