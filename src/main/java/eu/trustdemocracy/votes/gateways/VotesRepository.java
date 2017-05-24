package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Map;
import java.util.UUID;

public interface VotesRepository {

  VoteOption upsert(Vote vote);

  VoteOption remove(Vote vote);

  Map<VoteOption, Double> findWithRank(UUID id);
}
