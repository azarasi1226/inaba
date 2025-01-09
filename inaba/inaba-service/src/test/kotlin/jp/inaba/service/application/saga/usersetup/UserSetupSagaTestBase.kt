package jp.inaba.service.application.saga.usersetup

//abstract class UserSetupSagaTestBase {
//    var fixture = SagaTestFixture(UserSetupSaga::class.java)
//
//    @Mock
//    private lateinit var userIdFactory: UserIdFactory
//
//    val userId = UserId()
//    val emailAddress = "azarasikazuki@gmail.com"
//    val confirmCode = "A0001"
//
//    val signupConfirmed =
//        SignupConfirmedEvent(
//            emailAddress = emailAddress,
//            confirmCode = confirmCode,
//        )
//
//    @BeforeEach
//    fun before() {
//        MockitoAnnotations.openMocks(this)
//        Mockito.`when`(userIdFactory.handle())
//            .thenReturn(userId)
//
//        fixture.withTransienceCheckDisabled()
//            .registerResource(userIdFactory)
//    }
//}
