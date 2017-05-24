package eu.trustdemocracy.votes.core.interactors.vote;

import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.GetVoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.GetVoteResponseDTO;
import eu.trustdemocracy.votes.gateways.VotesRepository;

public class GetVote implements Interactor<GetVoteRequestDTO, GetVoteResponseDTO> {

  public GetVote(VotesRepository votesRepository) {
  }

  @Override
  public GetVoteResponseDTO execute(GetVoteRequestDTO getVoteRequestDTO) {
    return null;
  }
}
