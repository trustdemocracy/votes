package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class FakeProposalsGateway implements ProposalsGateway {

  public Map<UUID, Map<VoteOption, Double>> proposals = new HashMap<>();

  @Override
  public void update(UUID proposalId, Map<VoteOption, Double> results) {
    proposals.put(proposalId, results);
  }

  @Override
  public void updateBatch(Map<Proposal, Map<VoteOption, Double>> proposals) {
    for (val proposal : proposals.entrySet()) {
      if (!proposal.getKey().isExpired()) {
        this.proposals.put(proposal.getKey().getId(), proposal.getValue());
      }
    }
  }
}
