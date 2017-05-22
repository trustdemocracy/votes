package eu.trustdemocracy.votes.core.interactors.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.interactors.util.TokenUtils;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.FakeEventsGateway;
import eu.trustdemocracy.votes.gateways.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.FakeRankRepository;
import eu.trustdemocracy.votes.gateways.FakeVotesRepository;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VoteTest {

  private FakeProposalsRepository proposalsRepository;
  private FakeVotesRepository votesRepository;
  private FakeRankRepository rankRepository;
  private FakeEventsGateway eventsGateway;

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    proposalsRepository = new FakeProposalsRepository();
    votesRepository = new FakeVotesRepository();
    rankRepository = new FakeRankRepository();
    eventsGateway = new FakeEventsGateway();

    for (int i = 0; i < 10; i++) {
      proposalsRepository.upsert(new Proposal()
          .setId(UUID.randomUUID())
          .setDueDate(System.currentTimeMillis() + 10000 * i)
          .setActive(i % 2 == 0));
    }
  }

  @Test
  public void voteFavour() {
    val proposalId = UUID.randomUUID();
    proposalsRepository.upsert(new Proposal()
        .setId(proposalId)
        .setDueDate(System.currentTimeMillis() + 10000)
        .setActive(true));

    val user = new User()
        .setId(UUID.randomUUID())
        .setUsername("username")
        .setRank(0.5);

    rankRepository.upsert(user);

    VoteRequestDTO voteRequest = new VoteRequestDTO()
        .setProposalId(proposalId)
        .setUserToken(TokenUtils.createToken(user.getId(), user.getUsername()))
        .setOption(VoteOption.FAVOUR);

    val interactor = new Vote(votesRepository, proposalsRepository, rankRepository, eventsGateway);

    VoteResponseDTO createdVote = interactor.execute(voteRequest);

    assertEquals(proposalId, createdVote.getProposalId());
    assertEquals(user.getId(), createdVote.getUserId());
    assertEquals(user.getRank(), createdVote.getRank());
  }
}
