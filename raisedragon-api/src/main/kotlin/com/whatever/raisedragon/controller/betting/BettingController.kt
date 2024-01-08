package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.applicationservice.betting.BettingApplicationService
import com.whatever.raisedragon.applicationservice.betting.dto.BettingCreateUpdateResponse
import com.whatever.raisedragon.applicationservice.betting.dto.BettingRetrieveResponse
import com.whatever.raisedragon.common.Response
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Betting create API",
        description = "Create Betting",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PostMapping
    fun create(
        @Valid @RequestBody bettingCreateRequest: BettingCreateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<BettingCreateUpdateResponse> {
        return Response.success(
            bettingApplicationService.create(bettingCreateRequest.toServiceRequest(userInfo.id))
        )
    }

    @Operation(summary = "Betting retrieve API", description = "Retrieve Betting")
    @GetMapping("/{bettingId}")
    fun retrieve(
        @PathVariable bettingId: Long
    ): Response<BettingRetrieveResponse> {
        return Response.success(bettingApplicationService.retrieve(bettingId))
    }

    @Operation(
        summary = "Betting Update API",
        description = "베팅 내역을 수정합니다",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @PutMapping
    fun update(
        @Valid @RequestBody request: BettingUpdateRequest,
        @GetAuth userInfo: UserInfo
    ): Response<BettingRetrieveResponse> {
        return Response.success(
            bettingApplicationService.update(request.toServiceRequest(userInfo.id))
        )
    }

    @Operation(
        summary = "Betting Delete API",
        description = "베팅을 삭제합니다",
        security = [SecurityRequirement(name = "Authorization")]
    )
    @DeleteMapping("{bettingId}")
    fun delete(
        @PathVariable bettingId: Long,
        @GetAuth userInfo: UserInfo
    ): Response<Unit> {
        bettingApplicationService.delete(userInfo.id, bettingId)
        return Response.success()
    }
}