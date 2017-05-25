package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Map;
import java.util.UUID;

public interface ProposalsGateway {

  void update(UUID proposalId, Map<VoteOption,Double> results);

  void updateBatch(Map<Proposal, Map<VoteOption,Double>> proposals);
}
