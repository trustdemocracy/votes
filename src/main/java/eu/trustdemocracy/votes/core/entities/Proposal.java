package eu.trustdemocracy.votes.core.entities;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Proposal {

  private UUID id;
  private Long dueDate;
  private boolean isActive;
}
