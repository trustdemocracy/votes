package eu.trustdemocracy.votes.core.models.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankResponseDTO {

  private boolean successful;
}
