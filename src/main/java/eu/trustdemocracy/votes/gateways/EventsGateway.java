package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;

public interface EventsGateway {

  void createVoteEvent(Vote vote);
}
