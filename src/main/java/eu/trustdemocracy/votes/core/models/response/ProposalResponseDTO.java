package eu.trustdemocracy.votes.core.models.response;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProposalResponseDTO {

  private UUID id;
  private Long dueDate;
}
