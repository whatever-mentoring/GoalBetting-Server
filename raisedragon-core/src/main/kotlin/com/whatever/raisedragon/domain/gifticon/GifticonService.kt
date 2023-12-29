package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.domain.user.UserRepository
import com.whatever.raisedragon.domain.user.fromDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class GifticonService(
    private val gifticonRepository: GifticonRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun create(
        userId: Long,
        url: String,
    ): Gifticon {
        val gifticon = gifticonRepository.save(
            GifticonEntity(
                userEntity = userRepository.findById(userId).get(),
                url = URL(url)
            )
        )
        return gifticon.toDto()
    }

    fun loadById(gifticonId: Long): Gifticon {
        return gifticonRepository.findById(gifticonId).get().toDto()
    }

    @Transactional
    fun hardDelete(user: User) {
        val gifticons = gifticonRepository.findAllByUserEntity(user.fromDto())
        gifticonRepository.deleteAll(gifticons)
    }
}