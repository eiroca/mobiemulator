package Emulator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

// ~--- JDK imports ------------------------------------------------------------

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class MainStatusPanel extends JPanel {

  /**
   *
   */
  private static final long serialVersionUID = 5936509708062722955L;
  private static int statusPanelStringOffsets = 2;
  public static String statusCenterText = "CENTER";
  private static String statusLeftText = "LSK";
  private static String statusRightText = "RSK";

  public MainStatusPanel(final MobiEmulator instance, final int Width, final int Height) {
    setBackground(Color.LIGHT_GRAY);
    setBorder(new LineBorder(Color.black, 1, false));
    setPreferredSize(new Dimension(Width, Height));
    setVisible(true);
  }

  @Override
  public void paintComponent(final Graphics g) {
    g.setColor(Color.BLACK);
    final int textY = getHeight() - (getFontMetrics(getFont()).getHeight() / 2);
    g.drawString(MainStatusPanel.statusLeftText, MainStatusPanel.statusPanelStringOffsets, textY);
    g.drawString(MainStatusPanel.statusRightText,
        getWidth() - getFontMetrics(getFont()).stringWidth(MainStatusPanel.statusRightText) - MainStatusPanel.statusPanelStringOffsets,
        textY);
    g.drawString(MainStatusPanel.statusCenterText, (getWidth() / 2) - (getFontMetrics(getFont()).stringWidth(MainStatusPanel.statusCenterText) / 2),
        textY);
  }

  public void setLeftText(final String Lefttext) {
    MainStatusPanel.statusLeftText = Lefttext;
  }

  public void setRightText(final String Righttext) {
    MainStatusPanel.statusRightText = Righttext;
  }

  public void setCenterText(final String Centertext) {
    MainStatusPanel.statusCenterText = Centertext;
  }

  public String getLeftText() {
    return MainStatusPanel.statusLeftText;
  }

  public String getRightText() {
    return MainStatusPanel.statusRightText;
  }

  public String getCenterText() {
    return MainStatusPanel.statusCenterText;
  }
}
