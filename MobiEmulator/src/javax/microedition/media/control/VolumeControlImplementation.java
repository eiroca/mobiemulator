package javax.microedition.media.control;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.media.PlayerImplementation;

public class VolumeControlImplementation implements VolumeControl {
    int                  currentVolumeLevel = 0;
    boolean              isMuted            = false;
    PlayerImplementation player;
    public VolumeControlImplementation(PlayerImplementation Player) {
        this.player = Player;
    }

    public int getLevel() {
        return currentVolumeLevel;
    }
    public boolean isMuted() {
        return isMuted;
    }
    public int setLevel(int level) {
        if (level <= 0) {
            currentVolumeLevel = 0;
        } else if (level >= 100) {
            currentVolumeLevel = 100;
        } else {
            currentVolumeLevel = level;
        }
        System.out.println("in setlevel volume" + currentVolumeLevel);
        this.player.setVolume(currentVolumeLevel);

        return 1;
    }
    public void setMute(boolean mute) {
        this.player.setMute(isMuted);
        isMuted = mute;
    }
}
