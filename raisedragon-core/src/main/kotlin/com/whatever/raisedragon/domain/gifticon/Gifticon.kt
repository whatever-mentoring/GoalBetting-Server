package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "gifticon")
@Entity
class Gifticon(

    @Embedded
    @Column(name = "url", nullable = true, length = 255)
    private val url: URL,

    @Column(name = "is_validated")
    private var isValidated: Boolean = true

) : BaseEntity()

class URL {
    private var value = ""

    // 유효한 URL 인지 검증
    fun validate() {

    }

    fun updateURL(newURL: String) {
        this.value = newURL
    }

    // 정적 저장소에 업로드 한 후 해당 URL을 반영할 메서드
    fun generatedURL() {

    }
}

