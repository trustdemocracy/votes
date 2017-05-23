package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeProposalsGateway implements ProposalsGateway {

  public Map<UUID, Map<VoteOption, Double>> proposals = new HashMap<>();

}
