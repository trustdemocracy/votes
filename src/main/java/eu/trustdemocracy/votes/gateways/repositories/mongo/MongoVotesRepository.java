package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoVotesRepository implements VotesRepository {

  private static final String RANK_COLLECTION = "votes_rank";
  private static final String VOTES_COLLECTION = "votes";
  private MongoCollection<Document> collection;
  private MongoDatabase db;

  public MongoVotesRepository(MongoDatabase db) {
    this.db = db;
    this.collection = db.getCollection(VOTES_COLLECTION);
  }

  @Override
  public VoteOption upsert(Vote vote) {
    val userId = vote.getUser().getId();
    val proposalId = vote.getProposal().getId();
    val option = vote.getOption();

    val document = new Document("userId", userId.toString())
        .append("proposalId", proposalId.toString())
        .append("option", option.toString());

    val condition = and(
        eq("userId", userId.toString()),
        eq("proposalId", proposalId.toString())
    );
    val options = new UpdateOptions().upsert(true);
    val rel = collection.replaceOne(condition, document, options);
    return vote.getOption();
  }

  @Override
  public VoteOption remove(Vote vote) {
    return null;
  }

  @Override
  public Map<VoteOption, Double> findWithRank(UUID proposalId) {
    return null;
  }

  @Override
  public Vote findWithRank(UUID proposalId, UUID userId) {
    return null;
  }

  @Override
  public void updateExpired(Set<Proposal> expiredProposals) {

  }
}
