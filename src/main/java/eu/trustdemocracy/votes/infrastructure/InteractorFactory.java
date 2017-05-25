package eu.trustdemocracy.votes.infrastructure;

import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;

public interface InteractorFactory {

  UpdateRank getUpdateRank();
}
