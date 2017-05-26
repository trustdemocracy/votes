package eu.trustdemocracy.votes.core.interactors;

public interface Interactor <RequestDTO, ResponseDTO> {
  ResponseDTO execute(RequestDTO requestDTO);
}
