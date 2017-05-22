package eu.trustdemocracy.votes.gateways;

import java.util.ArrayList;
import java.util.List;

public class FakeRankerGateway implements RankerGateway {
  public List<Long> dueDates = new ArrayList<>();

  @Override
  public void addDueDate(Long dueDate) {
    dueDates.add(dueDate);
  }
}
