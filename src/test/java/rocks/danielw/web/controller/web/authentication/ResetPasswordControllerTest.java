package rocks.danielw.web.controller.web.authentication;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import rocks.danielw.security.WebSecurity;

@RunWith(SpringRunner.class)
@WebMvcTest(ResetPasswordController.class)
@Import(WebSecurity.class)
public class ResetPasswordControllerTest {

  // ToDo DanielW: Implement this

}