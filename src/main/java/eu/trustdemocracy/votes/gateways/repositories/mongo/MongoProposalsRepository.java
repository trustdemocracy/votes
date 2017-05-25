package eu.trustdemocracy.votes.gateways.repositories.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.gateways.repositories.ProposalsRepository;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
    val condition = and(
        eq("id", id.toString())
    );
    val document = collection.find(condition).first();
    if (document == null) {
      return null;
    }
    return buildFromDocument(document);
  }

  @Override
  public Set<Proposal> findAllActive() {
    Set<Proposal> proposals = new HashSet<>();

    val condition = and(
        eq("active", true)
    );
    val documents = collection.find(condition);
    for (val document : documents) {
      proposals.add(buildFromDocument(document));
    }

    return proposals;
  }

  @Override
  public void updateExpired(Set<Proposal> expiredProposals) {
    val ids = expiredProposals.stream()
        .map(p -> p.getId().toString())
        .collect(Collectors.toSet());

    collection.updateMany(
        in("id", ids),
        new Document("$set", new Document("expired", true))
    );
  }

  private static Proposal buildFromDocument(Document document) {
    return new Proposal()
        .setId(UUID.fromString(document.getString("id")))
        .setDueDate(document.getLong("dueDate"))
        .setActive(document.getBoolean("active"))
        .setExpired(document.getBoolean("expired"));
  }
}
