package com.samsung.util;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AudioClip {

  public static final int TYPE_MIDI = 3;
  public static final int TYPE_MMF = 1;
  public static final int TYPE_MP3 = 2;

  public AudioClip(final int type, final java.lang.String filename) throws java.io.IOException {
  }

  public AudioClip(final int type, final byte[] audioData, final int audioOffset, final int audioLength) {
  }

  public void play(final int loop, final int volume) {
  }

  public void stop() {
  }

  public void pause() {
  }

  public void resume() {
  }

  public static boolean isSupported() {
    return false;
  }

}
