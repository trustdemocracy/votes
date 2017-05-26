package eu.trustdemocracy.votes.infrastructure;

import eu.trustdemocracy.votes.core.interactors.proposal.RegisterProposal;
import eu.trustdemocracy.votes.core.interactors.proposal.UnregisterProposal;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.core.interactors.vote.GetVote;
import eu.trustdemocracy.votes.core.interactors.vote.VoteProposal;

public interface InteractorFactory {

  UpdateRank getUpdateRank();

  RegisterProposal getRegisterProposal();

  UnregisterProposal getUnregisterProposal();

  VoteProposal getVoteProposal();

  GetVote getGetVote();
}
