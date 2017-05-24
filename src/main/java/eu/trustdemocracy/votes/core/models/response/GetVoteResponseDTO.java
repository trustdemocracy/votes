package eu.trustdemocracy.votes.core.models.response;

import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetVoteResponseDTO {

  private UUID userId;
  private UUID proposalId;
  private VoteOption option;
  private Double rank;
  private boolean proposalLocked;
}
