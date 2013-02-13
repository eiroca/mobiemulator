/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.samsung.util;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AudioClip {
    public static final int TYPE_MIDI = 3;
    public static final int TYPE_MMF  = 1;
    public static final int TYPE_MP3  = 2;

    public AudioClip(int type, java.lang.String filename) throws java.io.IOException {}

    public AudioClip(int type, byte[] audioData, int audioOffset, int audioLength) {}

    public void play(int loop, int volume) {}

    public void stop() {}

    public void pause() {}

    public void resume() {}

    public static boolean isSupported() {
        return false;
    }
}
