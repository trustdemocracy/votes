package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Vote;

public interface EventsGateway {

  void createVoteEvent(Vote vote);
}
