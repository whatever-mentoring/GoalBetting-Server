package com.whatever.raisedragon.controller.goalgifticon

import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Goal-Gifticon", description = "Goal-Gifticon API")
@RestController
@RequestMapping("/v1/goal")
class GoalGifticonController {

    @Operation(summary = "Registering GoalGifticon API", description = "Register GoalProof")
    @PostMapping("/{goalId}/gifticon")
    fun register(
        @PathVariable goalId: Long,
        @RequestBody request: RegisterGoalGifticonRequest
    ): Response<Unit> {
        return Response.success()
    }

}