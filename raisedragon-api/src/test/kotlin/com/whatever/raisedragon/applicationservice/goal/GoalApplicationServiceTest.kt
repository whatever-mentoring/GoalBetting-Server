package com.whatever.raisedragon.applicationservice.goal

import com.whatever.raisedragon.applicationservice.ApplicationServiceTestSupport
import com.whatever.raisedragon.applicationservice.goal.dto.GoalCreateServiceRequest
import com.whatever.raisedragon.applicationservice.goal.dto.GoalResponse
import com.whatever.raisedragon.domain.gifticon.GifticonService
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.Content
import com.whatever.raisedragon.domain.goal.GoalResult
import com.whatever.raisedragon.domain.goal.GoalType
import com.whatever.raisedragon.domain.goalgifticon.GoalGifticonService
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
class GoalApplicationServiceTest : ApplicationServiceTestSupport {

    @Autowired
    private lateinit var goalApplicationService: GoalApplicationService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalGifticonService: GoalGifticonService

    @Autowired
    private lateinit var gifticonService: GifticonService

    @DisplayName("상품이 없는 Goal을 생성한다.")
    @Test
    fun createFreeGoal() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val now = LocalDateTime.now()
        val goalType = GoalType.FREE
        val request = GoalCreateServiceRequest(
            content = Content("Sample content"),
            goalType = goalType,
            startDate = now,
            endDate = now.plusDays(7L),
            userId = userEntity.id,
            gifticonUrl = null
        )

        // when
        val goalResponse = goalApplicationService.createGoal(request)

        // then
        assertThat(goalResponse).isInstanceOf(GoalResponse::class.java)
        assertThat(goalResponse.hostUserId).isEqualTo(userEntity.id)
        assertThat(goalResponse.type).isEqualTo(goalType)
        assertThat(goalResponse.goalResult).isEqualTo(GoalResult.PROCEEDING)
    }

    @DisplayName("상품이 있는 Goal을 생성한다.")
    @Test
    fun createBillingGoal() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val now = LocalDateTime.now()
        val goalType = GoalType.BILLING
        val gifticonUrl = URL("www.sample.com")
        val request = GoalCreateServiceRequest(
            content = Content("Sample content"),
            goalType = goalType,
            startDate = now,
            endDate = now.plusDays(7L),
            userId = userEntity.id,
            gifticonUrl = gifticonUrl
        )

        // when
        val goalResponse = goalApplicationService.createGoal(request)

        // then
        assertThat(goalResponse).isInstanceOf(GoalResponse::class.java)
        assertThat(goalResponse.hostUserId).isEqualTo(userEntity.id)
        assertThat(goalResponse.type).isEqualTo(goalType)
        assertThat(goalResponse.goalResult).isEqualTo(GoalResult.PROCEEDING)
        val gifticonId = goalGifticonService.findByGoalId(goalResponse.id)?.gifticonId
        assertThat(gifticonId).isNotNull()
        assertThat(gifticonService.findById(gifticonId!!).url).isEqualTo(gifticonUrl)
    }

}