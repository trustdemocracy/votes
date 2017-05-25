package eu.trustdemocracy.votes.infrastructure;

import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.gateways.out.ProposalsGateway;
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

  private RankRepository getRankRepository() {
    return null;
  }

  private ProposalsRepository getProposalsRepository() {
    return null;
  }

  private VotesRepository getVotesRepository() {
    return null;
  }

  private ProposalsGateway getProposalsGateway() {
    return null;
  }
}
