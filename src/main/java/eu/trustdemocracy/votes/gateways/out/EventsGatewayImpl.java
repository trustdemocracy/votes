package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import java.util.Map;
import lombok.val;

public class EventsGatewayImpl implements EventsGateway {

  private Vertx vertx = Vertx.vertx();
  private static String host;
  private static Integer port;

  @Override
  public void createVoteEvent(Vote vote, Map<VoteOption, Double> votes) {
    val serializedContent = new JsonObject()
        .put("proposalId", vote.getProposal().getId().toString())
        .put("title", vote.getProposal().getTitle())
        .put("option", vote.getOption())
        .put("contributed", vote.getUser().getRank())
        .put("results", votes);

    val event = new JsonObject()
        .put("userId", vote.getUser().getId().toString())
        .put("username", vote.getUser().getUsername())
        .put("type", "VOTE")
        .put("timestamp", System.currentTimeMillis())
        .put("serializedContent", serializedContent);
    sendEvent(event);
  }

  private void sendEvent(JsonObject event) {
    WebClient.create(vertx)
        .post(getSocialPort(), getSocialHost(), "/events")
        .rxSendJson(event)
        .subscribe();
  }

  private static String getSocialHost() {
    if (host == null) {
      host = System.getenv("social_host");
    }
    return host;
  }

  private static int getSocialPort() {
    if (port == null) {
      port = Integer.valueOf(System.getenv("social_port"));
    }
    return port;
  }
}
