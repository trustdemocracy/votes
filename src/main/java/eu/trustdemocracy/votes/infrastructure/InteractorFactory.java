package eu.trustdemocracy.votes.infrastructure;

import eu.trustdemocracy.votes.core.interactors.proposal.RegisterProposal;
import eu.trustdemocracy.votes.core.interactors.proposal.UnregisterProposal;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;

public interface InteractorFactory {

  UpdateRank getUpdateRank();

  RegisterProposal getRegisterProposal();

  UnregisterProposal getUnregisterProposal();
}
