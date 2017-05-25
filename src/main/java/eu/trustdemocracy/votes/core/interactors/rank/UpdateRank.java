package eu.trustdemocracy.votes.core.interactors.rank;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.RankRequestDTO;
import eu.trustdemocracy.votes.core.models.response.RankResponseDTO;
import eu.trustdemocracy.votes.gateways.ProposalsGateway;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    this.votesRepository = votesRepository;
    this.proposalsGateway = proposalsGateway;
  }

  @Override
  public RankResponseDTO execute(RankRequestDTO requestDTO) {
    assert requestDTO.getRankings() != null;
    assert requestDTO.getCalculatedTime() != null;

    Map<UUID, Double> rankings = requestDTO.getRankings();

    val activeProposals = proposalsRepository.findAllActive();

    sealExpiredProposals(activeProposals, requestDTO.getCalculatedTime());

    val successful = rankRepository.upsertBatch(rankings);

    val proposals = reduceVotes(activeProposals, requestDTO.getCalculatedTime());

    proposalsGateway.updateBatch(proposals);

    return new RankResponseDTO().setSuccessful(successful);
  }

  private void sealExpiredProposals(Set<Proposal> activeProposals, long calculatedTime) {
    Set<Proposal> expiredProposals = activeProposals.parallelStream()
        .filter(p -> isExpired(p, calculatedTime))
        .collect(Collectors.toSet());
    votesRepository.updateExpired(expiredProposals);
    proposalsRepository.updateExpired(expiredProposals);
  }

  private Map<Proposal, Map<VoteOption, Double>> reduceVotes(Set<Proposal> proposals,
      long calculatedTime) {
    Map<Proposal, Map<VoteOption, Double>> result = new HashMap<>();

    return proposals.parallelStream()
        .map(p -> p.setExpired(isExpired(p, calculatedTime)))
        .reduce(result,
            (map, proposal) -> {
              if (proposal.isExpired()) {
                map.put(proposal, null);
              } else {
                val options = votesRepository.findProposalResults(proposal.getId());
                map.put(proposal, options);
              }
              return map;
            },
            (m1, m2) -> {
              m1.putAll(m2);
              return m1;
            }
        );
  }

  private static boolean isExpired(Proposal proposal, long lastCalculatedTime) {
    return proposal.getDueDate() <= lastCalculatedTime;
  }
}
