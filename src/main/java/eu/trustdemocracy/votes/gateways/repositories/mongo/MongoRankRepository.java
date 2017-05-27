package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
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
    val condition = eq("id", id.toString());
    val document = collection.find(condition).first();
    if (document == null) {
      return 0.0;
    }
    return document.getDouble("rank");
  }

  @Override
  public boolean upsertBatch(Map<UUID, Double> rankings) {
    val options = new UpdateOptions().upsert(true);
    val result = collection.bulkWrite(
        rankings.entrySet().stream()
            .map(entry ->
                new ReplaceOneModel<Document>(
                    eq("id", entry.getKey().toString()),
                    new Document("id", entry.getKey().toString())
                        .append("rank", entry.getValue()),
                    options
                )
            )
            .collect(Collectors.toList()),
        new BulkWriteOptions().ordered(false)
    );

    return (result.getModifiedCount() + result.getInsertedCount()) == rankings.size();
  }
}
