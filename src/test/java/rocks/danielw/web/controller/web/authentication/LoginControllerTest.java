package rocks.danielw.web.controller.web.authentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.security.WebSecurity;
import rocks.danielw.service.user.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
@Import(WebSecurity.class)
public class LoginControllerTest {

    @Autowired private MockMvc               mockMvc;
    @MockBean  private UserService           userService;
    @MockBean  private BCryptPasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        // do nothing
    }

    @After
    public void tearDown() {
        // do nothing
    }

    @Test
    public void given_login_uri_should_return_login_view() throws Exception {
        mockMvc.perform(get(Endpoints.LOGIN))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewNames.LOGIN))
                .andExpect(content().string(containsString("Login")));
    }
}