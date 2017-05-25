package eu.trustdemocracy.votes.endpoints;

import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.UUID;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ProposalControllerTest extends ControllerTest {

  @Test
  public void registerProposal(TestContext context) {
    val async = context.async();

    val request = new ProposalResponseDTO()
        .setDueDate(System.currentTimeMillis())
        .setId(UUID.randomUUID());

    val single = client.post(port, HOST, "/proposals/register")
        .rxSendJson(request);

    assert200(context, async, single);
  }

  @Test
  public void unregisterProposal(TestContext context) {
    val async = context.async();

    val request = new ProposalResponseDTO()
        .setDueDate(System.currentTimeMillis())
        .setId(UUID.randomUUID());

    client.post(port, HOST, "/proposals/register")
        .rxSendJson(request)
        .subscribe(response -> {
          val single = client.post(port, HOST, "/proposals/unregister")
              .rxSendJson(request);
          assert200(context, async, single);
        });
  }

}
