package eu.trustdemocracy.votes.infrastructure;

import eu.trustdemocracy.votes.core.interactors.proposal.RegisterProposal;
import eu.trustdemocracy.votes.core.interactors.proposal.UnregisterProposal;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.core.interactors.vote.GetVote;
import eu.trustdemocracy.votes.core.interactors.vote.VoteProposal;
import eu.trustdemocracy.votes.gateways.out.EventsGateway;
import eu.trustdemocracy.votes.gateways.out.EventsGatewayImpl;
import eu.trustdemocracy.votes.gateways.out.ProposalsGateway;
import eu.trustdemocracy.votes.gateways.out.ProposalsGatewayImpl;
import eu.trustdemocracy.votes.gateways.out.RankerGateway;
import eu.trustdemocracy.votes.gateways.out.RankerGatewayImpl;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;

public class DefaultInteractorFactory implements InteractorFactory {

  private static DefaultInteractorFactory instance;

  private DefaultInteractorFactory() {
  }

  public static DefaultInteractorFactory getInstance() {
    if (instance == null) {
      instance = new DefaultInteractorFactory();
    }
    return instance;
  }

  @Override
  public UpdateRank getUpdateRank() {
    return new UpdateRank(
        getRankRepository(),
        getProposalsRepository(),
        getVotesRepository(),
        getProposalsGateway()
    );
  }

  @Override
  public RegisterProposal getRegisterProposal() {
    return new RegisterProposal(getProposalsRepository(), getRankerGateway());
  }

  @Override
  public UnregisterProposal getUnregisterProposal() {
    return new UnregisterProposal(getProposalsRepository());
  }

  @Override
  public VoteProposal getVoteProposal() {
    return new VoteProposal(
        getVotesRepository(),
        getProposalsRepository(),
        getRankRepository(),
        getEventsGateway(),
        getProposalsGateway()
    );
  }

  @Override
  public GetVote getGetVote() {
    return new GetVote(
        getVotesRepository(),
        getProposalsRepository()
    );
  }

  private RankRepository getRankRepository() {
    return RepositoryFactory.getRankRepository();
  }

  private ProposalsRepository getProposalsRepository() {
    return RepositoryFactory.getProposalsRepository();
  }

  private VotesRepository getVotesRepository() {
    return RepositoryFactory.getVotesRepository();
  }

  private ProposalsGateway getProposalsGateway() {
    return new ProposalsGatewayImpl();
  }

  private RankerGateway getRankerGateway() {
    return new RankerGatewayImpl();
  }

  private EventsGateway getEventsGateway() {
    return new EventsGatewayImpl();
  }
}
