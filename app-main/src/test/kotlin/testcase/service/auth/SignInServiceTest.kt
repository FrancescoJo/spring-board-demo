/*
 * spring-message-board-demo
 * Refer to LICENCE.txt for licence details.
 */
package testcase.service.auth

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Jul - 2020
 */
class SignInServiceTest {
//    private lateinit var sut: SignUpService
//
//    @BeforeEach
//    internal fun setup() {
//        MockitoAnnotations.initMocks(this)
//
//        this.sut = SignInServiceImpl()
//    }
//
//    @Test
//    fun `an attempt without nonexistent loginName causes LoginNotAllowedException`() {
//        // given:
//        val request = AuthenticationRequestBuilder.createRandom()
//        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)
//
//        // when:
//        `when`(authRepo.findByLoginName(anyString())).thenReturn(null)
//
//        // then:
//        assertThrows<LoginNotAllowedException> {
//            sut.signIn(request, httpReq, utcNow())
//        }
//    }
//
//    @Test
//    fun `an attempt with wrong password causes LoginNotAllowedException`() {
//        // given:
//        val request = AuthenticationRequestBuilder.createRandom()
//        val mockAuthentication = AuthenticationBuilder(request.toAuthentication())
//                        .password("")
//                        .build()
//
//        // expect:
//        assertThrows<LoginNotAllowedException> {
//            sut.signIn(request, httpReq, utcNow())
//        }
//    }
//
//    @Test
//    fun `successful signIn attempt updates old Authentication and backed as AuthenticationResult`() {
//        // given:
//        val request = AuthenticationRequestBuilder.createRandom()
//        val mockAuthentication = AuthenticationBuilder(request.toAuthentication())
//        val timestamp = utcNow()
//
//        // when:
//        val result = sut.signIn(request, httpReq, timestamp)
//
//        // then:
//        verify(authRepo, times(1)).save(any<Authentication>())
//
//        verify(mockAuthentication, times(1)).lastActiveDate = timestamp
//    }

//    private fun prepareSignIn(request: AuthRequest, savedAuth: Authentication) {
//        // given:
//        val httpReq: HttpServletRequest = mock(HttpServletRequest::class.java)
//        val mockAccessToken = getRandomAlphaNumericString(128)
//        val tokenLifespanSecs = AppAuthProperties.DEFAULT_AUTH_TOKEN_ALIVE_SECS
//        val refreshTokenLifespanDays = AppAuthProperties.DEFAULT_REFRESH_TOKEN_ALIVE_DAYS
//
//        // when:
//        `when`(httpReq.remoteAddr).thenReturn("localhost")
//        `when`(authTokenMgr.create(anyString(), anyString(), any()))
//            .thenReturn(
//                FreshHttpAuthorizationToken(mockAccessToken, utcNow().plusSeconds(tokenLifespanSecs))
//            )
//        `when`(authProps.authTokenAliveSecs).thenReturn(tokenLifespanSecs)
//        `when`(authProps.refreshTokenAliveDays).thenReturn(refreshTokenLifespanDays)
//        `when`(authRepo.findByLoginName(anyString())).thenReturn(mockAuthentication)
//    }
//
//    private fun AuthenticationRequest.toAuthentication() {
//        return AuthenticationBuilder(AuthenticationBuilder.createRandom())
//            .loginName(loginName)
//            .password(password.value)
//            .platformType(platformType)
//            .platformVersion(platformVersion)
//            .appVersion(appVersion)
//            .build()
//    }
}
