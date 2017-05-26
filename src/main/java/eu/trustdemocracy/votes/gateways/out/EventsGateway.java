package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Map;

public interface EventsGateway {

  void createVoteEvent(Vote vote, Map<VoteOption, Double> results);
}
