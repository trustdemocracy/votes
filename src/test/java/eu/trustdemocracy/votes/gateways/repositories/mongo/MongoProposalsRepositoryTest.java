package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoProposalsRepositoryTest {

  private MongoCollection<Document> collection;
  private ProposalsRepository proposalsRepository;

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("proposals");
    proposalsRepository = new MongoProposalsRepository(db);
  }

  @Test
  public void upsertProposal() {
    val proposal = new Proposal()
        .setId(UUID.randomUUID())
        .setDueDate(System.currentTimeMillis())
        .setActive(true);

    assertEquals(0L, collection.count());

    val existedBefore = proposalsRepository.upsert(proposal);
    assertFalse(existedBefore);

    assertEquals(1L, collection.count());

    val condition = and(
        eq("id", proposal.getId().toString())
    );
    val document = collection.find(condition).first();
    assertNotNull(document);
    assertEquals(proposal.getId(), UUID.fromString(document.getString("id")));
    assertEquals(proposal.getDueDate(), document.getLong("dueDate"));
    assertTrue(document.getBoolean("active"));
    assertFalse(document.getBoolean("expired"));
  }

  @Test
  public void findProposal() {
    val proposal = new Proposal()
        .setId(UUID.randomUUID())
        .setDueDate(System.currentTimeMillis())
        .setActive(true);

    assertNull(proposalsRepository.find(proposal.getId()));

    proposalsRepository.upsert(proposal);

    assertEquals(proposal, proposalsRepository.find(proposal.getId()));
  }

}
