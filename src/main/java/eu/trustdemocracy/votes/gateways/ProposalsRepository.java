package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;
import java.util.Set;
import java.util.UUID;

public interface ProposalsRepository {

  boolean upsert(Proposal proposal);

  Proposal find(UUID id);

  Set<Proposal> findAllActive();

  void updateExpired(Set<Proposal> expiredProposals);
}
