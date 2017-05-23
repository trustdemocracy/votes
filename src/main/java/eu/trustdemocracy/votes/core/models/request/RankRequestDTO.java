package eu.trustdemocracy.votes.core.models.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankRequestDTO {

  private Map<UUID, Double> rankings = new HashMap<>();
}
