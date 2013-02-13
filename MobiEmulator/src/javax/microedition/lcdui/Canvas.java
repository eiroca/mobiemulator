/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */



package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------

import javax.microedition.midlet.MidletListener;
import javax.microedition.midlet.MidletUtils;

public abstract class Canvas extends Displayable {
    private Graphics g                        = null;
    private boolean  isPointerEventsSupported = true;
    private CanvasProperties canvasListener;
    protected MidletListener listener;

    protected Canvas() {
        super();


    }



    @Override
    public int getWidth() {
        listener= MidletUtils.getInstance().getMidletListener();
        return listener.getCanvasWidth();
    }
    @Override
    public int getHeight() {
        listener= MidletUtils.getInstance().getMidletListener();
        return listener.getCanvasHeight();
    }
    public boolean isDoubleBuffered() {
        return true;
    }
    @Override
    protected void keyPressed(int keyCode) {}
    @Override
    protected void keyRepeated(int keyCode) {}
    @Override
    protected void keyReleased(int keyCode) {}
    @Override
    protected void pointerPressed(int x, int y) {}
    @Override
    protected void pointerDragged(int x, int y) {}
    @Override
    protected void pointerReleased(int x, int y) {}
    public void invokeHideNotify() {
        hideNotify();
    }
    public void invokeShowNotify() {
        showNotify();
    }
    public void setFullScreenMode(boolean mode) {
        if (mode) {
            listener.setCanvasFullScreen(true);
        } else {
            listener.setCanvasFullScreen(false);

        }
    }
    public void repaint(int x, int y, int w, int h) {
        // if (isShown ())
        {
            // Force a repaint
            // MobiEmulator.mobiEmulatorInstance.canvasPanel.repaint (x, y, w, h);
            repaint();
        }
    }
    public void repaint() {
        // if (isShown ())
        {
            // Force a repaint
//          System.out.println("in repaint "+MobiEmulator.mobiEmulatorInstance.performanceInvestigate+" "+MobiEmulator.mobiEmulatorInstance.paintRecordCount+" "+MobiEmulator.mobiEmulatorInstance.paintStackFrame.getRecordFrameCount());
            try{
            if (listener.isPerformanceEnabled()
                    && (listener.getPaintRecordCount() < listener.getPaintStackRecordCount())) {
//              System.out.println("record count"+MobiEmulator.mobiEmulatorInstance.paintRecordCount);
                // while(MobiEmulator.mobiEmulatorInstance.paintRecordCount<MobiEmulator.mobiEmulatorInstance.paintStackFrame.getRecordFrameCount())
                {
                    listener.paintStackStartFrame();
                    invokeFramePaint(new Graphics(listener.getCanvasScreen().createGraphics(),listener.getCanvasScreen()));
                    listener.paintStackEndFrame();
                    listener.incrPaintRecordCount();
                }
                if (listener.getPaintRecordCount() >= listener.getPaintStackRecordCount()) {
                    listener.paintStackStopRecording();
                }
            } else {
                invokeFramePaint(new Graphics(listener.getCanvasScreen().createGraphics(),listener.getCanvasScreen()));
            }
            }catch(Exception e){}
        }
    }

    /**
     * An interesting method that probably doesn't need to be implemented for
     * most games
     */
    public void serviceRepaints() {
        if (isShown()) {
            // repaint();
            // Do the service repaints thing. This is somewhat awkward, as there
            // isn't an equivalent
            // to serviceRepaints in AWT. You could do something like loop until
            // the AWT event
            // queue is empty, using
            // java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue()
        }
    }

    /**
     * Abstract method that needs to be implemented by concrete midlet supplied
     * subclasses.
     */
    protected abstract void paint(javax.microedition.lcdui.Graphics g);

    /**
     * This is the public method that CanvasAWT uses to invoke the midlets
     * Canvas paint method.
     */
    public void invokeFramePaint(javax.microedition.lcdui.Graphics g) {
        // Setup the graphics object as per the spec.
        synchronized (globalSync) {
            // try{Thread.yield();}catch(Exception e){}
            synchronized (this.paintsync) {
//                if(MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen!=null){
//                MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen.getGraphics().setColor(Color.BLUE);
//                MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen.getGraphics().fillRect(0, 0, MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen.getWidth(),
//                        MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen.getHeight());
 //                       
 //               }
                paint(g);
            }
           listener.canvasRepaint();
            // try{Thread.yield();}catch(Exception e){}
        }
    }
    public void showNotify() {}
    public void hideNotify() {}
    @Override
    protected void sizeChanged(int w, int h) {}
//  @Override
//  public void setCurrent(boolean current) {
//          if (current) {
//                  showNotify();
//          } else {
//                  hideNotify();
//          }
//  }
    public boolean hasPointerMotionEvents() {
        return isPointerEventsSupported;
    }
    public boolean hasPointerEvents() {
        return isPointerEventsSupported;
    }
}

interface CanvasProperties {

    public void paintStackStartFrame();
    public void paintStackEndFrame();
}