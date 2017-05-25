package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Map;

public interface ProposalsGateway {

  void updateProposal(Vote vote, VoteOption voteOption);

  void updateBatch(Map<Proposal, Map<VoteOption,Double>> proposals);
}
