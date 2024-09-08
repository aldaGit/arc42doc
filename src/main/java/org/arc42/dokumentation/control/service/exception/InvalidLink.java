package org.arc42.dokumentation.control.service.exception;

/**
 * Exception for invalid links. This Class was originally intended only for use of reporting invalid
 * Stack overflow links but can be used for any invalid link.
 */
public class InvalidLink extends Exception {

  public InvalidLink(String message) {
    super(message);
  }
}
