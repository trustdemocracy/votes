package eu.trustdemocracy.votes.core.interactors.proposal;

import eu.trustdemocracy.votes.core.entities.utils.ProposalMapper;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;
import lombok.val;

public class UnregisterProposal implements Interactor<ProposalRequestDTO, ProposalResponseDTO> {

  private ProposalsRepository proposalsRepository;

  public UnregisterProposal(ProposalsRepository proposalsRepository) {
    this.proposalsRepository = proposalsRepository;
  }

  @Override
  public ProposalResponseDTO execute(ProposalRequestDTO requestDTO) {
    val proposal = ProposalMapper.createEntity(requestDTO);

    proposal.setActive(false);

    val foundProposal = proposalsRepository.find(proposal.getId());
    proposal.setDueDate(foundProposal.getDueDate());

    proposalsRepository.upsert(proposal);

    return ProposalMapper.createResponse(proposal);
  }
}
