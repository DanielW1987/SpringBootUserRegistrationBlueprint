package rocks.danielw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import rocks.danielw.config.constants.Endpoints;
import rocks.danielw.security.handler.CustomAuthenticationFailureHandler;
import rocks.danielw.security.handler.CustomLogoutSuccessHandler;
import rocks.danielw.service.user.UserService;

@EnableWebSecurity
@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // BEGIN UNRESTRICTED ACCESS TO ALL ENDPOINTS - DO NOT MERGE INTO MASTER
//    http.authorizeRequests()
//            .anyRequest()
//            .permitAll();
    // END UNRESTRICTED ACCESS TO ALL ENDPOINTS - DO NOT MERGE INTO MASTER

    http
      .csrf().disable()// todo danielw: enable later, do logout within a POST
      .authorizeRequests()
        // .antMatchers(Endpoints.AntPattern.ADMIN).hasRole("ROLE_ADMIN")
        .antMatchers(Endpoints.AntPattern.RESOURCES).permitAll()
        .antMatchers(Endpoints.AntPattern.INDEX).permitAll()
        .antMatchers(Endpoints.AntPattern.LOGIN).permitAll()
        .antMatchers(Endpoints.AntPattern.REGISTER).permitAll()
        .antMatchers(Endpoints.AntPattern.REGISTRATION_VERIFICATION).permitAll()
        .antMatchers(Endpoints.AntPattern.RESEND_VERIFICATION_TOKEN).permitAll()
        .antMatchers(Endpoints.AntPattern.FORGOT_PASSWORD).permitAll()
        .antMatchers(Endpoints.AntPattern.RESET_PASSWORD).permitAll()
        .antMatchers(Endpoints.REST.USERS_REST_V1).permitAll() // todo danielw: disallow this later
        .anyRequest().authenticated()
      .and()
      .formLogin()
        .loginPage(Endpoints.LOGIN)
        .loginProcessingUrl(Endpoints.LOGIN)
        .defaultSuccessUrl(Endpoints.USERS_LIST, false) // if this is true, user see always this site after login
        .failureHandler(authenticationFailureHandler())
      //.failureUrl(Endpoints.LOGIN + "?error=true")
      .and()
      .logout()
        .logoutUrl(Endpoints.LOGOUT).permitAll()
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .deleteCookies("JSESSIONID")
        .logoutSuccessHandler(logoutSuccessHandler());
        //.logoutSuccessUrl(Endpoints.LOGIN);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }

//  private AuthenticationFilter getAuthenticationFilter() throws Exception {
//    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
//    filter.setFilterProcessesUrl("/authenticate");
//    return filter;
//  }

  private AuthenticationFailureHandler authenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  private LogoutSuccessHandler logoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

}
