package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.client.WebClient;
import java.util.HashMap;
import java.util.Map;
import lombok.val;

public class ProposalsGatewayImpl implements ProposalsGateway {

  private Vertx vertx = Vertx.vertx();
  private static String host;
  private static Integer port;

  @Override
  public void update(Proposal proposal, Map<VoteOption, Double> results) {
    Map<Proposal, Map<VoteOption, Double>> map = new HashMap<>();
    map.put(proposal, results);
    updateBatch(map);
  }

  @Override
  public void updateBatch(Map<Proposal, Map<VoteOption, Double>> proposals) {
    val jsonProposals = new JsonObject();
    for (val proposal : proposals.entrySet()) {
      jsonProposals.put(
          proposal.getKey().getId().toString(),
          new JsonObject()
              .put("results", proposal.getValue())
              .put("expired", proposal.getKey().isExpired())
      );
    }

    sendUpdate(jsonProposals);
  }

  private void sendUpdate(JsonObject proposals) {
    WebClient.create(vertx)
        .post(getProposalsPort(), getProposalsHost(), "/proposals/results")
        .rxSendJson(proposals)
        .subscribe();
  }

  private static String getProposalsHost() {
    if (host == null) {
      host = System.getenv("proposals_host");
    }
    return host;
  }

  private static int getProposalsPort() {
    if (port == null) {
      port = Integer.valueOf(System.getenv("proposals_port"));
    }
    return port;
  }
}
