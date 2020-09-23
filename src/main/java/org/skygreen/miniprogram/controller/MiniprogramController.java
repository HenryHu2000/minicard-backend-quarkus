package org.skygreen.miniprogram.controller;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.Form;
import org.skygreen.miniprogram.data.UserForm;
import org.skygreen.miniprogram.entity.User;
import org.skygreen.miniprogram.service.IBusinessCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/miniprogram")
public class MiniprogramController {

  public static final Logger log = LoggerFactory.getLogger(MiniprogramController.class);

  @Inject
  IBusinessCardService businessCardService;

  private Response createResponse(Object entity) {
    if (entity == null) {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
    return Response.ok(entity).build();
  }

  @GET
  @Path("/getid")
  @Produces("text/plain")
  public Response getUserId(@QueryParam("session") String session) {
    Integer id = businessCardService.getUserId(session);
    return createResponse(id);
  }

  @GET
  @Path("/get")
  @Produces("application/json")
  public Response getUser(@QueryParam("id") Integer id) {
    User user = businessCardService.getUser(id);
    return createResponse(user);
  }

  @POST
  @Path("/set")
  @Produces("application/json")
  public Response setUser(@QueryParam("session") String session, @Form UserForm form) {
    User user = businessCardService.setUser(session, form);
    return createResponse(user);
  }

  @GET
  @Path("/login")
  @Produces("text/plain")
  public Response loginUser(@QueryParam("js_code") String jsCode) {
    String session = businessCardService.loginUser(jsCode);
    return createResponse(session);
  }

  @GET
  @Path("/getcode")
  @Produces("image/jpeg")
  public Response getQRCode(@QueryParam("id") Integer id) {
    byte[] binaryImage = businessCardService.getQRCode(id);
    return createResponse(binaryImage);
  }

}
