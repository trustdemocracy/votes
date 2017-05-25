package eu.trustdemocracy.votes.infrastructure;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.votes.core.interactors.rank.UpdateRank;
import eu.trustdemocracy.votes.gateways.out.FakeProposalsGateway;
import eu.trustdemocracy.votes.gateways.out.ProposalsGateway;
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

  private MongoDatabase getDB() {
    if (db == null) {
      val fongo = new Fongo("test server");
      db = fongo.getDatabase("test_database");
    }
    return db;
  }
}
