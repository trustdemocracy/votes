package eu.trustdemocracy.votes.endpoints;

import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.interactors.util.TokenUtils;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.util.UUID;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class VoteControllerTest extends ControllerTest {

  @Test
  public void vote(TestContext context) {
    val async = context.async();

    val proposal = new ProposalRequestDTO()
        .setDueDate(System.currentTimeMillis() * 20 * 1000)
        .setId(UUID.randomUUID());
    interactorFactory.getRegisterProposal().execute(proposal);

    val request = new VoteRequestDTO()
        .setOption(VoteOption.FAVOUR);
    val userToken = TokenUtils.createToken(UUID.randomUUID(), "username");

    val single = client.post(port, HOST, "/votes/" + proposal.getId())
        .putHeader("Authorization", "Bearer: " + userToken)
        .rxSendJson(request);

    assert200(context, async, single);
  }

  @Test
  public void getVote(TestContext context) {
    val async = context.async();

    val proposal = new ProposalRequestDTO()
        .setDueDate(System.currentTimeMillis() * 20 * 1000)
        .setId(UUID.randomUUID());
    interactorFactory.getRegisterProposal().execute(proposal);

    val request = new VoteRequestDTO()
        .setOption(VoteOption.AGAINST);

    val userToken = TokenUtils.createToken(UUID.randomUUID(), "username");

    client.post(port, HOST, "/votes/" + proposal.getId())
        .putHeader("Authorization", "Bearer: " + userToken)
        .rxSendJson(request)
        .subscribe(response -> {
          val single = client.get(port, HOST, "/votes/" + proposal.getId())
              .putHeader("Authorization", "Bearer: " + userToken)
              .rxSendJson(request);
          assert200(context, async, single);
        });
  }

}
