package eu.trustdemocracy.votes.gateways.repositories.mongo;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    val condition = eq("id", proposal.getId().toString());
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

  @Test
  public void findAllActive() {
    List<Proposal> activeProposals = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      val active = i % 3 == 0;
      val proposal = new Proposal()
          .setId(UUID.randomUUID())
          .setDueDate(System.currentTimeMillis())
          .setActive(active);

      if (active) {
        activeProposals.add(proposal);
      }

      proposalsRepository.upsert(proposal);
    }

    Set<Proposal> proposals = proposalsRepository.findAllActive();

    assertEquals(activeProposals.size(), proposals.size());

    for (val proposal : activeProposals) {
      assertTrue(proposals.contains(proposal));
    }
  }

  @Test
  public void updateExpired() {
    List<Proposal> proposalsToExpire = new ArrayList<>();
    for (int i = 0; i < 30; i++) {
      val toExpire = i % 3 == 0;
      val proposal = new Proposal()
          .setId(UUID.randomUUID())
          .setDueDate(System.currentTimeMillis())
          .setActive(true);

      if (toExpire) {
        proposalsToExpire.add(proposal);
      }

      proposalsRepository.upsert(proposal);
    }

    proposalsRepository.updateExpired(new HashSet<>(proposalsToExpire));

    val condition = eq("expired", true);
    assertEquals(proposalsToExpire.size(), collection.count(condition));
  }

}
