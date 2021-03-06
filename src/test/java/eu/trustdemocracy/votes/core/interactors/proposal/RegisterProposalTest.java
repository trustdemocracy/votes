package eu.trustdemocracy.votes.core.interactors.proposal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.repositories.fake.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.out.FakeRankerGateway;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterProposalTest {

  private FakeProposalsRepository proposalsRepository;
  private FakeRankerGateway rankerGateway;

  @BeforeEach
  public void init() {
    this.proposalsRepository = new FakeProposalsRepository();
    this.rankerGateway = new FakeRankerGateway();
  }

  @Test
  public void registerProposal() {
    Long dueDate = System.currentTimeMillis();
    val proposalId = UUID.randomUUID();

    val proposalRequest = new ProposalRequestDTO()
        .setId(proposalId)
        .setDueDate(dueDate);

    val interactor = new RegisterProposal(proposalsRepository, rankerGateway);

    ProposalResponseDTO proposalResponse = interactor.execute(proposalRequest);

    assertEquals(dueDate, proposalResponse.getDueDate());
    assertEquals(proposalId, proposalResponse.getId());

    val registeredProposal = proposalsRepository.proposals.get(proposalId);

    assertEquals(proposalId, registeredProposal.getId());
    assertEquals(dueDate, registeredProposal.getDueDate());
    assertTrue(registeredProposal.isActive());
    assertTrue(rankerGateway.dueDates.contains(dueDate));
  }
}
