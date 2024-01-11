package com.whatever.raisedragon.scheduler

import com.whatever.raisedragon.domain.betting.Betting
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import com.whatever.raisedragon.domain.betting.BettingService
import com.whatever.raisedragon.domain.goal.Goal
import com.whatever.raisedragon.domain.goal.GoalResult
import com.whatever.raisedragon.domain.goal.GoalService
import com.whatever.raisedragon.domain.goal.GoalType
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.goalproof.GoalProofService
import com.whatever.raisedragon.domain.winner.WinnerService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class GoalResultScheduler(
    private val goalService: GoalService,
    private val goalProofService: GoalProofService,
    private val goalGifticonService: GoalGifticonService,
    private val bettingService: BettingService,
    private val winnerService: WinnerService
) {
    private val logger = LoggerFactory.getLogger(GoalResultScheduler::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    @Async("asyncSchedulerExecutor")
    @Transactional
    fun adjustGoalResult() {
        val now = LocalDateTime.now()
        logger.info("Start adjusting result of goals at {}", now)
        val endDate = LocalDateTime.of(now.year, now.month, now.dayOfMonth, 23, 59, 59, 59)
        val betGoalList = goalService.findAllByEndDateLessThanEqualAndResultIsProceeding(endDate)
        betGoalList.forEach(::confirmGoalResult)
        logger.info("Done adjusting result for {} goals, at {}", betGoalList.size, LocalDateTime.now())
    }

    private fun confirmGoalResult(goal: Goal) {
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
            if (gifticonId == null) {
                logger.error("[{}] cannot find gifticon from Goal {}.", this::class.java.simpleName, goal.id)
                throw IllegalStateException("cannot find gifticon from Goal ${goal.id}")
            }
            val winnerBetting = succeedBettingList.random()
            val loserBettingList = succeedBettingList - winnerBetting
            bettingService.bulkModifyingByResultWhereIdInIds(
                loserBettingList.map { betting -> betting.id }.toSet(),
                com.whatever.raisedragon.domain.betting.BettingResult.NO_GIFTICON
            )
            bettingService.updateResult(
                winnerBetting.id,
                com.whatever.raisedragon.domain.betting.BettingResult.GET_GIFTICON
            )
            winnerService.create(
                goalId = goal.id,
                userId = winnerBetting.userId,
                gifticonId = gifticonId
            )
        }
    }
}