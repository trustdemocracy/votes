package eu.trustdemocracy.votes.core.interactors.vote;

import eu.trustdemocracy.votes.core.entities.utils.UserMapper;
import eu.trustdemocracy.votes.core.entities.utils.VoteMapper;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.votes.core.models.request.GetVoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import lombok.val;

public class GetVote implements Interactor<GetVoteRequestDTO, VoteResponseDTO> {

  private VotesRepository votesRepository;
  private ProposalsRepository proposalsRepository;

  public GetVote(VotesRepository votesRepository, ProposalsRepository proposalsRepository) {
    this.votesRepository = votesRepository;
    this.proposalsRepository = proposalsRepository;
  }

  @Override
  public VoteResponseDTO execute(GetVoteRequestDTO requestDTO) {
    assert requestDTO.getProposalId() != null;
    assert requestDTO.getUserToken() != null;

    val user = UserMapper.createEntity(requestDTO.getUserToken());

    val propsoalId = requestDTO.getProposalId();
    val proposal = proposalsRepository.find(propsoalId);
    if (proposal == null || !proposal.isActive()) {
      throw new ResourceNotFoundException("Proposal with id ["
          + propsoalId + "] does not exist or it is unpublished");
    }

    val vote = votesRepository.findVoteInProposal(propsoalId, user.getId());
    if (vote == null) {
      return null;
    }
    vote.setProposal(proposal);
    return VoteMapper.createResponse(vote);
  }
}
