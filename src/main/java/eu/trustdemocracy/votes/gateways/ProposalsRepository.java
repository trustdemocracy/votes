package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;
import java.util.UUID;

public interface ProposalsRepository {

  boolean upsert(Proposal proposal);

  Proposal find(UUID id);
}
