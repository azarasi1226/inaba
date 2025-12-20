package jp.inaba.apigateway.presentation.admin.createtestdata

import jp.inaba.apigateway.application.admin.createtestdata.CreateTestDataInteractor
import jp.inaba.apigateway.presentation.admin.AdminController
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("local", "test")
class CreateTestDataController(
    val interactor: CreateTestDataInteractor,
) : AdminController {
    @PostMapping("/create-test-data")
    fun handle() {
        interactor.handle()
    }
}
