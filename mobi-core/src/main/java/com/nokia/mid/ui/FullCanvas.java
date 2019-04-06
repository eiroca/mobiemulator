package com.nokia.mid.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import mobiemulator.Environment;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class FullCanvas extends Canvas {

  @Override
  protected void paint(final Graphics g) {
    Environment.instance.repaint(g);
    // System.out.println("calling canvas paint in Nokia");
  }

  @Override
  public void addCommand(final javax.microedition.lcdui.Command cmd) {
  }

  @Override
  public void setCommandListener(final javax.microedition.lcdui.CommandListener l) {
  }

}
