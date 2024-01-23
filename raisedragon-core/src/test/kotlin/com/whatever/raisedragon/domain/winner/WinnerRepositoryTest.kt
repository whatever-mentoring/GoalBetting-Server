package com.whatever.raisedragon.domain.winner

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.gifticon.GifticonEntity
import com.whatever.raisedragon.domain.gifticon.GifticonRepository
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class WinnerRepositoryTest : IntegrationTestSupport {

    @Autowired
    private lateinit var winnerRepository: WinnerRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var gifticonRepository: GifticonRepository

    @DisplayName("goalEntity를 받아 GoalEntity를 포함하는 Winner를 조회한다.")
    @Test
    fun findByGoalEntity1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity1 = GifticonEntity(userEntity = userEntity1, url = URL("URL1"))
        val gifticonEntity2 = GifticonEntity(userEntity = userEntity1, url = URL("URL2"))
        gifticonRepository.saveAll(listOf(gifticonEntity1, gifticonEntity2))

        val winnerEntity1 = WinnerEntity(goalEntity1, userEntity1, gifticonEntity1)
        val winnerEntity2 = WinnerEntity(goalEntity2, userEntity2, gifticonEntity2)
        winnerRepository.saveAll(listOf(winnerEntity1, winnerEntity2))

        // when
        val foundWinner = winnerRepository.findByGoalEntity(goalEntity1)

        // then
        assertThat(foundWinner)
            .isNotNull
            .isInstanceOf(WinnerEntity::class.java)

        assertThat(foundWinner!!.goalEntity)
            .isInstanceOf(GoalEntity::class.java)
            .extracting("goalType", "goalResult")
            .contains(FREE, FAIL)

        assertThat(foundWinner.userEntity)
            .isInstanceOf(UserEntity::class.java)
            .extracting("nickname")
            .isEqualTo(Nickname("User1"))

        assertThat(foundWinner.gifticonEntity)
            .isInstanceOf(GifticonEntity::class.java)
            .extracting("url")
            .isEqualTo(URL("URL1"))
    }

    @DisplayName("goalEntity를 받아 GoalEntity를 포함하는 Winner를 조회하는 중 해당하는 Winner가 없는 경우 null를 반환한다.")
    @Test
    fun findByGoalEntity2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, SUCCESS)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val gifticonEntity1 = GifticonEntity(userEntity = userEntity1, url = URL("URL1"))
        val gifticonEntity2 = GifticonEntity(userEntity = userEntity1, url = URL("URL2"))
        gifticonRepository.saveAll(listOf(gifticonEntity1, gifticonEntity2))

        val winnerEntity1 = WinnerEntity(goalEntity1, userEntity1, gifticonEntity1)
        val winnerEntity2 = WinnerEntity(goalEntity2, userEntity2, gifticonEntity2)
        winnerRepository.saveAll(listOf(winnerEntity1, winnerEntity2))

        // when
        val foundWinner = winnerRepository.findByGoalEntity(goalEntity3)

        // then
        assertThat(foundWinner)
            .isNull()
    }

    @DisplayName("goalEntity와 userEntity를 입력받아 Winner를 조회한다.")
    @Test
    fun findByGoalEntityAndUserEntity1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity1 = GifticonEntity(userEntity = userEntity1, url = URL("URL1"))
        val gifticonEntity2 = GifticonEntity(userEntity = userEntity1, url = URL("URL2"))
        gifticonRepository.saveAll(listOf(gifticonEntity1, gifticonEntity2))

        val winnerEntity1 = WinnerEntity(goalEntity1, userEntity1, gifticonEntity1)
        val winnerEntity2 = WinnerEntity(goalEntity2, userEntity2, gifticonEntity2)
        winnerRepository.saveAll(listOf(winnerEntity1, winnerEntity2))

        // when
        val foundWinner = winnerRepository.findByGoalEntityAndUserEntity(goalEntity2, userEntity2)

        // then
        assertThat(foundWinner)
            .isNotNull
            .isInstanceOf(WinnerEntity::class.java)

        assertThat(foundWinner!!.goalEntity)
            .isInstanceOf(GoalEntity::class.java)
            .extracting("goalType", "goalResult")
            .contains(BILLING, SUCCESS)

        assertThat(foundWinner.userEntity)
            .isInstanceOf(UserEntity::class.java)
            .extracting("nickname")
            .isEqualTo(Nickname("User2"))

        assertThat(foundWinner.gifticonEntity)
            .isInstanceOf(GifticonEntity::class.java)
            .extracting("url")
            .isEqualTo(URL("URL2"))
    }

    @DisplayName("goalEntity와 userEntity를 입력받아 Winner를 조회하는 중 해당하는 Winner를 찾을 수 없으면 null를 반환한다.")
    @Test
    fun findByGoalEntityAndUserEntity2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, SUCCESS)
        val goalEntity3 = createGoalEntity(userEntity2, FREE, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val gifticonEntity1 = GifticonEntity(userEntity = userEntity1, url = URL("URL1"))
        val gifticonEntity2 = GifticonEntity(userEntity = userEntity1, url = URL("URL2"))
        gifticonRepository.saveAll(listOf(gifticonEntity1, gifticonEntity2))

        val winnerEntity1 = WinnerEntity(goalEntity1, userEntity1, gifticonEntity1)
        val winnerEntity2 = WinnerEntity(goalEntity2, userEntity2, gifticonEntity2)
        winnerRepository.saveAll(listOf(winnerEntity1, winnerEntity2))

        // when
        val foundWinner = winnerRepository.findByGoalEntityAndUserEntity(goalEntity3, userEntity2)

        // then
        assertThat(foundWinner)
            .isNull()
    }

    @DisplayName("goalEntity와 userEntity를 입력받아 Winner를 조회하는 중 해당하는 Winner를 찾을 수 없으면 null를 반환한다.")
    @Test
    fun findByGoalEntityAndUserEntity3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        val userEntity3 = UserEntity(nickname = Nickname("User3"))
        userRepository.saveAll(listOf(userEntity1, userEntity2, userEntity3))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, FAIL)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, SUCCESS)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val gifticonEntity1 = GifticonEntity(userEntity = userEntity1, url = URL("URL1"))
        val gifticonEntity2 = GifticonEntity(userEntity = userEntity1, url = URL("URL2"))
        gifticonRepository.saveAll(listOf(gifticonEntity1, gifticonEntity2))

        val winnerEntity1 = WinnerEntity(goalEntity1, userEntity1, gifticonEntity1)
        val winnerEntity2 = WinnerEntity(goalEntity2, userEntity2, gifticonEntity2)
        winnerRepository.saveAll(listOf(winnerEntity1, winnerEntity2))

        // when
        val foundWinner = winnerRepository.findByGoalEntityAndUserEntity(goalEntity2, userEntity3)

        // then
        assertThat(foundWinner)
            .isNull()
    }

    private fun createGoalEntity(
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
}