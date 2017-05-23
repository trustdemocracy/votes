package eu.trustdemocracy.votes.gateways;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeRankRepository implements RankRepository {

  public Map<UUID, Double> rankings = new HashMap<>();

  @Override
  public void upsert(UUID userId, Double rank) {
    rankings.put(userId, rank);
  }

  @Override
  public Double find(UUID id) {
    if (rankings.containsKey(id)) {
      return rankings.get(id);
    }
    return 0.0;
  }
}
