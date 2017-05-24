package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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

  @Override
  public Set<Proposal> findAllActive() {
    return proposals.values().stream()
        .filter(p -> p.isActive() && !p.isExpired())
        .collect(Collectors.toSet());
  }

  @Override
  public void updateExpired(Set<Proposal> expiredProposals) {
    for (val proposal : expiredProposals) {
      proposals.get(proposal.getId()).setExpired(proposal.isExpired());
    }
  }
}
