/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Xrayframe extends JFrame {
    public Xrayframe() {
        setTitle("XRAY");
        setSize(new Dimension(MobiEmulator.mobiEmulatorInstance.canvasPanelWidth, MobiEmulator.mobiEmulatorInstance.canvasPanelHeight));
    }

    @Override
    public void paint(Graphics g) {
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
