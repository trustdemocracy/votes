package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;

public interface VotesRepository {

  VoteOption upsert(Vote vote);

  VoteOption remove(Vote vote);
}
