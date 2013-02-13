/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface AudioOutputControl extends javax.microedition.media.Control {
    public static final int ALL     = 1;
    public static final int DEFAULT = 0;
    public static final int NONE    = 2;
    public static final int PRIVATE = 3;
    public static final int PUBLIC  = 4;

    int[] getAvailableOutputModes();

    AudioOutput getCurrent();

    int setOutputMode(int mode);

    int getOutputMode();
}
