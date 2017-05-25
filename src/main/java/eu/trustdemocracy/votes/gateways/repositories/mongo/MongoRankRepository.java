package eu.trustdemocracy.votes.gateways.repositories.mongo;

import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import java.util.Map;
import java.util.UUID;

public class MongoRankRepository implements RankRepository {

  public MongoRankRepository(MongoDatabase db) {
  }

  @Override
  public void upsert(UUID userId, Double rank) {

  }

  @Override
  public Double find(UUID id) {
    return null;
  }

  @Override
  public boolean upsertBatch(Map<UUID, Double> rankings) {
    return false;
  }
}
