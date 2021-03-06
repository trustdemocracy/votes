package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoRankRepositoryTest {

  private MongoCollection<Document> collection;
  private RankRepository rankRepository;

  private Random rand = new Random();

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("votes_rank");
    rankRepository = new MongoRankRepository(db);
  }

  @Test
  public void upsert() {
    val user = new User()
        .setId(UUID.randomUUID())
        .setRank(rand.nextDouble());

    rankRepository.upsert(user.getId(), user.getRank());

    val condition = eq("id", user.getId().toString());
    val document = collection.find(condition).first();
    assertNotNull(document);
    assertEquals(user.getId(), UUID.fromString(document.getString("id")));
    assertEquals(user.getRank(), document.getDouble("rank"));
  }

  @Test
  public void find() {
    val user = new User()
        .setId(UUID.randomUUID())
        .setRank(rand.nextDouble());

    rankRepository.upsert(user.getId(), user.getRank());

    val foundRank = rankRepository.find(user.getId());

    assertEquals(user.getRank(), foundRank);
  }

  @Test
  public void upsertBatch() {
    Map<UUID, Double> users = new HashMap<>();
    for (int i = 0; i < 30; i++) {
      val user = new User()
          .setId(UUID.randomUUID())
          .setRank(rand.nextDouble());
      users.put(user.getId(), user.getRank());
    }

    assertEquals(0L, collection.count());

    val updatedAll = rankRepository.upsertBatch(users);
    assertTrue(updatedAll);

    assertEquals(users.size(), collection.count());
  }

}
