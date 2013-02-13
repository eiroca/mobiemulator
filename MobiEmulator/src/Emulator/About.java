package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class About extends JDialog {
    private static final float        MB           = 1024 * 1024;
    private final JPanel contentPanel = new JPanel();
    private Graph        grph         = null;
    private JPanel       aboutTopPanel;
    private JButton      okButton;
    private JProgressBar progressBar;
    private JButton      recycle;

    /**
     * Create the dialog.
     */
    public About() {
        setAlwaysOnTop(true);
        // setUndecorated(true);
        setTitle("About");
        getContentPane().setLayout(new BorderLayout(0, 0));
        setSize(235, 350);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel buttonPane = new JPanel();
            contentPanel.add(buttonPane, BorderLayout.SOUTH);
            {
                recycle = new JButton();
                try {
                    InputStream   is   = "".getClass().getResourceAsStream("/icons/RecycleBin_Empty.png");
                    BufferedImage icon = ImageIO.read(is);
                    is.close();
                    is = null;
                    recycle.setIcon(new ImageIcon(icon));
                    recycle.setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()));
                    // recycle.setBorderPainted(false);
                    icon = null;
                } catch (IOException ex) {
                    System.out.println("Failed to load icon recycle bin icon");
                }
                recycle.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.gc();
                        update();
                    }
                });
                buttonPane.add(recycle, BorderLayout.WEST);
            }
            {
                progressBar = new JProgressBar();
                progressBar.setPreferredSize(new Dimension(90, recycle.getPreferredSize().height));
                progressBar.setStringPainted(true);
                progressBar.setValue(50);
                buttonPane.add(progressBar, BorderLayout.CENTER);
            }
            {
                okButton = new JButton("OK");
                okButton.setPreferredSize(okButton.getPreferredSize());
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });
                buttonPane.add(okButton, BorderLayout.EAST);
                getRootPane().setDefaultButton(okButton);
            }
        }
        JLabel lblNewLabel = new JLabel("<html>J2ME Emulator V0.4 Beta <br> Author Ashok Kumar.</html>");
        // lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        JLabel button      = new JLabel();
        button.setText("<HTML><FONT color=\"#000099\"><U>Check Upgrades Here</U></FONT></HTML>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        // button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(Color.WHITE);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    URI uri = new URI("http://ashokcodecrafts.blogspot.com/search/label/MobiEmulator");
                    open(uri);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        JLabel memorygraphLabel = new JLabel("MobiEmulator Memory Graph");
        aboutTopPanel = new JPanel();
        aboutTopPanel.setPreferredSize(new Dimension(getWidth(), 90));
        aboutTopPanel.add(lblNewLabel, BorderLayout.NORTH);
        aboutTopPanel.add(button, BorderLayout.CENTER);
        aboutTopPanel.add(memorygraphLabel, BorderLayout.SOUTH);
        getContentPane().add(aboutTopPanel, BorderLayout.NORTH);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setPreferredSize(new Dimension(getWidth() - 20, 30));
        grph = new Graph();
        grph.setPreferredSize(grph.getPreferredSize());
        getContentPane().add(grph, BorderLayout.CENTER);
        getContentPane().add(contentPanel, BorderLayout.SOUTH);
        setLocation(MobiEmulator.mobiEmulatorInstance.getX() + MobiEmulator.mobiEmulatorInstance.getWidth() / 2 - this.getWidth() / 2,
                    MobiEmulator.mobiEmulatorInstance.getY() + MobiEmulator.mobiEmulatorInstance.getHeight() / 2 - this.getHeight() / 2);
        setVisible(false);
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {

            }
        }
    }
    public void update() {
        Runtime rt      = Runtime.getRuntime();
        float   freeMem = rt.freeMemory() / (MB);
        freeMem = round(freeMem, 2);
        float totMem = rt.totalMemory() / (MB);
        totMem = round(totMem, 2);
        float usedMem = totMem - freeMem;
        progressBar.setMinimum(0);
        progressBar.setMaximum((int) totMem);
        progressBar.setValue((int) usedMem);
        usedMem = round(usedMem, 2);
        progressBar.setString(usedMem + "M/" + totMem + "M");
    }
    public static float round(float val, int c) {
        float p = (float) Math.pow(10, c);
        return Math.round(val * p) / p;
    }
    public void setAboutVisible(boolean visible) {
        setVisible(visible);
        if (grph != null) {
            grph.isVisible = visible;
        }
    }
    public boolean isAboutVisible() {
        return isVisible();
    }
}
