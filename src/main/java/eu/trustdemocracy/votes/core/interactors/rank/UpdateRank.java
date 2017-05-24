package eu.trustdemocracy.votes.core.interactors.rank;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.ProposalsGateway;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.RankRepository;
import eu.trustdemocracy.votes.gateways.VotesRepository;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.val;

public class UpdateRank implements Interactor<RankRequestDTO, RankResponseDTO> {

  private RankRepository rankRepository;
  private ProposalsRepository proposalsRepository;
  private VotesRepository votesRepository;
  private ProposalsGateway proposalsGateway;

  public UpdateRank(
      RankRepository rankRepository,
      ProposalsRepository proposalsRepository,
      VotesRepository votesRepository,
      ProposalsGateway proposalsGateway
  ) {
    this.rankRepository = rankRepository;
    this.proposalsRepository = proposalsRepository;
    this.proposalsGateway = proposalsGateway;
  }

  @Override
  public RankResponseDTO execute(RankRequestDTO requestDTO) {
    val response = new RankResponseDTO();

    Map<UUID, Double> rankings = requestDTO.getRankings();
    val successful = rankRepository.upsertBatch(rankings);


//    proposalsGateway.update()

    response.setSuccessful(successful);
    return response;
  }

  private Set<Proposal> reduceVotes(Set<Proposal> proposals) {
    return proposals;

  }
}
