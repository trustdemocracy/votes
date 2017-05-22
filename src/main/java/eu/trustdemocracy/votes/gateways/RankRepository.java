package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.User;

public interface RankRepository {

  boolean upsert(User user);
}
