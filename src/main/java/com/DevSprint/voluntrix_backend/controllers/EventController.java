package com.DevSprint.voluntrix_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/public/v1/events")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

}
