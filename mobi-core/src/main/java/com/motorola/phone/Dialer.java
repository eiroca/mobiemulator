package com.motorola.phone;

import java.io.IOException;

public class Dialer {

  public void endCall() throws IOException {
  }

  public static Dialer getDefaultDialer() {
    return null;
  }

  public void sendExtNo(final String extNumber) throws IOException {
  }

  public void startCall(final String teleNumber) throws IOException {
  }

  public void startCall(final String teleNumber, final String extNo) throws IOException {
  }

  public void setDialerListener(final DialerListener listener) {
  }

  public void notifyDialerListener(final byte event) {
  }

}
