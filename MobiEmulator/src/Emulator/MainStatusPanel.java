package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class MainStatusPanel extends JPanel {
    private static int statusPanelStringOffsets = 2;
    public static  String statusCenterText = "CENTER";
    private static  String statusLeftText = "LSK";
    private static  String statusRightText = "RSK";

    public MainStatusPanel(MobiEmulator instance, int Width, int Height) {
        setBackground(Color.LIGHT_GRAY);
        setBorder(new LineBorder(Color.black, 1, false));
        setPreferredSize(new Dimension(Width, Height));
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        int textY = getHeight() - getFontMetrics(getFont()).getHeight() / 2;
        g.drawString(statusLeftText, statusPanelStringOffsets, textY);
        g.drawString(statusRightText,
                getWidth() - getFontMetrics(getFont()).stringWidth(statusRightText) - statusPanelStringOffsets,
                textY);
        g.drawString(statusCenterText, getWidth() / 2 - (getFontMetrics(getFont()).stringWidth(statusCenterText) / 2),
                textY);
    }

    public void setLeftText(String Lefttext) {
        statusLeftText = Lefttext;
    }

    public void setRightText(String Righttext) {
        statusRightText = Righttext;
    }

    public void setCenterText(String Centertext) {
        statusCenterText = Centertext;
    }

    public String getLeftText() {
        return statusLeftText;
    }

    public String getRightText() {
        return statusRightText;
    }

    public String getCenterText() {
        return statusCenterText;
    }
}
