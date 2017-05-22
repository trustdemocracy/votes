package eu.trustdemocracy.votes.core.interactors.proposal;

import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;

public class UnregisterProposal implements Interactor<ProposalRequestDTO, ProposalResponseDTO> {

  public UnregisterProposal(ProposalsRepository proposalsRepository) {
  }

  @Override
  public ProposalResponseDTO execute(ProposalRequestDTO requestDTO) {
    return null;
  }
}
