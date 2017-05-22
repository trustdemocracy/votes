package eu.trustdemocracy.votes.core.models.response;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VoteResponseDTO {

  private UUID proposalId;
  private UUID userId;
  private Double rank;
}
