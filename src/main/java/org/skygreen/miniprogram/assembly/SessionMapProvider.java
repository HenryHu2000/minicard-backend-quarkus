package org.skygreen.miniprogram.assembly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.skygreen.miniprogram.dto.LoginCodeDto;

@ApplicationScoped
public class SessionMapProvider {

  private Map<String, LoginCodeDto> sessionMap =
      Collections.synchronizedMap(new HashMap<>());

  @Produces
  public Map<String, LoginCodeDto> sessionMap(InjectionPoint injectionPoint) {
    return sessionMap;
  }

}
