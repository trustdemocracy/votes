package eu.trustdemocracy.votes.gateways;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeProposalsRepository implements ProposalsRepository {
  public Map<UUID, Long> proposals = new HashMap<>();

}
