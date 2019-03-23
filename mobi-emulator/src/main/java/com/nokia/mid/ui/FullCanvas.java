package com.nokia.mid.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
// ~--- non-JDK imports --------------------------------------------------------
import Emulator.MobiEmulator;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class FullCanvas extends Canvas {

  @Override
  protected void paint(final Graphics g) {
    MobiEmulator.mobiEmulatorInstance.canvasPanel.midp_canvas.invokeFramePaint(g);
    // System.out.println("calling canvas paint in Nokia");
  }

  @Override
  public void addCommand(final javax.microedition.lcdui.Command cmd) {
  }

  @Override
  public void setCommandListener(final javax.microedition.lcdui.CommandListener l) {
  }

}
