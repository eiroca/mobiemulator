/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.sound;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.PlayerListener;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class SoundListenerImpl implements SoundListener {
    PlayerListener pl;

    public void soundStateChanged(Sound sound, int event) {
        String Curevent = PlayerListener.DEVICE_UNAVAILABLE;
        if (sound.getPlayer().getState() == Sound.SOUND_PLAYING) {
            Curevent = PlayerListener.STARTED;
        } else if (sound.getPlayer().getState() == Sound.SOUND_STOPPED) {
            Curevent = PlayerListener.STOPPED;
        } else if (sound.getPlayer().getState() == Sound.SOUND_UNINITIALIZED) {
            Curevent = PlayerListener.CLOSED;
        }

        pl.playerUpdate(sound.getPlayer(), Curevent, null);
    }
}
