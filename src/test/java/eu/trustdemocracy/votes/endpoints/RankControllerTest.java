package eu.trustdemocracy.votes.endpoints;

import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.unit.TestContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RankControllerTest extends ControllerTest {

  @Test
  public void updateRank(TestContext context) {
    val async = context.async();

    Map<UUID, Double> rankings = new HashMap<>();
    rankings.put(UUID.randomUUID(), new Random().nextDouble());

    val request = new RankRequestDTO()
        .setCalculatedTime(System.currentTimeMillis())
        .setRankings(rankings);

    val single = client.post(port, HOST, "/rank")
        .rxSendJson(request);

    single.subscribe(response -> {
      context.assertEquals(response.statusCode(), 200);
      context.assertTrue(response.headers().get("content-type").contains("application/json"));

      async.complete();
    }, error -> {
      context.fail(error);
      async.complete();
    });
  }

}
