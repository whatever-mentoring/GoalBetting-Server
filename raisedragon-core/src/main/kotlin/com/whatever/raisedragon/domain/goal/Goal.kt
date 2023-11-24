package com.whatever.raisedragon.domain.goal

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.*

@Table(name = "goal")
@Entity
class Goal(

    @Embedded
    @Column(name = "content", nullable = true, length = 255)
    val content: Content,

    @Enumerated(EnumType.STRING)
    val type: BettingType,

    @Embedded
    @Column(name = "threshold", nullable = false)
    val threshold: Threshold = Threshold(0)

) : BaseEntity()

class Content(
    private var value: String
) {

    private fun validate() {
        if (this.value.isBlank()) {
            throw IllegalArgumentException()
        }
    }

    private fun update(newContent: String) {
        this.value = newContent
    }

}

enum class BettingType {
    FREE, BILLING
}

class Threshold(
    private var value: Int
) {

    // 7일간의 목표를 달성한 경우
    private fun isAchieved(): Boolean {
        return this.value == 7
    }

    // 목표 진행률 +1
    private fun increaseValue() {
        this.value++
    }

}