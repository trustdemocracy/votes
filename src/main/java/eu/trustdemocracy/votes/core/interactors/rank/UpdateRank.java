package eu.trustdemocracy.votes.core.interactors.rank;

import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.RankRepository;

public class UpdateRank implements Interactor<RankRequestDTO, RankResponseDTO> {

  public UpdateRank(RankRepository rankRepository) {

  }

  @Override
  public RankResponseDTO execute(RankRequestDTO rankRequestDTO) {
    return null;
  }
}
