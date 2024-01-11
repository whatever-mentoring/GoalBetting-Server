package com.whatever.raisedragon.applicationservice.test

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.goal.GoalResult
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goal.GoalType
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
        val goalResult = if (goalProofCount >= 7) GoalResult.SUCCESS else GoalResult.FAIL
        goalService.updateResult(goal.id, goalResult)

        val bettingList = bettingService.findAllByGoalId(goal.id)
        val failedBettingList = mutableListOf<Betting>()
        val succeedBettingList = mutableListOf<Betting>()
        bettingList.forEach { betting ->
            if (betting.bettingPredictionType == BettingPredictionType.FAIL && goalResult == GoalResult.FAIL ||
                betting.bettingPredictionType == BettingPredictionType.SUCCESS && goalResult == GoalResult.SUCCESS
            ) {
                succeedBettingList.add(betting)
            } else {
                failedBettingList.add(betting)
            }
        }
        bettingService.bulkModifyingByResultWhereIdInIds(
            failedBettingList.map { betting -> betting.id }.toSet(),
            com.whatever.raisedragon.domain.betting.BettingResult.FAIL
        )
        if (goal.type == GoalType.FREE) {
            bettingService.bulkModifyingByResultWhereIdInIds(
                succeedBettingList.map { betting -> betting.id }.toSet(),
                com.whatever.raisedragon.domain.betting.BettingResult.NO_GIFTICON
            )
        } else {
            val gifticonId = goalGifticonService.findByGoalId(goal.id)?.gifticonId
                ?: throw IllegalStateException("cannot find gifticon from Goal ${goal.id}")
            val winnerBetting = succeedBettingList.random()
            val loserBettingList = succeedBettingList - winnerBetting
            bettingService.bulkModifyingByResultWhereIdInIds(
                loserBettingList.map { betting -> betting.id }.toSet(),
                com.whatever.raisedragon.domain.betting.BettingResult.NO_GIFTICON
            )
            bettingService.updateResult(winnerBetting.id, com.whatever.raisedragon.domain.betting.BettingResult.GET_GIFTICON)
            winnerService.create(
                goalId = goal.id,
                userId = winnerBetting.userId,
                gifticonId = gifticonId
            )
        }
    }
}