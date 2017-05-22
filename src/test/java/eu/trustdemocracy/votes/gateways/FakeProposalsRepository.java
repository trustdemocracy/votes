package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class FakeProposalsRepository implements ProposalsRepository {

  public Map<UUID, Long> proposals = new HashMap<>();

  @Override
  public boolean upsert(Proposal proposal) {
    val previousValue = proposals.put(proposal.getId(), proposal.getDueDate());
    return null != previousValue;
  }
}
