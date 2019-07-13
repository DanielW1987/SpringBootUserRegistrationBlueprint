package rocks.danielw.web.controller.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.config.constants.ViewNames;
import rocks.danielw.security.WebSecurity;
import rocks.danielw.service.user.UserService;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
@Import(WebSecurity.class)
class IndexControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private BCryptPasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    // do nothing
  }

  @AfterEach
  void tearDown() {
    // do nothing
  }

  @Test
  void given_index_uri_then_return_index_view() throws Exception {
    mockMvc.perform(get(Endpoints.SLASH_INDEX))
            .andExpect(status().isOk())
            .andExpect(view().name(ViewNames.INDEX))
            .andExpect(content().string(containsString("Welcome")));
  }
}
