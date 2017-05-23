package eu.trustdemocracy.votes.gateways;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.val;

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

  @Override
  public boolean upsertBatch(Map<UUID, Double> rankings) {
    for (val id : rankings.keySet()) {
      this.rankings.put(id, rankings.get(id));
    }

    return true;
  }
}
