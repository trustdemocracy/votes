package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.interactors.vote.Vote;
import java.util.ArrayList;
import java.util.List;

public class FakeVotesRepository implements VotesRepository {

  public List<Vote> votes = new ArrayList<>();
}
