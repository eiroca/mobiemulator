package Emulator;

import java.awt.Dimension;
import java.awt.Graphics;
// ~--- JDK imports ------------------------------------------------------------
import javax.swing.JFrame;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Xrayframe extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = -8001131676415166807L;

  public Xrayframe() {
    setTitle("XRAY");
    setSize(new Dimension(MobiEmulator.mobiEmulatorInstance.canvasPanelWidth, MobiEmulator.mobiEmulatorInstance.canvasPanelHeight));
  }

  @Override
  public void paint(final Graphics g) {
    // synchronized (Canvas.globalSync){
    // g.setColor(new Color(0xFF000000));
    // g.fillRect(0, 0, MobiEmulator.mobiEmulatorInstance.canvasPanelWidth, MobiEmulator.mobiEmulatorInstance.canvasPanelHeight);
    if (MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen != null) {
      g.drawImage(MobiEmulator.mobiEmulatorInstance.canvasPanel.xrayScreen, 0, 0, null);
    }
    // }
  }

  public void update() {
    repaint();
  }

}
