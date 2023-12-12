package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.common.exception.ExceptionCode
import com.whatever.raisedragon.controller.goalproof.GoalProofCreateUpdateResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofListRetrieveResponse
import com.whatever.raisedragon.controller.goalproof.GoalProofRetrieveResponse
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goalproof.Comment
import com.whatever.raisedragon.domain.goalproof.GoalProof
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.user.UserService
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class GoalProofApplicationService(
    private val goalProofService: GoalProofService,
    private val goalService: GoalService,
    private val userService: UserService,
) {

    @Transactional
    fun create(
        userId: Long,
        goalId: Long,
        url: String,
        comment: String
    ): GoalProofCreateUpdateResponse {
        val goal = goalService.loadById(goalId)
        val user = userService.loadById(userId)
        val goalProof = goalProofService.create(
            user = user,
            goal = goal,
            url = URL(url),
            comment = Comment(comment)
        )
        goalService.increaseThreshold(goal, user.fromDto())
        return GoalProofCreateUpdateResponse(GoalProofRetrieveResponse.of(goalProof))
    }

    fun retrieve(goalProofId: Long): GoalProofRetrieveResponse {
        return GoalProofRetrieveResponse.of(findByIdOrThrowException(goalProofId))
    }

    fun retrieveAll(goalId: Long, userId: Long): GoalProofListRetrieveResponse {
        val goalProofs = goalProofService.findAllByGoalIdAndUserId(goalId, userId)
        return GoalProofListRetrieveResponse(goalProofs.map { GoalProofRetrieveResponse.of(it) })
    }

    @Transactional
    fun update(
        goalProofId: Long,
        userId: Long,
        url: String? = null,
        comment: String? = null
    ): GoalProofRetrieveResponse {
        validateUpdatable(url, comment)
        findByIdOrThrowException(goalProofId)
            .also { it.validateOwnerId(userId) }
            .also { it.validateEndDate() }
        return GoalProofRetrieveResponse.of(
            goalProofService.update(goalProofId, url?.let { URL(it) }, comment?.let { Comment(it) })
        )
    }

    private fun findByIdOrThrowException(goalProofId: Long): GoalProof {
        return goalProofService.findById(goalProofId) ?: throw BaseException.of(ExceptionCode.E404_NOT_FOUND)
    }

    private fun validateUpdatable(url: String?, comment: String?) {
        if (url == null && comment == null) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST)
        }
    }

    private fun GoalProof.validateOwnerId(userId: Long) {
        if (this.userId != userId) {
            throw BaseException.of(ExceptionCode.E403_FORBIDDEN, "접근할 수 없는 다짐 인증입니다")
        }
    }

    private fun GoalProof.validateEndDate() {
        if (goalService.loadById(goalId).endDate > LocalDateTime.now()) {
            throw BaseException.of(ExceptionCode.E400_BAD_REQUEST, "이미 끝난 내기입니다")
        }
    }
}