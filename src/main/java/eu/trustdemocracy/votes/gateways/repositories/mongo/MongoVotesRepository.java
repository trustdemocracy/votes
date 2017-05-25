package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.User;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoVotesRepository implements VotesRepository {

  private static final String RANK_COLLECTION = "votes_rank";
  private static final String VOTES_COLLECTION = "votes";
  private MongoCollection<Document> collection;
  private MongoCollection<Document> rankCollection;
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
    val userId = vote.getUser().getId();
    val proposalId = vote.getProposal().getId();

    val condition = and(
        eq("userId", userId.toString()),
        eq("proposalId", proposalId.toString())
    );

    val document = collection.find(condition).first();
    if (document == null) {
      return null;
    }

    collection.deleteOne(eq("_id", document.getObjectId("_id")));

    return VoteOption.valueOf(document.getString("option"));
  }

  @Override
  public Map<VoteOption, Double> findProposalResults(UUID proposalId) {
    Map<VoteOption, Set<String>> userIds = new HashMap<>();

    val votes = collection.find(eq("proposalId", proposalId.toString()));
    for (val doc : votes) {
      val option = VoteOption.valueOf(doc.getString("option"));
      Set<String> set = userIds.get(option);
      if (set == null) {
        set = new HashSet<>();
      }
      set.add(doc.getString("userId"));
      userIds.put(option, set);
    }

    Map<VoteOption, Double> results = new HashMap<>();

    for (val option : VoteOption.values()) {
      if (!option.equals(VoteOption.WITHDRAW)) {
        results.put(option, 0.0);
      }
    }

    for (val option : userIds.keySet()) {
      val ids = userIds.get(option);

      val total = getRankCollection().aggregate(Arrays.asList(
          Aggregates.match(in("id", ids)),
          Aggregates.group(null, Accumulators.sum("total", "$rank"))
      ))
          .first()
          .getDouble("total");

      results.put(option, total);
    }

    return results;
  }

  @Override
  public Vote findVoteInProposal(UUID proposalId, UUID userId) {

    val condition = and(
        eq("userId", userId.toString()),
        eq("proposalId", proposalId.toString())
    );
    val voteDoc = collection.find(condition).first();
    if (voteDoc == null) {
      return null;
    }

    Double rank = voteDoc.getDouble("rank");
    rank = rank == null ? 0.0 : rank;
    val option = VoteOption.valueOf(voteDoc.getString("option"));

    return new Vote()
        .setProposal(new Proposal().setId(proposalId))
        .setUser(new User().setId(userId).setRank(rank))
        .setOption(option);
  }

  @Override
  public void sealVotes(Set<Proposal> expiredProposals) {
    for (val proposal : expiredProposals) {
      collection.find(eq("proposalId", proposal.getId().toString()))
          .forEach((Block<Document>) doc -> {
            val id = doc.getString("userId");
            val rankDoc = getRankCollection().find(eq("id", id)).first();
            val rank = rankDoc.getDouble("rank") == null ? 0.0 : rankDoc.getDouble("rank");
            doc.put("rank", rank);
            collection.replaceOne(eq("_id", doc.getObjectId("_id")), doc);
          });
    }
  }

  private MongoCollection<Document> getRankCollection() {
    if (rankCollection == null) {
      rankCollection = db.getCollection(RANK_COLLECTION);
    }
    return rankCollection;
  }
}
