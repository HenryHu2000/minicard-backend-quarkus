package org.skygreen.miniprogram.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.jboss.resteasy.annotations.Form;
import org.skygreen.miniprogram.data.UserForm;
import org.skygreen.miniprogram.entity.User;
import org.skygreen.miniprogram.exception.ForbiddenException;
import org.skygreen.miniprogram.service.IBusinessCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Path("/miniprogram")
public class MiniprogramController {

  public static final Logger log = LoggerFactory.getLogger(MiniprogramController.class);

  @Inject
  IBusinessCardService businessCardService;

  private <T> ResponseEntity<T> createResponseEntity(T object) {
    if (object == null) {
      throw new ForbiddenException();
    }
    return new ResponseEntity<>(object, HttpStatus.OK);
  }

  @GET
  @Path("/getid")
  @Produces(MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<Integer> getUserId(@QueryParam("session") String session) {
    Integer id = businessCardService.getUserId(session);
    return createResponseEntity(id);
  }

  @GET
  @Path("/get")
  @Produces(MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<User> getUser(@QueryParam("id") Integer id) {
    User user = businessCardService.getUser(id);
    return createResponseEntity(user);
  }

  @POST
  @Path("/set")
  @Produces(MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> setUser(@QueryParam("session") String session, @Form UserForm form) {
    User user = businessCardService.setUser(session, form);
    return createResponseEntity(user);
  }

  @GET
  @Path("/login")
  @Produces(MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> loginUser(@QueryParam("js_code") String jsCode) {
    String session = businessCardService.loginUser(jsCode);
    return createResponseEntity(session);
  }

  @GET
  @Path("/getcode")
  @Produces(MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> getQRCode(@QueryParam("id") Integer id) {
    byte[] binaryImage = businessCardService.getQRCode(id);
    return createResponseEntity(binaryImage);
  }

}
