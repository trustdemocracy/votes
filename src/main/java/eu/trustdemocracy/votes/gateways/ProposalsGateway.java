package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;

public interface ProposalsGateway {

  void updateProposal(Vote vote, VoteOption voteOption);
}
