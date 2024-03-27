package com.whatever.raisedragon.domain.goalproof

import com.whatever.raisedragon.IntegrationTestSupport
import com.whatever.raisedragon.domain.gifticon.URL
import com.whatever.raisedragon.domain.goal.*
import com.whatever.raisedragon.domain.goal.GoalResult.*
import com.whatever.raisedragon.domain.goal.GoalType.*
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.UserEntity
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.utils.anyNotFoundException
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.ObjectAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@Transactional
class GoalProofServiceTest : IntegrationTestSupport {
    @Autowired
    private lateinit var goalProofService: GoalProofService

    @Autowired
    private lateinit var goalRepository: GoalRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var goalProofRepository: GoalProofRepository

    @DisplayName("userId, goalId, url, comment를 입력받아 GoalProof를 생성한다.")
    @Test
    fun create1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("url")
        val comment = Comment("Sample comment")

        // when
        val foundGoalProof = goalProofService.create(
            userId = userEntity.id,
            goalId = goalEntity.id,
            url = url,
            comment = comment
        )

        // then
        assertThat(foundGoalProof)
            .isInstanceOfGoalProof()
            .extracting("userId", "goalId", "url", "comment")
            .contains(userEntity.id, goalEntity.id, url, comment)
    }

    @DisplayName("GoalProof 생성 시 존재하지 않는 userId를 입력하면 예외가 발생한다.")
    @Test
    fun create2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("url")
        val comment = Comment("Sample comment")

        // when // then
        assertThatThrownBy {
            goalProofService.create(
                userId = -1L,
                goalId = goalEntity.id,
                url = url,
                comment = comment
            )
        }.anyNotFoundException()
    }

    @DisplayName("GoalProof 생성 시 존재하지 않는 goalId를 입력하면 예외가 발생한다.")
    @Test
    fun create3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("url")
        val comment = Comment("Sample comment")

        // when // then
        assertThatThrownBy {
            goalProofService.create(
                userId = userEntity.id,
                goalId = -1L,
                url = url,
                comment = comment
            )
        }.anyNotFoundException()
    }

    @DisplayName("요청받은 id에 해당하는 GoalProof를 조회한다.")
    @Test
    fun findById1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("url")
        val comment = Comment("Sample comment")
        val goalProofEntity = GoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            url = url,
            comment = comment
        )
        goalProofRepository.save(goalProofEntity)

        // when
        val foundGoalProof = goalProofService.findById(goalProofEntity.id)

        // then
        assertThat(foundGoalProof)
            .isInstanceOfGoalProof()
            .extracting("userId", "goalId", "url", "comment")
            .contains(userEntity.id, goalEntity.id, url, comment)
    }

    @DisplayName("특정 id의 GoalProof를 조회하는 중 해당하는 GoalProof가 없는 경우 예외가 발생한다.")
    @Test
    fun findById2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("url")
        val comment = Comment("Sample comment")
        val goalProofEntity = GoalProofEntity(
            userEntity = userEntity,
            goalEntity = goalEntity,
            url = url,
            comment = comment
        )
        goalProofRepository.save(goalProofEntity)

        // when // then
        assertThatThrownBy { goalProofService.findById(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("요청받은 goalId와 userId를 갖는 모든 GoalProof를 조회한다.")
    @Test
    fun findAllByGoalIdAndUserId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when
        val foundGoalProofList = goalProofService.findAllByGoalIdAndUserId(
            goalId = goalEntity1.id,
            userId = userEntity1.id
        )

        // then
        assertThat(foundGoalProofList)
            .hasSize(2)
            .extracting("userId", "goalId", "url", "comment")
            .containsExactlyInAnyOrder(
                tuple(userEntity1.id, goalEntity1.id, URL("url1"), Comment("comment1")),
                tuple(userEntity1.id, goalEntity1.id, URL("url5"), Comment("comment5"))
            )
    }

    @DisplayName("요청받은 goalId와 userId를 갖는 모든 GoalProof를 조회할 때 해당하는 GoalProof가 없으면 EmptyList를 반환한다.")
    @Test
    fun findAllByGoalIdAndUserId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when
        val foundGoalProofList = goalProofService.findAllByGoalIdAndUserId(
            goalId = goalEntity3.id,
            userId = userEntity1.id
        )

        // then
        assertThat(foundGoalProofList)
            .isEmpty()
    }

    @DisplayName("요청받은 goalId와 userId를 갖는 모든 GoalProof를 조회할 때 해당하는 goalId를 조회할 수 없는 경우 예외가 발생한다.")
    @Test
    fun findAllByGoalIdAndUserId3() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when // then
        assertThatThrownBy { goalProofService.findAllByGoalIdAndUserId(goalId = -1L, userId = userEntity1.id) }
            .anyNotFoundException()
    }

    @DisplayName("요청받은 goalId와 userId를 갖는 모든 GoalProof를 조회할 때 userId 해당하는 User를 조회할 수 없는 경우 예외가 발생한다.")
    @Test
    fun findAllByGoalIdAndUserId4() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when // then
        assertThatThrownBy { goalProofService.findAllByGoalIdAndUserId(goalId = goalEntity1.id, userId = -1L) }
            .anyNotFoundException()
    }

    @DisplayName("요청한 goalId를 갖는 GoalProof의 개수를 반환한다.")
    @Test
    fun countAllByGoalId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when
        val count = goalProofService.countAllByGoalId(goalEntity1.id)

        // then
        assertThat(count).isEqualTo(3)
    }

    @DisplayName("요청한 goalId를 갖는 GoalProof의 개수를 반환할 때 goalId에 해당하는 Goal이 없는 경우 예외가 발생한다.")
    @Test
    fun countAllByGoalId2() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity1 = createGoalEntity(userEntity1, FREE, PROCEEDING)
        val goalEntity2 = createGoalEntity(userEntity2, BILLING, FAIL)
        val goalEntity3 = createGoalEntity(userEntity2, BILLING, FAIL)
        goalRepository.saveAll(listOf(goalEntity1, goalEntity2, goalEntity3))

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity1, URL("url1"), Comment("comment1"))
        val goalProofEntity2 = createGoalProofEntity(userEntity1, goalEntity2, URL("url2"), Comment("comment2"))
        val goalProofEntity3 = createGoalProofEntity(userEntity2, goalEntity1, URL("url3"), Comment("comment3"))
        val goalProofEntity4 = createGoalProofEntity(userEntity2, goalEntity2, URL("url4"), Comment("comment4"))
        val goalProofEntity5 = createGoalProofEntity(userEntity1, goalEntity1, URL("url5"), Comment("comment5"))
        goalProofRepository.saveAll(
            listOf(
                goalProofEntity1,
                goalProofEntity2,
                goalProofEntity3,
                goalProofEntity4,
                goalProofEntity5
            )
        )

        // when // then
        assertThatThrownBy { goalProofService.countAllByGoalId(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("GoalProof의 id, url, comment를 입력받아 url 및 comment가 null이 아닌경우 수정한다.")
    @Test
    fun update1() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val goalProofEntity = createGoalProofEntity(userEntity, goalEntity)
        goalProofRepository.save(goalProofEntity)

        val toBeURL = URL("modified url")
        val toBeComment = Comment("modified comment")

        // when
        val foundGoalProof = goalProofService.update(goalProofEntity.id, toBeURL, toBeComment)

        // then
        assertThat(foundGoalProof)
            .isInstanceOfGoalProof()
            .extracting("userId", "goalId", "url", "comment")
            .contains(userEntity.id, goalEntity.id, toBeURL, toBeComment)
    }

    @DisplayName("GoalProof의 id, url, comment를 입력받아 url 및 comment가 null이면 수정하지 않는다.")
    @Test
    fun update2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("URL")
        val comment = Comment("Comment")
        val goalProofEntity = createGoalProofEntity(userEntity, goalEntity, url, comment)
        goalProofRepository.save(goalProofEntity)

        // when
        val foundGoalProof = goalProofService.update(goalProofEntity.id)

        // then
        assertThat(foundGoalProof)
            .isInstanceOfGoalProof()
            .extracting("userId", "goalId", "url", "comment")
            .contains(userEntity.id, goalEntity.id, url, comment)
    }

    @DisplayName("GoalProof 수정 시 요청받은 id의 GoalProof를 조회하지 못한 경우 예외가 발생한다.")
    @Test
    fun update3() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("URL")
        val comment = Comment("Comment")
        val goalProofEntity = createGoalProofEntity(userEntity, goalEntity, url, comment)
        goalProofRepository.save(goalProofEntity)

        // when // then
        assertThatThrownBy { goalProofService.update(-1L) }
            .anyNotFoundException()
    }

    @DisplayName("testId를 포함하는 모든 GoalProof를 hard-delete 한다.")
    @Test
    fun hardDeleteByUserId1() {
        // given
        val userEntity1 = UserEntity(nickname = Nickname("User1"))
        val userEntity2 = UserEntity(nickname = Nickname("User2"))
        userRepository.saveAll(listOf(userEntity1, userEntity2))

        val goalEntity = createGoalEntity(userEntity1, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val goalProofEntity1 = createGoalProofEntity(userEntity1, goalEntity)
        val goalProofEntity2 = createGoalProofEntity(userEntity2, goalEntity)
        val goalProofEntity3 = createGoalProofEntity(userEntity1, goalEntity)

        goalProofRepository.saveAll(listOf(goalProofEntity1, goalProofEntity2, goalProofEntity3))

        // when
        goalProofService.hardDeleteByUserId(userEntity1.id)

        // then
        assertThat(
            goalProofRepository.findByIdOrNull(goalProofEntity1.id)
        ).isNull()
        assertThat(
            goalProofRepository.findByIdOrNull(goalProofEntity3.id)
        ).isNull()
    }

    @DisplayName("GoalProof를 hard-delete 하는 중 요청받은 userId의 User를 조회할 수 없는 경우 예외가 발생한다.")
    @Test
    fun hardDeleteByUserId2() {
        // given
        val userEntity = UserEntity(nickname = Nickname("User"))
        userRepository.save(userEntity)

        val goalEntity = createGoalEntity(userEntity, FREE, PROCEEDING)
        goalRepository.save(goalEntity)

        val url = URL("URL")
        val comment = Comment("Comment")
        val goalProofEntity = createGoalProofEntity(userEntity, goalEntity, url, comment)
        goalProofRepository.save(goalProofEntity)

        // when // then
        assertThatThrownBy { goalProofService.hardDeleteByUserId(-1L) }
            .anyNotFoundException()
    }

    private fun ObjectAssert<*>.isInstanceOfGoalProof() = isInstanceOf(GoalProof::class.java)

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

    private fun createGoalProofEntity(
        userEntity: UserEntity,
        goalEntity: GoalEntity,
        url: URL = URL("sample url"),
        comment: Comment = Comment("sample comment"),
        createdAt: LocalDateTime? = null
    ) = GoalProofEntity(
        userEntity = userEntity,
        goalEntity = goalEntity,
        url = url,
        comment = comment
    ).also { goalProof -> createdAt?.let { goalProof.createdAt = it } }
}