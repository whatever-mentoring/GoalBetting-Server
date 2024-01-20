package com.whatever.raisedragon.domain.gifticon

import com.whatever.raisedragon.common.exception.BaseException
import com.whatever.raisedragon.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Transactional(readOnly = true)
@Service
class GifticonService(
    @Qualifier("notFoundExceptionSupplier") private val notFoundExceptionSupplier: Supplier<BaseException>,
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
                userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier),
                url = URL(url)
            )
        )
        return gifticon.toDto()
    }

    fun findById(gifticonId: Long): Gifticon {
        return gifticonRepository.findById(gifticonId).orElseThrow(notFoundExceptionSupplier).toDto()
    }

    @Transactional
    fun hardDeleteByUserId(userId: Long) {
        val gifticonEntities = gifticonRepository.findAllByUserEntity(
            userEntity = userRepository.findById(userId).orElseThrow(notFoundExceptionSupplier)
        )
        gifticonRepository.deleteAll(gifticonEntities)
    }
}