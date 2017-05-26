package eu.trustdemocracy.votes.endpoints.controllers;

import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

public class ProposalController extends Controller {

  public ProposalController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/proposals/register").handler(this::registerProposal);
    getRouter().post("/proposals/unregister").handler(this::unregisterProposal);
  }

  private void registerProposal(RoutingContext context) {
    val request = Json.decodeValue(context.getBodyAsString(), ProposalRequestDTO.class);

    val interactor = getInteractorFactory().getRegisterProposal();

    val responseDTO = interactor.execute(request);

    serveJsonResponse(context, 200, Json.encodePrettily(responseDTO));
  }

  private void unregisterProposal(RoutingContext context) {
    val request = Json.decodeValue(context.getBodyAsString(), ProposalRequestDTO.class);

    val interactor = getInteractorFactory().getUnregisterProposal();

    val responseDTO = interactor.execute(request);

    serveJsonResponse(context, 200, Json.encodePrettily(responseDTO));
  }
}
