package org.skygreen.miniprogram.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.skygreen.miniprogram.config.MiniprogramProperties;
import org.skygreen.miniprogram.data.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

@ApplicationScoped
public class TokenUpdateScheduler {
  public static final Logger log = LoggerFactory.getLogger(TokenUpdateScheduler.class);

  @Inject
  MiniprogramProperties miniprogramProperties;

  private static final int TIME_RATE = 3600000;

  private AccessToken accessToken;

  @Scheduled(fixedRate = TIME_RATE)
  public void updateAccessToken() {
    var url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
        + miniprogramProperties.getAppid() + "&secret=" + miniprogramProperties.getSecret();

    Client client = ClientBuilder.newBuilder().build();
    WebTarget target = client.target(url);
    Response response = target.request().get();
    this.accessToken = response.readEntity(AccessToken.class);
    response.close();

    log.info("WeChat access token updated");
  }

  @Produces
  public AccessToken accessToken(InjectionPoint injectionPoint) {
    if (accessToken == null) {
      updateAccessToken();
    }
    return accessToken;
  }

}
