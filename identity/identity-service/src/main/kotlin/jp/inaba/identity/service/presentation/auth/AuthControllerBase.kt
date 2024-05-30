package jp.inaba.identity.service.presentation.auth

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Auth")
@RequestMapping("/api/auth")
abstract class AuthControllerBase
