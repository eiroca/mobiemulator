/*
 * This program is free software. It comes without any warranty, to the extent permitted by
 * applicable law. You can redistribute it and/or modify it under the terms of the Do What The Fuck
 * You Want To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */

package javax.microedition.midlet;

public class MIDletStateChangeException extends java.lang.Exception {

  /**
   *
   */
  private static final long serialVersionUID = 3076677089294259140L;

  public MIDletStateChangeException() {
    System.out.println("exception in Midlet ");
  }
}
