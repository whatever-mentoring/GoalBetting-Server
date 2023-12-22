package com.whatever.raisedragon.applicationservice

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.betting.PredictionType
import com.whatever.raisedragon.domain.goal.BettingType
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goal.Result
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.winner.WinnerService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TestApplicationService(
    private val goalService: GoalService,
    private val goalProofService: GoalProofService,
    private val goalGifticonService: GoalGifticonService,
    private val bettingService: BettingService,
    private val winnerService: WinnerService
) {

    @Transactional
    fun confirmGoalResult(goalId: Long) {
        val goal = goalService.loadById(goalId)
        val goalProofCount = goalProofService.countAllByGoalId(goal.id)
        // TODO: using 7 instead of goal's threshold. must be changed to goal's threshold after confirming business requirements
        val goalResult = if (goalProofCount >= 7) Result.SUCCESS else Result.FAIL
        goalService.updateResult(goal.id, goalResult)

        val bettingList = bettingService.findAllByGoalIdAndNotDeleted(goal.id)
        val failedBettingList = mutableListOf<Betting>()
        val succeedBettingList = mutableListOf<Betting>()
        bettingList.forEach { betting ->
            if (betting.predictionType == PredictionType.FAIL && goalResult == Result.FAIL ||
                betting.predictionType == PredictionType.SUCCESS && goalResult == Result.SUCCESS
            ) {
                succeedBettingList.add(betting)
            } else {
                failedBettingList.add(betting)
            }
        }
        bettingService.bulkModifyingByResultWhereIdInIds(
            failedBettingList.map { betting -> betting.id }.toSet(),
            com.whatever.raisedragon.domain.betting.Result.FAIL
        )
        if (goal.type == BettingType.FREE) {
            bettingService.bulkModifyingByResultWhereIdInIds(
                succeedBettingList.map { betting -> betting.id }.toSet(),
                com.whatever.raisedragon.domain.betting.Result.NO_GIFTICON
            )
        } else {
            val gifticonId = goalGifticonService.findByGoalId(goal.id)?.gifticonId
                ?: throw IllegalStateException("cannot find gifticon from Goal ${goal.id}")
            val winnerBetting = succeedBettingList.random()
            val loserBettingList = succeedBettingList - winnerBetting
            bettingService.bulkModifyingByResultWhereIdInIds(
                loserBettingList.map { betting -> betting.id }.toSet(),
                com.whatever.raisedragon.domain.betting.Result.NO_GIFTICON
            )
            bettingService.updateResult(winnerBetting.id, com.whatever.raisedragon.domain.betting.Result.GET_GIFTICON)
            winnerService.create(
                goalId = goal.id,
                userId = winnerBetting.userId,
                gifticonId = gifticonId
            )
        }
    }
}