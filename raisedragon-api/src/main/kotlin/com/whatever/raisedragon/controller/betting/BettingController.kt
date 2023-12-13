package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.applicationservice.BettingApplicationService
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.common.aop.Auth
import com.whatever.raisedragon.common.aop.AuthContext
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "Betting", description = "Betting API")
@RestController
@RequestMapping("/v1/betting")
class BettingController(
    private val bettingApplicationService: BettingApplicationService
) {

    @Auth
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Betting create API", description = "Create Betting")
    @PostMapping
    fun create(
        @Valid @RequestBody bettingCreateRequest: BettingCreateRequest,
        // @GetAuth userInfo: UserInfo
    ): Response<BettingCreateUpdateResponse> {
        return Response.success(
            bettingApplicationService.create(
                userId = AuthContext.getUser().id!!,
                goalId = bettingCreateRequest.goalId,
                predictionType = bettingCreateRequest.predictionType
            )
        )
    }

    @Auth
    @Operation(summary = "Betting retrieve API", description = "Retrieve Betting")
    @GetMapping("/{bettingId}")
    fun retrieve(
        @PathVariable bettingId: Long
    ): Response<BettingRetrieveResponse> {
        return Response.success(bettingApplicationService.retrieve(bettingId))
    }

    @Auth
    @Operation(summary = "Betting Update API", description = "베팅 내역을 수정합니다")
    @PutMapping
    fun update(
        @Valid @RequestBody request: BettingUpdateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<BettingRetrieveResponse> {
        return Response.success(
            bettingApplicationService.update(
                userId = AuthContext.getUser().id!!,
                bettingId = request.bettingId,
                predictionType = request.predictionType
            )
        )
    }

    @Auth
    @Operation(summary = "Betting Delete API", description = "베팅을 삭제합니다")
    @DeleteMapping("{bettingId}")
    fun delete(
        @PathVariable bettingId: Long,
        @GetAuth userInfo: UserInfo
    ): Response<Unit> {
        bettingApplicationService.delete(AuthContext.getUser().id!!, bettingId)
        return Response.success()
    }
}