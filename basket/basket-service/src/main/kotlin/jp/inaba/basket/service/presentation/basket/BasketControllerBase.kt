package jp.inaba.basket.service.presentation.basket

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Basket")
@RequestMapping("/api/baskets")
abstract class BasketControllerBase