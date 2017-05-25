package eu.trustdemocracy.votes.core.interactors.proposal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import eu.trustdemocracy.votes.core.models.request.ProposalRequestDTO;
import eu.trustdemocracy.votes.core.models.response.ProposalResponseDTO;
import eu.trustdemocracy.votes.gateways.repositories.fake.FakeProposalsRepository;
import eu.trustdemocracy.votes.gateways.out.FakeRankerGateway;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UnregisterProposalTest {

  private FakeProposalsRepository proposalsRepository;

  @BeforeEach
  public void init() {
    this.proposalsRepository = new FakeProposalsRepository();
  }

  @Test
  public void unregisterProposal() {
    Long dueDate = System.currentTimeMillis();
    val proposalId = UUID.randomUUID();

    val proposalRequest = new ProposalRequestDTO()
        .setId(proposalId)
        .setDueDate(dueDate);

    new RegisterProposal(proposalsRepository, new FakeRankerGateway()).execute(proposalRequest);

    val registeredProposal = proposalsRepository.proposals.get(proposalId);

    assertTrue(registeredProposal.isActive());

    val interactor = new UnregisterProposal(proposalsRepository);

    ProposalResponseDTO proposalResponse = interactor.execute(proposalRequest);

    val unregisteredProposal = proposalsRepository.proposals.get(proposalId);
    assertFalse(unregisteredProposal.isActive());

    assertEquals(dueDate, proposalResponse.getDueDate());
    assertEquals(proposalId, proposalResponse.getId());
  }

}
