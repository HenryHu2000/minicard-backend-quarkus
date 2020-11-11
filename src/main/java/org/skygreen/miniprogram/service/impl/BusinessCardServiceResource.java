package org.skygreen.miniprogram.service.impl;

import java.util.Map;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.skygreen.miniprogram.config.MiniprogramProperties;
import org.skygreen.miniprogram.dao.UserDao;
import org.skygreen.miniprogram.data.AccessToken;
import org.skygreen.miniprogram.data.QRCodeForm;
import org.skygreen.miniprogram.data.UserForm;
import org.skygreen.miniprogram.dto.LoginCodeDto;
import org.skygreen.miniprogram.entity.User;
import org.skygreen.miniprogram.service.IBusinessCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class BusinessCardServiceResource implements IBusinessCardService {

  public static final Logger log =
      LoggerFactory.getLogger(BusinessCardServiceResource.class);

  @Inject
  AccessToken accessToken;

  @Inject
  UserDao userRepository;

  @Inject
  MiniprogramProperties miniprogramProperties;

  @Inject
  private Map<String, LoginCodeDto> sessionMap;

  private boolean isSessionValid(String session) {
    return sessionMap.containsKey(session);
  }

  /*
   * Precondition: session is valid
   */
  private String sessionToOpenid(String session) {
    var loginCode = sessionMap.get(session);
    return loginCode.getOpenid();
  }

  /*
   * Precondition: session is valid
   */
  private Integer sessionToUserId(String session) {
    var openid = sessionToOpenid(session);
    User user = userRepository.findUserByOpenid(openid);
    var id = (user != null) ? user.getId() : -1; // Return -1 when user is not
                                                 // recorded
    return id;
  }

  @Override
  public Integer getUserId(String session) {
    if (!isSessionValid(session)) {
      return null;
    }

    var id = sessionToUserId(session);
    return id;
  }

  @Override
  public User getUser(Integer id) {
    var user = userRepository.findUserById(id);
    return user;
  }

  @Override
  public User setUser(String session, UserForm userForm) {
    if (!isSessionValid(session)) {
      return null;
    }

    var id = sessionToUserId(session); // Could be unavailable
    var openid = sessionToOpenid(session); // Always available

    User user;
    if (!id.equals(-1)) {
      user = userRepository.findUserById(id);
    } else {
      user = new User();
      user.setOpenid(openid);
    }
    configureUserValues(user, userForm);
    userRepository.save(user);

    return userRepository.findUserByOpenid(openid);
  }

  private void configureUserValues(User user, UserForm userForm) {
    var name = userForm.getName();
    if (name != null) {
      user.setName(name);
    }
    var title = userForm.getTitle();
    if (title != null) {
      user.setTitle(title);
    }
    var organization = userForm.getOrganization();
    if (organization != null) {
      user.setOrganization(organization);
    }
    var address = userForm.getAddress();
    if (address != null) {
      user.setAddress(address);
    }
    var postcode = userForm.getPostcode();
    if (postcode != null) {
      user.setPostcode(postcode);
    }
    var telephone = userForm.getTelephone();
    if (telephone != null) {
      user.setTelephone(telephone);
    }
    var mobile = userForm.getMobile();
    if (mobile != null) {
      user.setMobile(mobile);
    }
    var fax = userForm.getFax();
    if (fax != null) {
      user.setFax(fax);
    }
    var email = userForm.getEmail();
    if (email != null) {
      user.setEmail(email);
    }
    var website = userForm.getWebsite();
    if (website != null) {
      user.setWebsite(website);
    }
  }

  @Override
  public String loginUser(String jsCode) {
    var url =
        "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&appid="
            + miniprogramProperties.getAppid() + "&secret="
            + miniprogramProperties.getSecret() + "&js_code=" + jsCode;

    Client client = ClientBuilder.newBuilder().build();
    WebTarget target = client.target(url);
    Response response = target.request().get();
    String loginCodeStr = response.readEntity(String.class);
    response.close();

    ObjectMapper mapper = new ObjectMapper();
    LoginCodeDto loginCode;
    try {
      loginCode = mapper.readValue(loginCodeStr, LoginCodeDto.class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }

    if (loginCode.getOpenid() == null) {
      return null;
    }

    var session = UUID.randomUUID().toString();
    sessionMap.put(session, loginCode);
    return session;
  }

  @Override
  public byte[] getQRCode(Integer id) {
    if (!userRepository.existsUserById(id)) {
      return null;
    }

    var url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="
        + accessToken.getAccessToken();
    var codeInfo = new QRCodeForm();
    codeInfo.setPath("pages/card/card");
    codeInfo.setScene("id=" + id);

    Client client = ClientBuilder.newBuilder().build();
    WebTarget target = client.target(url);
    Response response = target.request().post(Entity.json(codeInfo));
    byte[] binaryImage = response.readEntity(byte[].class);

    response.close();

    return binaryImage;
  }

}
