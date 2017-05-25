package eu.trustdemocracy.votes.gateways.out;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import eu.trustdemocracy.votes.gateways.out.ProposalsGateway;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.val;

public class FakeProposalsGateway implements ProposalsGateway {

  public Map<UUID, Map<VoteOption, Double>> proposals = new HashMap<>();

  @Override
  public void updateProposal(Vote vote, VoteOption previousOption) {
    val proposalId = vote.getProposal().getId();
    val option = vote.getOption();
    val userRank = vote.getUser().getRank();

    Map<VoteOption, Double> currentProposal = proposals.get(proposalId);
    if (currentProposal == null) {
      currentProposal = new HashMap<>();
    }

    if (previousOption != null) {
      val previouslyVoted = currentProposal.get(previousOption);
      assert previouslyVoted != null;

      currentProposal.put(previousOption, previouslyVoted - userRank);
    }

    if (option != VoteOption.WITHDRAW) {
      val currentValue = currentProposal.get(option);
      currentProposal.put(option, currentValue != null ? currentValue + userRank : userRank);
    }

    proposals.put(proposalId, currentProposal);
  }

  @Override
  public void updateBatch(Map<Proposal, Map<VoteOption, Double>> proposals) {
    for (val proposal : proposals.entrySet()) {
      if (!proposal.getKey().isExpired()) {
        this.proposals.put(proposal.getKey().getId(), proposal.getValue());
      }
    }
  }
}
