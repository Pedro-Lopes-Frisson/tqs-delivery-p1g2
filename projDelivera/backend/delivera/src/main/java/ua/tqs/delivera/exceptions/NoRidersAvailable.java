package ua.tqs.delivera.exceptions;

import java.lang.Exception;

public class NoRidersAvailable extends Exception {
  public NoRidersAvailable( String s ) {
    super( s );
  }
}
