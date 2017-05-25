package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import java.util.Set;
import java.util.UUID;
import lombok.val;
import org.bson.Document;

public class MongoProposalsRepository implements ProposalsRepository {

  private static final String PROPOSALS_COLLECTION = "proposals";
  private MongoCollection<Document> collection;

  public MongoProposalsRepository(MongoDatabase db) {
    this.collection = db.getCollection(PROPOSALS_COLLECTION);
  }

  /**
   * @return whether the proposal existed before or not
   */
  @Override
  public boolean upsert(Proposal proposal) {
    val document = new Document("id", proposal.getId().toString())
        .append("dueDate", proposal.getDueDate())
        .append("active", proposal.isActive())
        .append("expired", proposal.isExpired());

    val condition = and(
        eq("id", proposal.getId().toString())
    );
    val options = new UpdateOptions().upsert(true);
    val updateResult = collection.replaceOne(condition, document, options);

    return updateResult.getUpsertedId() == null;
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
