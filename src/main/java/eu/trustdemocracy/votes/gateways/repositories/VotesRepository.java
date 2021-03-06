package eu.trustdemocracy.votes.gateways.repositories;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface VotesRepository {

  VoteOption upsert(Vote vote);

  VoteOption remove(Vote vote);

  Map<VoteOption, Double> findProposalResults(UUID proposalId);

  Vote findVoteInProposal(UUID proposalId, UUID userId);

  void sealVotes(Set<Proposal> expiredProposals);
}
