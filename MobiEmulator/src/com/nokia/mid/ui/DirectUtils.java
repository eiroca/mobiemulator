/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.ui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class DirectUtils {
    public static DirectGraphics getDirectGraphics(javax.microedition.lcdui.Graphics g) {
        return new DirectGraphicsImpl(g) {}
        ;
    }

    public static javax.microedition.lcdui.Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        javax.microedition.lcdui.Image img = javax.microedition.lcdui.Image.createImage(imageData, imageOffset,
                                                 imageLength);

        return img;
    }

    public static javax.microedition.lcdui.Image createImage(int width, int height, int ARGBcolor) {
        javax.microedition.lcdui.Image img = javax.microedition.lcdui.Image.createImage(width, height);

        return img;
    }

    public static javax.microedition.lcdui.Font getFont(int face, int style, int height) {
        javax.microedition.lcdui.Font f = new javax.microedition.lcdui.Font(face, style, height);

        return f;
    }
}
