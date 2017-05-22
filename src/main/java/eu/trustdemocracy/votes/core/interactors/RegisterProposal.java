package eu.trustdemocracy.votes.core.interactors;

import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.RankerGateway;

public class RegisterProposal implements Interactor<ProposalRequestDTO, ProposalResponseDTO> {

  public RegisterProposal(ProposalsRepository proposalsRepository, RankerGateway rankerGateway) {
  }

  @Override
  public ProposalResponseDTO execute(ProposalRequestDTO proposalRequestDTO) {
    return null;
  }
}
