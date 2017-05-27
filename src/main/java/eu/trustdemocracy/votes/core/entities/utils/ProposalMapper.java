package eu.trustdemocracy.votes.core.entities.utils;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;

public final class ProposalMapper {

  public static Proposal createEntity(ProposalRequestDTO requestDTO) {
    return new Proposal()
        .setId(requestDTO.getId())
        .setTitle(requestDTO.getTitle())
        .setDueDate(requestDTO.getDueDate());
  }

  public static ProposalResponseDTO createResponse(Proposal proposal) {
    return new ProposalResponseDTO()
        .setId(proposal.getId())
        .setDueDate(proposal.getDueDate());
  }
}
