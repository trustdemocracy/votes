package eu.trustdemocracy.votes.core.models.request;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetVoteRequestDTO {

  private String userToken;
  private UUID proposalId;
}
