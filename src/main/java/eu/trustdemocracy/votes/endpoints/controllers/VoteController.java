package eu.trustdemocracy.votes.endpoints.controllers;

import eu.trustdemocracy.votes.core.models.request.GetVoteRequestDTO;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import eu.trustdemocracy.votes.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import java.util.UUID;
import lombok.val;

public class VoteController extends Controller {

  public VoteController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/votes/:proposalId").handler(this::vote);
    getRouter().get("/votes/:proposalId").handler(this::getVote);
  }

  private void vote(RoutingContext context) {
    val request = Json.decodeValue(context.getBodyAsString(), VoteRequestDTO.class);
    request.setProposalId(UUID.fromString(context.pathParam("proposalId")));
    val authToken = getAuthorizationToken(context.request());
    request.setUserToken(authToken);

    val interactor = getInteractorFactory().getVoteProposal();

    val responseDTO = interactor.execute(request);

    serveJsonResponse(context, 200, Json.encodePrettily(responseDTO));
  }

  private void getVote(RoutingContext context) {
    val authToken = getAuthorizationToken(context.request());

    val request = new GetVoteRequestDTO()
        .setProposalId(UUID.fromString(context.pathParam("proposalId")))
        .setUserToken(authToken);

    val interactor = getInteractorFactory().getGetVote();

    val responseDTO = interactor.execute(request);

    serveJsonResponse(context, 200, Json.encodePrettily(responseDTO));

  }

}
