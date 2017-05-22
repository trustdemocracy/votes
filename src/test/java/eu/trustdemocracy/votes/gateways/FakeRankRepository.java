package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.User;

public class FakeRankRepository implements RankRepository {

  @Override
  public boolean upsert(User user) {
    return false;
  }
}
