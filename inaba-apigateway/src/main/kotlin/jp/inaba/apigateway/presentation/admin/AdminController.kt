package jp.inaba.apigateway.presentation.admin

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Admin")
@RequestMapping("/api/admin")
interface AdminController
