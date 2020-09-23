package org.skygreen.miniprogram.config;

import io.quarkus.arc.config.ConfigProperties;

@ConfigProperties(prefix = "miniprogram")
public class MiniprogramProperties {

  private String appid;

  private String secret;

  public String getAppid() {
    return appid;
  }

  public void setAppid(String appid) {
    this.appid = appid;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

}
