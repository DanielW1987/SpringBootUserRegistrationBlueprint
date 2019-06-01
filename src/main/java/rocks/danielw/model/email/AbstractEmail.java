package rocks.danielw.model.email;

import rocks.danielw.config.AppProperties;
import rocks.danielw.config.SpringApplicationContext;

import java.util.Map;

public abstract class AbstractEmail {

  static final String  HOST = ((AppProperties) SpringApplicationContext.getBean("appProperties")).getHost();
  static final Integer PORT = ((AppProperties) SpringApplicationContext.getBean("appProperties")).getHttpPort();

  public String getFrom() {
    return ((AppProperties) SpringApplicationContext.getBean("appProperties")).getDefaultMailFrom();
  }

  public abstract String getRecipient();

  public abstract String getSubject();

  public abstract Map<String, Object> getViewModel();

  public abstract String getTemplateName();

}
