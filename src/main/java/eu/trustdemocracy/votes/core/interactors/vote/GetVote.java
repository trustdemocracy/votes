package eu.trustdemocracy.votes.core.interactors.vote;

import eu.trustdemocracy.votes.core.entities.utils.UserMapper;
import eu.trustdemocracy.votes.core.entities.utils.VoteMapper;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.GetVoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.VotesRepository;
import lombok.val;

public class GetVote implements Interactor<GetVoteRequestDTO, VoteResponseDTO> {

  private VotesRepository votesRepository;

  public GetVote(VotesRepository votesRepository) {
    this.votesRepository = votesRepository;
  }

  @Override
  public VoteResponseDTO execute(GetVoteRequestDTO requestDTO) {
    assert requestDTO.getProposalId() != null;
    assert requestDTO.getUserToken() != null;

    val user = UserMapper.createEntity(requestDTO.getUserToken());

    val vote = votesRepository.findWithRank(requestDTO.getProposalId(), user.getId());

    return VoteMapper.createResponse(vote);
  }
}
