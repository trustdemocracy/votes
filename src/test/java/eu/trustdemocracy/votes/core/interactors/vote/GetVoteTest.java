package eu.trustdemocracy.votes.core.interactors.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.votes.core.interactors.proposal.UnregisterProposal;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.core.interactors.util.TokenUtils;
import eu.trustdemocracy.votes.core.models.request.GetVoteRequestDTO;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.FakeProposalsGateway;
import eu.trustdemocracy.votes.gateways.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.FakeRankRepository;
import eu.trustdemocracy.votes.gateways.FakeVotesRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetVoteTest {

  private Random rand = new Random();

  private FakeProposalsRepository proposalsRepository;
  private FakeVotesRepository votesRepository;
  private FakeRankRepository rankRepository;

  private List<Proposal> proposals = new ArrayList<>();
  private User user;
  private Double updatedRank = rand.nextDouble();

  @BeforeEach
  public void init() throws JoseException {
    TokenUtils.generateKeys();

    proposalsRepository = new FakeProposalsRepository();
    rankRepository = new FakeRankRepository();
    votesRepository = new FakeVotesRepository(rankRepository.rankings);

    user = new User()
        .setId(UUID.randomUUID())
        .setUsername("username")
        .setRank(rand.nextDouble());

    rankRepository.upsert(user.getId(), user.getRank());

    for (int i = 0; i < 10; i++) {
      val proposal = new Proposal()
          .setId(UUID.randomUUID())
          .setDueDate(System.currentTimeMillis() + 10000 * i)
          .setActive(i % 2 == 0);
      proposals.add(proposal);
      proposalsRepository.upsert(proposal);
    }

    Map<UUID, Double> ranks = new HashMap<>();
    ranks.put(user.getId(), updatedRank);

    new UpdateRank(rankRepository, proposalsRepository, votesRepository,
        new FakeProposalsGateway())
        .execute(new RankRequestDTO()
            .setCalculatedTime(System.currentTimeMillis())
            .setRankings(ranks));
  }

  @Test
  public void getVoteInPublished() throws Exception {
    Proposal proposal = proposals.stream()
        .filter(Proposal::isActive)
        .findFirst()
        .orElseThrow(Exception::new);

    val option = VoteOption.AGAINST;
    votesRepository.votes.put(proposal.getId() + "|" + user.getId(), option);

    GetVoteRequestDTO requestDTO = new GetVoteRequestDTO()
        .setUserToken(TokenUtils.createToken(user.getId(), user.getUsername()))
        .setProposalId(proposal.getId());

    VoteResponseDTO responseDTO = new GetVote(votesRepository, proposalsRepository)
        .execute(requestDTO);

    assertEquals(user.getId(), responseDTO.getUserId());
    assertEquals(proposal.getId(), responseDTO.getProposalId());
    assertEquals(option, responseDTO.getOption());
    assertEquals(null, responseDTO.getRank());
    assertFalse(responseDTO.isProposalLocked());
  }

  @Test
  public void getVoteInUnpublished() throws Exception {
    Proposal proposal = proposals.stream()
        .filter(p -> !p.isActive())
        .findFirst()
        .orElseThrow(Exception::new);

    val option = VoteOption.AGAINST;
    votesRepository.votes.put(proposal.getId() + "|" + user.getId(), option);

    new UnregisterProposal(proposalsRepository).execute(new ProposalRequestDTO()
        .setId(proposal.getId()));

    GetVoteRequestDTO requestDTO = new GetVoteRequestDTO()
        .setUserToken(TokenUtils.createToken(user.getId(), user.getUsername()))
        .setProposalId(proposal.getId());

    assertThrows(ResourceNotFoundException.class,
        () -> new GetVote(votesRepository, proposalsRepository).execute(requestDTO));
  }

  @Test
  public void getVoteInExpired() throws Exception {
    Proposal proposal = proposals.stream()
        .filter(p -> p.isExpired() && p.isActive())
        .findFirst()
        .orElseThrow(Exception::new);

    val option = VoteOption.AGAINST;
    val key = proposal.getId() + "|" + user.getId();
    votesRepository.votes.put(key, option);
    votesRepository.lockedRanks.put(key, user.getRank());
    
    GetVoteRequestDTO requestDTO = new GetVoteRequestDTO()
        .setUserToken(TokenUtils.createToken(user.getId(), user.getUsername()))
        .setProposalId(proposal.getId());

    VoteResponseDTO responseDTO = new GetVote(votesRepository, proposalsRepository)
        .execute(requestDTO);

    assertEquals(user.getId(), responseDTO.getUserId());
    assertEquals(proposal.getId(), responseDTO.getProposalId());
    assertEquals(option, responseDTO.getOption());
    assertEquals(user.getRank(), responseDTO.getRank());
    assertTrue(responseDTO.isProposalLocked());
  }
}
