package rocks.danielw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

  @Autowired
  private Environment env;

  public String getTokenSecret() {
    return env.getProperty("token-secret");
  }

  public String getDefaultMailFrom() {
    return env.getProperty("mail.from.email");
  }

  public int getHttpPort() {
    return Integer.parseInt(env.getProperty("server.port", "80"));
  }

  public String getHost() {
    return env.getProperty("application.host");
  }
	
}
