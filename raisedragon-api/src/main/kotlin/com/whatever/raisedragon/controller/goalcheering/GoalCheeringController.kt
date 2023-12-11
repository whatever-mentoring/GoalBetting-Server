package com.whatever.raisedragon.controller.goalcheering

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Goal-Cheering", description = "Goal-Cheering API")
@RestController
@RequestMapping("/v1/cheering")
class GoalCheeringController