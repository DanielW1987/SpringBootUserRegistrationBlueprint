package rocks.danielw.web.controller.web.authentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.model.exceptions.EmailVerificationTokenExpiredException;
import rocks.danielw.model.exceptions.ResourceNotFoundException;
import rocks.danielw.security.WebSecurity;
import rocks.danielw.service.token.TokenService;
import rocks.danielw.service.user.UserService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
@Import(WebSecurity.class)
public class RegistrationControllerTest {

    private static final String NOT_EXISTING_TOKEN = "not_existing_token";
    private static final String EXPIRED_TOKEN      = "expired_token";

    private MockMvc mockMvc;

    // Dependencies for WebSecurity.class
    @MockBean  private UserService securityUserService;
    @MockBean  private TokenService securitTokenService;
    @MockBean  private BCryptPasswordEncoder passwordEncoder;

    // Dependencies for RegistrationController.class
    @Mock private UserService userService;
    @Mock private TokenService tokenService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private MessageSource messages;

    @Before
    public void setUp() {
        userService = mock(UserService.class);
        doThrow(ResourceNotFoundException.class).when(userService).verifyEmailToken(NOT_EXISTING_TOKEN);
        doThrow(EmailVerificationTokenExpiredException.class).when(userService).verifyEmailToken(EXPIRED_TOKEN);

        tokenService = mock(TokenService.class);
        doThrow(ResourceNotFoundException.class).when(tokenService).resendExpiredEmailVerificationToken(NOT_EXISTING_TOKEN);

        eventPublisher = mock(ApplicationEventPublisher.class);
        messages       = mock(MessageSource.class);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/resources/templates/");
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(new RegistrationController(userService, tokenService, eventPublisher, messages))
                                 .setViewResolvers(viewResolver)
                                 .build();
    }

    @After
    public void tearDown() {
        // do nothing
    }

    @Test
    public void given_register_uri_should_return_register_view() throws Exception {
        mockMvc.perform(get(Endpoints.REGISTER))
               .andExpect(status().isOk())
               .andExpect(view().name(ViewNames.REGISTER));
    }

    @Test
    public void given_valid_token_on_registration_verification_uri_should_redirect_to_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.REGISTRATION_VERIFICATION + "?token=valid_token"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:" + Endpoints.LOGIN + "?verificationSuccess"));
    }

    @Test
    public void given_not_existing_token_on_registration_verification_uri_should_redirect_to_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.REGISTRATION_VERIFICATION + "?token=" + NOT_EXISTING_TOKEN))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:" + Endpoints.LOGIN + "?tokenNotFound"));
    }

    @Test
    public void given_expired_token_on_registration_verification_uri_should_redirect_to_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.REGISTRATION_VERIFICATION + "?token=" + EXPIRED_TOKEN))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:" + Endpoints.LOGIN + "?tokenExpired&token=" + EXPIRED_TOKEN));
    }

    @Test
    public void given_valid_token_on_resend_verification_token_uri_should_redirect_to_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.RESEND_VERIFICATION_TOKEN + "?token=valid-token"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:" + Endpoints.LOGIN + "?resendVerificationTokenSuccess"));
    }

    @Test
    public void given__not_existing_token_on_resend_verification_token_uri_should_redirect_to_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.RESEND_VERIFICATION_TOKEN + "?token=" + NOT_EXISTING_TOKEN))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:" + Endpoints.LOGIN + "?tokenNotFound"));
    }

}