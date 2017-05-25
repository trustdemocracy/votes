package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoCollection;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoVotesRepositoryTest {

  private MongoCollection<Document> collection;
  private MongoCollection<Document> ranks;
  private VotesRepository votesRepository;

  private Random rand = new Random();

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("votes");
    ranks = db.getCollection("votes_rank");
    votesRepository = new MongoVotesRepository(db);
  }

  @Test
  public void upsert() {
    val user = new User()
        .setId(UUID.randomUUID());
    val proposal = new Proposal()
        .setId(UUID.randomUUID());
    val vote = new Vote()
        .setUser(user)
        .setProposal(proposal)
        .setOption(VoteOption.FAVOUR);

    votesRepository.upsert(vote);

    val condition = and(
        eq("userId", user.getId().toString()),
        eq("proposalId", proposal.getId().toString())
    );
    val document = collection.find(condition).first();
    assertNotNull(document);
    assertEquals(user.getId(), UUID.fromString(document.getString("userId")));
    assertEquals(proposal.getId(), UUID.fromString(document.getString("proposalId")));
    assertEquals(vote.getOption(), VoteOption.valueOf(document.getString("option")));
  }

  @Test
  public void remove() {
    val user = new User()
        .setId(UUID.randomUUID());
    val proposal = new Proposal()
        .setId(UUID.randomUUID());
    val vote = new Vote()
        .setUser(user)
        .setProposal(proposal)
        .setOption(VoteOption.AGAINST);
    votesRepository.upsert(vote);
    assertEquals(1L, collection.count());

    votesRepository.remove(vote);
    assertEquals(1L, collection.count());
    val condition = and(
        eq("userId", user.getId().toString()),
        eq("proposalId", proposal.getId().toString())
    );
    val document = collection.find(condition).first();
    assertNull(document);
  }

}
