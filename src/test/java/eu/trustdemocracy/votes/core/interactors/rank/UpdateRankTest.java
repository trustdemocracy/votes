package eu.trustdemocracy.votes.core.interactors.rank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.FakeProposalsGateway;
import eu.trustdemocracy.votes.gateways.repositories.fake.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.fake.FakeRankRepository;
import eu.trustdemocracy.votes.gateways.repositories.fake.FakeVotesRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UpdateRankTest {

  private FakeRankRepository rankRepository;
  private FakeProposalsRepository proposalsRepository;
  private FakeVotesRepository votesRepository;
  private FakeProposalsGateway proposalsGateway;
  private Random random = new Random();


  private Proposal dueProposal;
  private Proposal dueProposalToBeUpdated;
  private Proposal inactiveProposal;
  private Proposal activeProposal;

  private User[] favourUsers = new User[10];
  private User[] againstUsers = new User[10];

  private double favourTotal = 0;
  private double againstTotal = 0;

  private long lastCalculatedRank;

  @BeforeEach
  public void init() {
    this.rankRepository = new FakeRankRepository();
    this.proposalsRepository = new FakeProposalsRepository();
    this.votesRepository = new FakeVotesRepository(this.rankRepository.rankings);
    this.proposalsGateway = new FakeProposalsGateway();

    createProposals();
    createVotes(favourUsers, VoteOption.FAVOUR);
    createVotes(againstUsers, VoteOption.AGAINST);
  }

  private void createProposals() {
    lastCalculatedRank = System.currentTimeMillis() - 60 * 1000;

    dueProposal = new Proposal()
        .setId(UUID.randomUUID())
        .setActive(true)
        .setDueDate(lastCalculatedRank - 10);
    proposalsRepository.upsert(dueProposal);

    dueProposalToBeUpdated = new Proposal()
        .setId(UUID.randomUUID())
        .setActive(true)
        .setDueDate(lastCalculatedRank + 1);
    proposalsRepository.upsert(dueProposalToBeUpdated);

    inactiveProposal = new Proposal()
        .setId(UUID.randomUUID())
        .setActive(false)
        .setDueDate(System.currentTimeMillis() + 60 * 1000);
    proposalsRepository.upsert(inactiveProposal);

    activeProposal = new Proposal()
        .setId(UUID.randomUUID())
        .setActive(true)
        .setDueDate(System.currentTimeMillis() + 60 * 1000);
    proposalsRepository.upsert(activeProposal);
  }

  private void createVotes(User[] users, VoteOption option) {
    for (int i = 0; i < users.length; i++) {
      val user = new User()
          .setId(UUID.randomUUID())
          .setRank(random.nextDouble());
      users[i] = user;
      rankRepository.upsert(user.getId(), user.getRank());

      votesRepository.votes.put(dueProposal.getId() + "|" + user.getId(), option);
      votesRepository.votes.put(dueProposalToBeUpdated.getId() + "|" + user.getId(), option);
      votesRepository.votes.put(inactiveProposal.getId() + "|" + user.getId(), option);
      votesRepository.votes.put(activeProposal.getId() + "|" + user.getId(), option);
    }
  }


  @Test
  public void updateRank() {
    int amountOfUsers = favourUsers.length + againstUsers.length;

    RankRequestDTO request = new RankRequestDTO()
        .setRankings(getUserRankings())
        .setCalculatedTime(lastCalculatedRank);

    assertEquals(amountOfUsers, rankRepository.rankings.size());

    val interactor = new UpdateRank(rankRepository, proposalsRepository, votesRepository,
        proposalsGateway);
    RankResponseDTO response = interactor.execute(request);

    assertTrue(response.isSuccessful());
    assertEquals(amountOfUsers, rankRepository.rankings.size());

    val updatedProposals = proposalsGateway.proposals;
    assertNull(updatedProposals.get(dueProposal.getId()));
    assertNull(updatedProposals.get(inactiveProposal.getId()));

    val dueProposalToBeUpdatedMap = updatedProposals.get(dueProposalToBeUpdated.getId());
    assertEquals(favourTotal, dueProposalToBeUpdatedMap.get(VoteOption.FAVOUR), 0.1);
    assertEquals(againstTotal, dueProposalToBeUpdatedMap.get(VoteOption.AGAINST), 0.1);

    val activeProposalMap = updatedProposals.get(activeProposal.getId());
    assertEquals(favourTotal, activeProposalMap.get(VoteOption.FAVOUR), 0.1);
    assertEquals(againstTotal, activeProposalMap.get(VoteOption.AGAINST), 0.1);
  }

  private Map<UUID, Double> getUserRankings() {
    Map<UUID, Double> rankings = new HashMap<>();

    for (int i = 0; i < favourUsers.length; i++) {
      val rank = random.nextDouble();
      rankings.put(favourUsers[i].getId(), rank);
      favourTotal += rank;
    }

    for (int i = 0; i < againstUsers.length; i++) {
      val rank = random.nextDouble();
      rankings.put(againstUsers[i].getId(), rank);
      againstTotal += rank;
    }

    return rankings;
  }

}
