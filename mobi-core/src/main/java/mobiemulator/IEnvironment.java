package mobiemulator;

import javax.microedition.lcdui.Graphics;

public interface IEnvironment {

  public void repaint(Graphics g); // mobiEmulatorInstance.canvasPanel.midp_canvas.invokeFramePaint(g)

  public String getRMSDir(); //Main.getRmsDir()

}
