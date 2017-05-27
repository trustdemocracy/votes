package eu.trustdemocracy.votes.core.models.request;


import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProposalRequestDTO {

  private UUID id;
  private String title;
  private Long dueDate;
}
