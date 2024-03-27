package com.whatever.raisedragon

import com.fasterxml.jackson.databind.ObjectMapper
import com.whatever.raisedragon.applicationservice.betting.BettingApplicationService
import com.whatever.raisedragon.controller.betting.BettingController
import com.whatever.raisedragon.domain.user.Nickname
import com.whatever.raisedragon.domain.user.User
import com.whatever.raisedragon.security.authentication.UserInfo
import com.whatever.raisedragon.security.jwt.JwtAgent
import com.whatever.raisedragon.security.jwt.JwtToken
import com.whatever.raisedragon.security.resolver.UserInfoArgumentResolver
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import org.mockito.Spy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    controllers = [
        BettingController::class
    ]
)
@ActiveProfiles("test")
abstract class ControllerTestSupport {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    protected lateinit var bettingApplicationService: BettingApplicationService
}