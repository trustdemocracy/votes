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
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import lombok.val;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MongoVotesRepositoryTest {

  private MongoCollection<Document> collection;
  private VotesRepository votesRepository;
  private RankRepository rankRepository;

  private Random rand = new Random();

  @BeforeEach
  public void init() {
    val fongo = new Fongo("test server");
    val db = fongo.getDatabase("test_database");
    collection = db.getCollection("votes");
    rankRepository = new MongoRankRepository(db);
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
    assertEquals(0L, collection.count());
    val condition = and(
        eq("userId", user.getId().toString()),
        eq("proposalId", proposal.getId().toString())
    );
    val document = collection.find(condition).first();
    assertNull(document);
  }

  @Test
  public void findProposalResults() {
    val user = new User()
        .setId(UUID.randomUUID())
        .setRank(rand.nextDouble());
    val proposal = new Proposal()
        .setId(UUID.randomUUID());
    val vote = new Vote()
        .setUser(user)
        .setProposal(proposal)
        .setOption(VoteOption.AGAINST);
    votesRepository.upsert(vote);
    assertEquals(1L, collection.count());

    rankRepository.upsert(user.getId(), user.getRank());

    Map<VoteOption, Double> results = votesRepository.findProposalResults(proposal.getId());

    assertEquals(0.0, results.get(VoteOption.FAVOUR), 0.1);
    assertEquals(user.getRank(), results.get(VoteOption.AGAINST), 0.1);
  }

  @Test
  public void findVoteInProposal() {
    val user = new User()
        .setId(UUID.randomUUID())
        .setRank(rand.nextDouble());
    val proposal = new Proposal()
        .setId(UUID.randomUUID());
    val vote = new Vote()
        .setUser(user)
        .setProposal(proposal)
        .setOption(VoteOption.AGAINST);
    votesRepository.upsert(vote);
    assertEquals(1L, collection.count());
    rankRepository.upsert(user.getId(), user.getRank());

    val voteFound = votesRepository.findVoteInProposal(vote.getProposal().getId(),
        vote.getUser().getId());

    assertEquals(vote.getProposal(), voteFound.getProposal());
    assertEquals(vote.getUser().getId(), voteFound.getUser().getId());
    assertEquals(0.0, voteFound.getUser().getRank(), 0.1);
    assertEquals(VoteOption.AGAINST, voteFound.getOption());
  }
  
}
