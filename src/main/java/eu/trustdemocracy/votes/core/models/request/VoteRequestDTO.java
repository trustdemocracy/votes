package eu.trustdemocracy.votes.core.models.request;

import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VoteRequestDTO {

  private UUID proposalId;
  private String userToken;
  private VoteOption option;
}
