package eu.trustdemocracy.votes.core.interactors.rank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.FakeProposalsGateway;
import eu.trustdemocracy.votes.gateways.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.FakeRankRepository;
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
  private FakeProposalsGateway proposalsGateway;
  private Random random = new Random();

  @BeforeEach
  public void init() {
    this.rankRepository = new FakeRankRepository();
    this.proposalsRepository = new FakeProposalsRepository();
    this.proposalsGateway = new FakeProposalsGateway();
  }

  @Test
  public void updateRank() {
    int amountOfUsers = 100;
    long calculatedTime = 1000;

    RankRequestDTO request = new RankRequestDTO()
        .setRankings(getUserRankings(amountOfUsers))
        .setCalculatedTime(calculatedTime);

    assertEquals(0, rankRepository.rankings.size());

    val interactor = new UpdateRank(rankRepository, proposalsRepository, proposalsGateway);
    RankResponseDTO response = interactor.execute(request);

    assertTrue(response.isSuccessful());
    assertEquals(amountOfUsers, rankRepository.rankings.size());
  }

  private Map<UUID, Double> getUserRankings(int size) {
    Map<UUID, Double> rankings = new HashMap<>();

    for (int i = 0; i < size; i++) {
      rankings.put(UUID.randomUUID(), random.nextDouble());
    }

    return rankings;
  }

}
