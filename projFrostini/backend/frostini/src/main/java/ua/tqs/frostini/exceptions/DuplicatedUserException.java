package ua.tqs.frostini.exceptions;

public class DuplicatedUserException extends Exception {
  
  public DuplicatedUserException(String message){
    super(message);
  }
}
