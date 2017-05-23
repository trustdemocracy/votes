package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.HashMap;
import java.util.Map;

public class FakeVotesRepository implements VotesRepository {

  public Map<String, VoteOption> votes = new HashMap<>();

  @Override
  public void upsert(Vote vote) {
    String key = getKey(vote);
    votes.put(key, vote.getOption());
  }

  @Override
  public void remove(Vote vote) {
    String key = getKey(vote);
    votes.remove(key);
  }

  private static String getKey(Vote vote) {
    return vote.getProposal().getId() + "|" + vote.getUser().getId();
  }
}
