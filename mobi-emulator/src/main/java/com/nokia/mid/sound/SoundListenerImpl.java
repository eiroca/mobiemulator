package com.nokia.mid.sound;

import javax.microedition.media.PlayerListener;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class SoundListenerImpl implements SoundListener {

  PlayerListener pl;

  @Override
  public void soundStateChanged(final Sound sound, final int event) {
    String Curevent = PlayerListener.DEVICE_UNAVAILABLE;
    if (sound.getPlayer().getState() == Sound.SOUND_PLAYING) {
      Curevent = PlayerListener.STARTED;
    }
    else if (sound.getPlayer().getState() == Sound.SOUND_STOPPED) {
      Curevent = PlayerListener.STOPPED;
    }
    else if (sound.getPlayer().getState() == Sound.SOUND_UNINITIALIZED) {
      Curevent = PlayerListener.CLOSED;
    }

    pl.playerUpdate(sound.getPlayer(), Curevent, null);
  }

}
