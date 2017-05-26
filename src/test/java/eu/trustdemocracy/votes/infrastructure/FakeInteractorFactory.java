package eu.trustdemocracy.votes.infrastructure;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.votes.core.interactors.proposal.RegisterProposal;
import eu.trustdemocracy.votes.core.interactors.proposal.UnregisterProposal;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.core.interactors.vote.GetVote;
import eu.trustdemocracy.votes.core.interactors.vote.VoteProposal;
import eu.trustdemocracy.votes.gateways.out.EventsGateway;
import eu.trustdemocracy.votes.gateways.out.FakeEventsGateway;
import eu.trustdemocracy.votes.gateways.out.FakeProposalsGateway;
import eu.trustdemocracy.votes.gateways.out.FakeRankerGateway;
import eu.trustdemocracy.votes.gateways.out.ProposalsGateway;
import eu.trustdemocracy.votes.gateways.out.RankerGateway;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.RankRepository;
import eu.trustdemocracy.votes.gateways.repositories.VotesRepository;
import eu.trustdemocracy.votes.gateways.repositories.mongo.MongoProposalsRepository;
import eu.trustdemocracy.votes.gateways.repositories.mongo.MongoRankRepository;
import eu.trustdemocracy.votes.gateways.repositories.mongo.MongoVotesRepository;
import lombok.val;

public class FakeInteractorFactory implements InteractorFactory {

  private MongoDatabase db;

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
    return new MongoRankRepository(getDB());
  }

  private ProposalsRepository getProposalsRepository() {
    return new MongoProposalsRepository(getDB());
  }

  private VotesRepository getVotesRepository() {
    return new MongoVotesRepository(getDB());
  }

  private ProposalsGateway getProposalsGateway() {
    return new FakeProposalsGateway();
  }

  private RankerGateway getRankerGateway() {
    return new FakeRankerGateway();
  }

  private EventsGateway getEventsGateway() {
    return new FakeEventsGateway();
  }

  private MongoDatabase getDB() {
    if (db == null) {
      val fongo = new Fongo("test server");
      db = fongo.getDatabase("test_database");
    }
    return db;
  }
}
