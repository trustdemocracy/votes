package eu.trustdemocracy.votes.core.interactors.proposal;

import eu.trustdemocracy.votes.core.entities.utils.ProposalMapper;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.out.RankerGateway;
import lombok.val;

public class RegisterProposal implements Interactor<ProposalRequestDTO, ProposalResponseDTO> {

  private ProposalsRepository proposalsRepository;
  private RankerGateway rankerGateway;

  public RegisterProposal(ProposalsRepository proposalsRepository, RankerGateway rankerGateway) {
    this.proposalsRepository = proposalsRepository;
    this.rankerGateway = rankerGateway;
  }

  @Override
  public ProposalResponseDTO execute(ProposalRequestDTO requestDTO) {
    val proposal = ProposalMapper.createEntity(requestDTO);

    proposal.setActive(true);
    val existedBefore = proposalsRepository.upsert(proposal);
    if (!existedBefore) {
      rankerGateway.addDueDate(proposal.getDueDate());
    }

    return ProposalMapper.createResponse(proposal);
  }
}
