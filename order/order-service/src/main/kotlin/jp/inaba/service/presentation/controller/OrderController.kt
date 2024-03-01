package jp.inaba.service.presentation.controller

import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun issueOrder() {

    }

    @GetMapping
    fun getOrder() {

    }
}