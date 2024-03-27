package com.whatever.raisedragon.controller.betting

import com.whatever.raisedragon.ControllerTestSupport
import com.whatever.raisedragon.domain.betting.BettingPredictionType
import com.whatever.raisedragon.security.WithCustomUser
import org.assertj.core.api.Assertions.*
import org.hamcrest.core.IsNull.notNullValue
import org.hamcrest.core.IsNull.nullValue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WithCustomUser(id = 1L, nickname = "User")
class BettingControllerTest : ControllerTestSupport() {

    @DisplayName("Betting을 생성한다.")
    @Test
    fun create1() {
        // given
        val request = BettingCreateRequest(goalId = 1L, predictionType = BettingPredictionType.FAIL)

        // when // then
        mockMvc
            .perform(
                post("/v1/betting")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(::print)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.errorResponse").value(nullValue()))
    }

    @DisplayName("Betting을 생성할 때 goalId는 양수이다.")
    @Test
    fun create2() {
        // given
        val request = BettingCreateRequest(goalId = -1, predictionType = BettingPredictionType.FAIL)

        // when // then
        mockMvc
            .perform(
                post("/v1/betting")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(::print)
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.isSuccess").value(false))
            .andExpect(jsonPath("$.errorResponse").value(notNullValue()))
            .andExpect(jsonPath("$.errorResponse.detailMessage").value("올바른 goalId를 입력해야합니다."))
            .andExpect(jsonPath("$.data").isEmpty)
    }

    @DisplayName("단건 베팅을 조회합니다.")
    @Test
    fun retrieve() {
        // given
        val bettingId = 1L

        // when // then
        mockMvc
            .perform(
                get("/v1/betting/$bettingId")
            )
            .andDo(::print)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.errorResponse").value(nullValue()))
    }

    @DisplayName("베팅을 업데이트 합니다.")
    @Test
    fun update() {
        // given
        val request = BettingUpdateRequest(bettingId = 1L, predictionType = BettingPredictionType.SUCCESS)

        // when // then
        mockMvc
            .perform(
                put("/v1/betting")
                    .with(csrf())
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(::print)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andExpect(jsonPath("$.errorResponse").value(nullValue()))
    }
}