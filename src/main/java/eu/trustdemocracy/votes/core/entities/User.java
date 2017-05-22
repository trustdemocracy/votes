package eu.trustdemocracy.votes.core.entities;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

  private UUID id;
  private String username;
  private Double rank;
}
