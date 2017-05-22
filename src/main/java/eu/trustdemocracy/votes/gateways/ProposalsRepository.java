package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Proposal;

public interface ProposalsRepository {

  boolean upsert(Proposal proposal);
}
