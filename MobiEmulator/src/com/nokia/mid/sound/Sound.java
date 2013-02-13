/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.sound;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.Player;
import javax.microedition.media.PlayerImplementation;
import javax.microedition.media.control.VolumeControlImplementation;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Sound {
    public static final int             FORMAT_TONE         = 1;
    public static final int             FORMAT_WAV          = 5;
    public static final int             SOUND_PLAYING       = 0;
    public static final int             SOUND_STOPPED       = 1;
    public static final int             SOUND_UNINITIALIZED = 3;
    private PlayerImplementation        player;
    private VolumeControlImplementation volumecontrol;

    public Sound(byte[] data, int type) {
        InputStream is = new ByteArrayInputStream(data);
        player = new PlayerImplementation(is, (type == 5) ? "audio/x-wav" : "audio/midi");
    }

    public Sound(int freq, long duration) {}

    public static int getConcurrentSoundCount(int type) {
        return 0;
    }

    public static int[] getSupportedFormats() {
        return null;
    }

    public void setSoundListener(SoundListener listener) {}

    public int getGain() {
        volumecontrol = (VolumeControlImplementation) player.getControl("VolumeControl");

        return volumecontrol.getLevel();
    }

    public void setGain(int gain) {
        volumecontrol = (VolumeControlImplementation) player.getControl("VolumeControl");
        volumecontrol.setLevel(gain);
    }

    public void release() {}

    public void resume() {
        player.start();
    }

    public void stop() {
        player.stop();
    }

    public void play(int loop) {
        player.setLoopCount(loop);
        player.start();
    }

    public int getState() {
        if (player.getState() == Player.PREFETCHED) {
            return SOUND_STOPPED;
        } else if ((player.getState() == Player.UNREALIZED) || (player.getState() == Player.CLOSED)) {
            return SOUND_UNINITIALIZED;
        } else if (player.getState() == Player.STARTED) {
            return SOUND_PLAYING;
        } else {
            return 3;
        }
    }

    public void init(byte[] data, int type) {
        InputStream is = new ByteArrayInputStream(data);
        player = new PlayerImplementation(is, (type == 5) ? "audio/x-wav" : "audio/midi");
    }

    public void init(int freq, long duration) {}

    public Player getPlayer() {
        return player;
    }
}
