package eu.trustdemocracy.votes.core.interactors.vote;

import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.EventsGateway;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.RankRepository;
import eu.trustdemocracy.votes.gateways.VotesRepository;

public class Vote implements Interactor<VoteRequestDTO, VoteResponseDTO> {

  public Vote(
      VotesRepository votesRepository,
      ProposalsRepository proposalsRepository,
      RankRepository rankRepository,
      EventsGateway eventsGateway
  ) {
  }

  @Override
  public VoteResponseDTO execute(VoteRequestDTO voteRequestDTO) {
    return null;
  }
}
