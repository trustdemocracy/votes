package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.gateways.out.EventsGateway;

public class FakeEventsGateway implements EventsGateway {

  @Override
  public void createVoteEvent(Vote vote) {

  }
}
