package eu.trustdemocracy.votes.core.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Vote {

  private Proposal proposal;
  private User user;
  private VoteOption option;
}
