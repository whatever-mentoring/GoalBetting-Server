package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.applicationservice.BettingApplicationService
import com.whatever.raisedragon.common.Response
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

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Betting create API", description = "Create Betting")
    @PostMapping
    fun create(
        @Valid @RequestBody bettingCreateRequest: BettingCreateRequest
    ): Response<BettingCreateUpdateResponse> {
        return Response.success(bettingApplicationService.create())
    }

    @Operation(summary = "Betting retrieve API", description = "Retrieve Betting")
    @GetMapping("/{bettingId}")
    fun retrieve(
        @PathVariable bettingId: Long
    ): Response<BettingRetrieveResponse> {
        return Response.success(bettingApplicationService.retrieve())
    }

    @Operation(summary = "Betting update API", description = "Update Betting")
    @PutMapping
    fun update(
        @RequestBody bettingUpdateRequest: BettingUpdateRequest
    ): Response<BettingCreateUpdateResponse> {
        return Response.success(bettingApplicationService.create())
    }

    @Operation(summary = "Betting delete API", description = "Delete Betting")
    @DeleteMapping("{bettingId}")
    fun delete(
        @PathVariable bettingId: Long
    ): Response<Any> {
        return Response.success()
    }
}