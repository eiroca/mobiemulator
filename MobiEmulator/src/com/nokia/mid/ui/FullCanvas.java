/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.nokia.mid.ui;

//~--- non-JDK imports --------------------------------------------------------

import Emulator.MobiEmulator;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class FullCanvas extends Canvas {
    @Override
    protected void paint(Graphics g) {
        MobiEmulator.mobiEmulatorInstance.canvasPanel.midp_canvas.invokeFramePaint(g);
        // System.out.println("calling canvas paint in Nokia");
    }

    @Override
    public void addCommand(javax.microedition.lcdui.Command cmd) {}

    @Override
    public void setCommandListener(javax.microedition.lcdui.CommandListener l) {}
}
