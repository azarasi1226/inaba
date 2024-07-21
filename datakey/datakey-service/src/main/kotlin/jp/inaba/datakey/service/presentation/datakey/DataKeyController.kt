package jp.inaba.datakey.service.jp.inaba.datakey.service.presentation.datakey

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "DataKeys")
@RequestMapping("/api/datakeys")
interface DataKeyController
