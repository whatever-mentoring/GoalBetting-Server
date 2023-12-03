package com.whatever.raisedragon.domain.gifticon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GifticonRepository : JpaRepository<Gifticon, Long>