package org.skygreen.miniprogram;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import java.util.Map;
import javax.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.skygreen.miniprogram.controller.MiniprogramController;
import org.skygreen.miniprogram.dao.UserDao;
import org.skygreen.miniprogram.data.UserForm;
import org.skygreen.miniprogram.dto.LoginCodeDto;
import org.skygreen.miniprogram.service.IBusinessCardService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@Tag("integration")
@TestProfile(MockMiniprogramProfile.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestHTTPEndpoint(MiniprogramController.class)
public class MiniprogramIntegrationTest {

  private static final String TEST_SESSION_BASE = "test-session-";
  private static final String TEST_OPENID_BASE = "test-openid-";

  private static final String TEST_SESSION_1 = TEST_SESSION_BASE + "1";
  private static final String TEST_NAME_1 = "Test 1";

  private static final String TEST_SESSION_2 = TEST_SESSION_BASE + "2";
  private static final String TEST_NAME_2 = "Test 2";

  private static final String TEST_SESSION_INVALID = TEST_SESSION_BASE + "0";

  @Inject
  IBusinessCardService businessCardService;

  @Inject
  UserDao userRepository;

  @Inject
  private Map<String, LoginCodeDto> sessionMap;

  @BeforeEach
  void setUp() throws Exception {
    final var userNum = 10;
    for (int i = 1; i <= userNum; i++) {
      LoginCodeDto loginCode = new LoginCodeDto();
      loginCode.setOpenid(TEST_OPENID_BASE + i);
      sessionMap.put(TEST_SESSION_BASE + i, loginCode);
    }
  }

  @AfterEach
  void tearDown() throws Exception {
    sessionMap.clear();
    userRepository.deleteAll();
  }

  @Test
  void testGetUserIdEndpoint() {
    var userForm = new UserForm();
    var user = businessCardService.setUser(TEST_SESSION_1, userForm);
    var id = user.getId();

    given().queryParam("session", TEST_SESSION_1).when().get("getid").then().statusCode(200)
        .body(is(id.toString()));

    given().queryParam("session", TEST_SESSION_INVALID).when().get("getid").then().statusCode(403);
  }

  @Test
  void testGetUserEndpoint() {
    var userForm1 = new UserForm();
    userForm1.setName(TEST_NAME_1);
    var user1 = businessCardService.setUser(TEST_SESSION_1, userForm1);
    var id1 = user1.getId();

    var userForm2 = new UserForm();
    userForm2.setName(TEST_NAME_2);
    var user2 = businessCardService.setUser(TEST_SESSION_2, userForm2);
    var id2 = user2.getId();

    given().queryParam("id", id1).when().get("get").then().statusCode(200).body("name",
        is(TEST_NAME_1));

    given().queryParam("id", id2).when().get("get").then().statusCode(200).body("name",
        is(TEST_NAME_2));

    given().queryParam("id", 0).when().get("get").then().statusCode(403);
  }

  @Test
  void testSetUserEndpoint() {
    given().queryParam("session", TEST_SESSION_1).formParam("name", TEST_NAME_1).when().post("set")
        .then().statusCode(200).body("name", is(TEST_NAME_1));

    given().queryParam("session", TEST_SESSION_INVALID).when().post("set").then().statusCode(403);

    given().get("set").then().statusCode(405);
  }

  @Test
  void testGetQRCodeEndpoint() {
    var userForm = new UserForm();
    var user = businessCardService.setUser(TEST_SESSION_1, userForm);
    var id = user.getId();

    given().queryParam("id", id).when().get("getcode").then().statusCode(200);

    given().queryParam("id", 0).when().get("getcode").then().statusCode(403);

    given().queryParam("id", -1).when().get("getcode").then().statusCode(403);

    given().queryParam("id", id + 1).when().get("getcode").then().statusCode(403);
  }

}
