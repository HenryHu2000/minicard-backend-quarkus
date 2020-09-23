package org.skygreen.miniprogram.service;

import org.skygreen.miniprogram.data.UserForm;
import org.skygreen.miniprogram.entity.User;

public interface IBusinessCardService {

  Integer getUserId(String session);

  User getUser(Integer id);

  User setUser(String session, UserForm userForm);

  String loginUser(String jsCode);

  byte[] getQRCode(Integer id);

}
