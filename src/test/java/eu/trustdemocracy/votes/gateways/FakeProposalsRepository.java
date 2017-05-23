package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class FakeProposalsRepository implements ProposalsRepository {

  public Map<UUID, Proposal> proposals = new HashMap<>();

  @Override
  public boolean upsert(Proposal proposal) {
    val previousValue = proposals.put(proposal.getId(), proposal);

    if (previousValue != null) {
      proposals.replace(proposal.getId(), proposal);
    }

    return false;
  }

  @Override
  public Proposal find(UUID id) {
    return proposals.get(id);
  }
}
