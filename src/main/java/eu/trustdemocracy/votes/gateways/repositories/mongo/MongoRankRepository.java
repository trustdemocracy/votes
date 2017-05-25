package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import java.util.Map;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoRankRepository implements RankRepository {

  private static final String RANK_COLLECTION = "votes_rank";
  private MongoCollection<Document> collection;

  public MongoRankRepository(MongoDatabase db) {
    this.collection = db.getCollection(RANK_COLLECTION);
  }

  @Override
  public void upsert(UUID userId, Double rank) {
    val document = new Document("id", userId.toString())
        .append("rank", rank);

    val condition = eq("id", userId.toString());
    val options = new UpdateOptions().upsert(true);
    collection.replaceOne(condition, document, options);
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
