/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media.control;

//~--- JDK imports ------------------------------------------------------------

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
