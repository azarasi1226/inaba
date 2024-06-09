package jp.inaba.identity.service.presentation.user

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "User")
@RequestMapping("/api/user")
interface UserController
