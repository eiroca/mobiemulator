package javax.microedition.media.control;

import javax.microedition.media.Control;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public abstract interface VolumeControl extends Control {

  public abstract void setMute(boolean mute);

  public abstract int setLevel(int level);

  public abstract int getLevel();

  public abstract boolean isMuted();

}
