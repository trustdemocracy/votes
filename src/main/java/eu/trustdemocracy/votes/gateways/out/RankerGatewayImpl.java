package eu.trustdemocracy.votes.gateways.out;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import lombok.val;

public class RankerGatewayImpl implements RankerGateway {

  private Vertx vertx = Vertx.vertx();
  private static String host;
  private static Integer port;

  @Override
  public void addDueDate(Long dueDate) {
    val json = new JsonObject().put("dueDate", dueDate);
    sendDueDate(json);
  }

  private void sendDueDate(JsonObject dueDate) {
    WebClient.create(vertx)
        .post(getRankerPort(), getRankerHost(), "/dueDate")
        .rxSendJson(dueDate)
        .subscribe();
  }

  private static String getRankerHost() {
    if (host == null) {
      host = System.getenv("ranker_host");
    }
    return host;
  }

  private static int getRankerPort() {
    if (port == null) {
      port = Integer.valueOf(System.getenv("ranker_port"));
    }
    return port;
  }
}
