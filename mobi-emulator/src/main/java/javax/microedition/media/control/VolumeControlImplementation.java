package javax.microedition.media.control;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.PlayerImplementation;

public class VolumeControlImplementation implements VolumeControl {

  int currentVolumeLevel = 0;
  boolean isMuted = false;
  PlayerImplementation player;

  public VolumeControlImplementation(final PlayerImplementation Player) {
    player = Player;
  }

  @Override
  public int getLevel() {
    return currentVolumeLevel;
  }

  @Override
  public boolean isMuted() {
    return isMuted;
  }

  @Override
  public int setLevel(final int level) {
    if (level <= 0) {
      currentVolumeLevel = 0;
    }
    else if (level >= 100) {
      currentVolumeLevel = 100;
    }
    else {
      currentVolumeLevel = level;
    }
    System.out.println("in setlevel volume" + currentVolumeLevel);
    player.setVolume(currentVolumeLevel);

    return 1;
  }

  @Override
  public void setMute(final boolean mute) {
    player.setMute(isMuted);
    isMuted = mute;
  }
}
