package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.HashMap;
import java.util.Map;

public class FakeVotesRepository implements VotesRepository {

  public Map<String, VoteOption> votes = new HashMap<>();

  @Override
  public void upsert(Vote vote) {
    String key = vote.getProposal().getId() + "|" + vote.getUser().getId();
    votes.put(key, vote.getOption());
  }
}
