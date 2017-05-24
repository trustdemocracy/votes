package eu.trustdemocracy.votes.gateways;

import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.entities.VoteOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.val;

public class FakeVotesRepository implements VotesRepository {

  public Map<String, VoteOption> votes = new HashMap<>();
  public Map<UUID, Double> rankings = new HashMap<>();

  public FakeVotesRepository(Map<UUID, Double> rankings) {
    this.rankings = rankings;
  }

  @Override
  public VoteOption upsert(Vote vote) {
    String key = getKey(vote);
    return votes.put(key, vote.getOption());
  }

  @Override
  public VoteOption remove(Vote vote) {
    String key = getKey(vote);
    return votes.remove(key);
  }

  @Override
  public Map<VoteOption, Double> findWithRank(UUID id) {
    ConcurrentMap<VoteOption, Double> accumulator = new ConcurrentHashMap<>();

    return votes.entrySet().stream()
        .reduce(accumulator,
            (map, entry) -> {
              if (!entry.getKey().contains(id + "|")) {
                return map;
              }

              val userId = getUserId(entry.getKey());
              val rank = rankings.get(userId);
              val option = entry.getValue();

              val count = map.get(option);
              map.put(option, count == null ? rank : count + rank);
              return map;
            }, (map1, map2) -> Stream.of(map1, map2)
                .parallel()
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                    Collectors.toConcurrentMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Double::sum
                    )));
  }

  private static String getKey(Vote vote) {
    return vote.getProposal().getId() + "|" + vote.getUser().getId();
  }

  private static UUID getUserId(String key) {
    return UUID.fromString(key.split("\\|")[1]);
  }
}
