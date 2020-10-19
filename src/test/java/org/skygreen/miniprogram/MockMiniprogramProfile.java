package org.skygreen.miniprogram;

import java.util.Map;
import java.util.TreeMap;
import io.quarkus.test.junit.QuarkusTestProfile;

public class MockMiniprogramProfile implements QuarkusTestProfile {

  @Override
  public Map<String, String> getConfigOverrides() {
    var map = new TreeMap<String, String>();
    map.put("quarkus.datasource.db-kind", "h2");
    map.put("quarkus.datasource.jdbc.url", "jdbc:h2:tcp://localhost/mem:test");
    map.put("quarkus.hibernate-orm.database.generation", "drop-and-create");
    return map;
  }

}
