package jp.inaba.identity.service.application.saga.usersetup

import jp.inaba.identity.api.domain.external.auth.AuthEvents
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserIdFactory
import org.axonframework.test.saga.SagaTestFixture
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

abstract class UserSetupSagaTestBase {
    var fixture = SagaTestFixture(UserSetupSaga::class.java)

    @Mock
    private lateinit var userIdFactory: UserIdFactory

    val userId = UserId()
    val emailAddress = "azarasikazuki@gmail.com"
    val confirmCode = "A0001"

    val signupConfirmed = AuthEvents.SignupConfirmed(
        emailAddress = emailAddress,
        confirmCode = confirmCode
    )

    @BeforeEach
    fun before() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(userIdFactory.handle())
            .thenReturn(userId)

        fixture.withTransienceCheckDisabled()
            .registerResource(userIdFactory)
    }
}