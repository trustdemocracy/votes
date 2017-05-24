package eu.trustdemocracy.votes.core.entities.utils;

import eu.trustdemocracy.votes.core.entities.Proposal;
import eu.trustdemocracy.votes.core.entities.Vote;
import eu.trustdemocracy.votes.core.models.request.VoteRequestDTO;
import eu.trustdemocracy.votes.core.models.response.VoteResponseDTO;

public final class VoteMapper {

  public static Vote createEntity(VoteRequestDTO requestDTO) {
    return new Vote()
        .setProposal(new Proposal().setId(requestDTO.getProposalId()))
        .setUser(UserMapper.createEntity(requestDTO.getUserToken()))
        .setOption(requestDTO.getOption());
  }

  public static VoteResponseDTO createResponse(Vote vote) {
    return new VoteResponseDTO()
        .setProposalId(vote.getProposal().getId())
        .setUserId(vote.getUser().getId())
        .setRank(vote.getUser().getRank())
        .setOption(vote.getOption());
  }
}
