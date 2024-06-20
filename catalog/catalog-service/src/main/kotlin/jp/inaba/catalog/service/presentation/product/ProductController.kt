package jp.inaba.catalog.service.presentation.product

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Products")
@RequestMapping("/api/products")
interface ProductController
