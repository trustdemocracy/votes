package eu.trustdemocracy.votes.core.interactors.vote;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.core.entities.utils.VoteMapper;
import eu.trustdemocracy.votes.core.interactors.Interactor;
import eu.trustdemocracy.votes.core.interactors.exceptions.ProposalDueException;
import eu.trustdemocracy.votes.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;
import eu.trustdemocracy.votes.gateways.EventsGateway;
import eu.trustdemocracy.votes.gateways.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.RankRepository;
import eu.trustdemocracy.votes.gateways.VotesRepository;
import lombok.val;

public class VoteProposal implements Interactor<VoteRequestDTO, VoteResponseDTO> {

  private VotesRepository votesRepository;
  private ProposalsRepository proposalsRepository;
  private RankRepository rankRepository;
  private EventsGateway eventsGateway;

  public VoteProposal(
      VotesRepository votesRepository,
      ProposalsRepository proposalsRepository,
      RankRepository rankRepository,
      EventsGateway eventsGateway
  ) {
    this.votesRepository = votesRepository;
    this.proposalsRepository = proposalsRepository;
    this.rankRepository = rankRepository;
    this.eventsGateway = eventsGateway;
  }

  @Override
  public VoteResponseDTO execute(VoteRequestDTO voteRequestDTO) {
    val vote = VoteMapper.createEntity(voteRequestDTO);

    Proposal proposal = proposalsRepository.find(vote.getProposal().getId());
    if (proposal == null) {
      throw new ResourceNotFoundException("Trying to vote non-existing proposal with id ["
          + vote.getProposal().getId() + "]");
    }
    checkProposalIsValid(proposal);

    if (isWithdrawing(vote)) {
      votesRepository.remove(vote);
    } else {
      votesRepository.upsert(vote);
    }

    val rank = rankRepository.find(vote.getUser().getId());
    vote.getUser().setRank(rank);

    eventsGateway.createVoteEvent(vote);

    return VoteMapper.createResponse(vote);
  }

  private static void checkProposalIsValid(Proposal proposal) {
    if (!proposal.isActive()) {
      throw new ResourceNotFoundException("Trying to vote inactive proposal with id ["
          + proposal.getId() + "]");
    }

    if (proposal.getDueDate() <= System.currentTimeMillis()) {
      throw new ProposalDueException("The proposal [" + proposal.getId() + "] is due");
    }
  }

  private static boolean isWithdrawing(Vote vote) {
    return vote.getOption().equals(VoteOption.WITHDRAW);
  }
}
