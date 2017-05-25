package eu.trustdemocracy.votes.gateways.repositories.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import java.util.Set;
import java.util.UUID;
import org.bson.Document;

public class MongoProposalsRepository implements ProposalsRepository {

  private static final String PROPOSALS_COLLECTION = "proposals";
  private MongoCollection<Document> collection;

  public MongoProposalsRepository(MongoDatabase db) {
    this.collection = db.getCollection(PROPOSALS_COLLECTION);
  }

  @Override
  public boolean upsert(Proposal proposal) {
    return false;
  }

  @Override
  public Proposal find(UUID id) {
    return null;
  }

  @Override
  public Set<Proposal> findAllActive() {
    return null;
  }

  @Override
  public void updateExpired(Set<Proposal> expiredProposals) {

  }
}
