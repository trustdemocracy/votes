package eu.trustdemocracy.votes.core.interactors.exceptions;

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }

}
