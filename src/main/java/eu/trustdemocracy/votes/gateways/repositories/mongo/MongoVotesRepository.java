package eu.trustdemocracy.votes.gateways.repositories.mongo;

import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MongoVotesRepository implements VotesRepository {

  public MongoVotesRepository(MongoDatabase db) {
  }

  @Override
  public VoteOption upsert(Vote vote) {
    return null;
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
