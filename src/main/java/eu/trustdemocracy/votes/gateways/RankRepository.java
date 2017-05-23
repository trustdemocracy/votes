package eu.trustdemocracy.votes.gateways;

import java.util.UUID;

public interface RankRepository {

  void upsert(UUID userId, Double rank);

  Double find(UUID id);
}
