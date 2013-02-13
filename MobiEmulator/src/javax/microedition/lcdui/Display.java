/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */



package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MidletUtils;

//~--- JDK imports ------------------------------------------------------------

public class Display {

    /** Only one 'display' object, so use a static variable */
    public static Display display;
    private int           numAlphalevels = 2;
    Thread                screenThread;
    public static Display getDisplay(MIDlet m) {
        if (display == null) {
            display = new Display();
        }

        return display;
    }
    public Displayable getCurrent() {
        return MidletUtils.getInstance().getMidletListener().getCurrentDisplay();
    }
    public void setCurrent(Alert a, Displayable displayable) {
        setCurrent(displayable);
    }

    /**
     * Call the CanvasAWT setCurrent method, as well as displayable setCurrent
     * methods, which filter down to the Canvas showNotify and hideNotify too.
     */
    public void setCurrent(Displayable nextDisplayable) {
        Displayable current = getCurrent();
        if (nextDisplayable == null) {
            current = null;
            if ((screenThread != null) && screenThread.isAlive()) {
                try {
                    screenThread.join();
                } catch (InterruptedException ex) {}
            }
            screenThread = null;
        }
        if ((current != null) && (current instanceof Canvas)) {
            ((Canvas) (current)).hideNotify();
        }
        if (nextDisplayable != null) {
            if (nextDisplayable instanceof Canvas) {
                if ((screenThread != null) && screenThread.isAlive()) {
                    try {
                        screenThread.join();
                    } catch (InterruptedException ex) {}
                }
                MidletUtils.getInstance().getMidletListener().setCurrentDisplay(nextDisplayable,"canvas");

              MidletUtils.getInstance().getMidletListener().canvasShowNotify();
               MidletUtils.getInstance().getMidletListener().canvasRepaint();   // invokeFramePaint(MobiEmulator.mobiEmulatorInstance.canvasPanel.midp_screen_image.createGraphics());
            } else if (nextDisplayable instanceof Screen) {    // for forms or list or alert or textbox need to run threaded
                System.out.println(">>>>> comming here");
                MidletUtils.getInstance().getMidletListener().setCurrentDisplay(nextDisplayable,"screen");
                // MobiEmulator.mobiEmulatorInstance.canvasPanel.midp_screen.showNotify();
                // .mobiEmulatorInstance.canvasPanel.midp_screen.repaint();
                screenThread = new Thread(MidletUtils.getInstance().getMidletListener().getCanvas());
                screenThread.start();
                // Thread.
                System.out.println("comming in screen");
            }
        }
    }
    public boolean isColor() {
        return true;
    }
    public boolean vibrate(int Duration) {
        vibrateFrame(Duration);

        return true;
    }
    public void vibrateFrame(int Duration) {
        int vibrate = Duration / 100;
        int posx    = MidletUtils.getInstance().getMidletListener().getPos()[0];
        int posy    = MidletUtils.getInstance().getMidletListener().getPos()[1];
        for (int i = 0;i < vibrate;i++) {
            if (i % 2 == 0) {
                MidletUtils.getInstance().getMidletListener().setPos(posx - 5, posy - 5);
            } else {
                MidletUtils.getInstance().getMidletListener().setPos(posx + 5, posy + 5);
            }
        }
        MidletUtils.getInstance().getMidletListener().setPos(posx, posy);
    }
    public boolean flashBacklight(int duration) {
        return true;
    }

    /**
     * Another interesting method that probably doesn't need to be implemented
     * for most games (see Canvas.serviceRepaints). In order to synchronise with
     * the repaint cycle we could maintain a list or queue of Runnables, and
     * then CanvasAWT could check it at the bottom of it's paint method, running
     * any objects in the queue.
     */
    public void callSerially(Runnable r) {
        MidletUtils.getInstance().getMidletListener().setRunnable(r);

    }
    public int numAlphaLevels() {
        return numAlphalevels;
    }
    public int numColors()
    {
        return 64*(1024);
    }
}
