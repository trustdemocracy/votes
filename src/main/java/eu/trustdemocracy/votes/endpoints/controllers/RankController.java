package eu.trustdemocracy.votes.endpoints.controllers;

import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.endpoints.App;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import lombok.val;

public class RankController extends Controller {

  public RankController(App app) {
    super(app);
  }

  @Override
  public void buildRoutes() {
    getRouter().post("/rank").handler(this::updateRank);
  }

  private void updateRank(RoutingContext context) {
    val request = Json.decodeValue(context.getBodyAsString(), RankRequestDTO.class);

    val interactor = getInteractorFactory().getUpdateRank();

    val responseDTO = interactor.execute(request);

    serveJsonResponse(context, 200, Json.encodePrettily(responseDTO));
  }
}
