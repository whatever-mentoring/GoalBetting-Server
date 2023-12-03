package com.whatever.raisedragon.controller.user

import com.whatever.raisedragon.applicationservice.UserApplicationService
import com.whatever.raisedragon.common.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "User", description = "User API")
@RestController
@RequestMapping("/v1/user")
class UserController(
    private val userApplicationService: UserApplicationService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "User create API", description = "Create User")
    @PostMapping
    fun create(
        @Valid @RequestBody userCreateRequest: UserCreateRequest
    ): Response<Any> {
        return Response.success(userApplicationService.create())
    }

    @Operation(summary = "User retrieve API", description = "Retrieve User")
    @GetMapping
    fun retrieve(): Response<UserRetrieveResponse> {
        return Response.success(userApplicationService.retrieve())
    }

    @Operation(summary = "User update API", description = "Update User")
    @PutMapping
    fun update(
        @RequestBody userUpdateRequest: UserUpdateRequest
    ): Response<UserCreateUpdateResponse> {
        return Response.success(userApplicationService.create())
    }

    @Operation(summary = "User delete API", description = "Delete User")
    @DeleteMapping
    fun delete(): Response<Unit> {
        return Response.success()
    }
}