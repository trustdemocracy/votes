package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;

public interface VotesRepository {

  void upsert(Vote vote);
}
