package eu.trustdemocracy.votes.core.interactors.rank;

import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.RankRepository;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class UpdateRank implements Interactor<RankRequestDTO, RankResponseDTO> {

  private RankRepository rankRepository;

  public UpdateRank(RankRepository rankRepository) {
    this.rankRepository = rankRepository;
  }

  @Override
  public RankResponseDTO execute(RankRequestDTO requestDTO) {
    val response = new RankResponseDTO();

    Map<UUID, Double> rankings = requestDTO.getRankings();
    val successful = rankRepository.upsertBatch(rankings);

    response.setSuccessful(successful);

    return response;
  }
}
