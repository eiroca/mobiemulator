/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.sprintpcs.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Vibrator {
    public static void vibrate(int duration) {
        javax.microedition.lcdui.Display d = new javax.microedition.lcdui.Display();
        d.vibrate(duration);
    }
}
