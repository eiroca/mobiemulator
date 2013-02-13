/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class LogFrame extends JFrame {
    private String logFilePath = "./logs/";
    private int logFrameHeight = 600;
    private int logFrameWidth = 600;
    private String               previousSearchText = "";
    private int                  previouspos        = 0;
//  An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.green);

    /** Log Frame */
    private LogPrintStream logStream;
    public JTextArea logTextArea;
    private JScrollPane logscrollPane;
    private JButton              clearButton;
    private Document             doc;
    private MobiEmulator mobiEmulatorInstance;
    private JButton              findButton;
    private JPanel               findPanel;
    private String               logText;
    private JTextField           searchBar;
    private JLabel               searchLabel;
    public LogFrame(MobiEmulator mobiEmulatorInstance) {
        this.mobiEmulatorInstance = mobiEmulatorInstance;
        createLogFrame();
    }

    void setLogFile(String path) {
        try {
            logStream.setLogFile(logFilePath + path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void createLogFrame() {

        /* Log Frame */
        setTitle("Log");
        setSize(logFrameWidth, logFrameHeight);
        setLocation(new Point(this.getX() + this.getWidth(), this.getY()));
        setLayout(new BorderLayout());
        logscrollPane = new JScrollPane();
        add(logscrollPane, BorderLayout.CENTER);
        logTextArea = new JTextArea();
        logTextArea.setBackground(Color.WHITE);
        logTextArea.setForeground(Color.BLACK);
        logTextArea.setFont(Font.getFont("Arial"));
        logscrollPane.setViewportView(logTextArea);
        findPanel   = new JPanel();
        searchLabel = new JLabel("Search");
        findPanel.add(searchLabel);
        searchBar = new JTextField();
        searchBar.setColumns(35);
        searchBar.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if (!searchBar.getText().equals(previousSearchText)) {
                    previouspos = 0;
                }
            }
            public void keyPressed(KeyEvent e) {
                // findButton.requestFocus(true);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    findMatch(searchBar.getText());
                }
            }
            public void keyReleased(KeyEvent e) {
                // findButton.requestFocus(true);
            }
        });
        findPanel.add(searchBar);
        findButton = new JButton();
        findButton.setText("Find");
        findButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findMatch(searchBar.getText());
            }
        });
        findButton.setFocusable(true);
        findPanel.add(findButton);
        clearButton = new JButton();
        clearButton.setText("ClearLog");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logTextArea.setText("");
                logStream.logLength = 0;
            }
        });
        findPanel.add(clearButton);
        add(findPanel, BorderLayout.NORTH);
        try {
            String dir = logFilePath.substring(logFilePath.indexOf("/") + 1, logFilePath.lastIndexOf("/"));
            if (!new File(dir).exists()) {
                new File(dir).mkdir();
            }
//          if (Main.uniqueID != 0) {
//              logFilePath = "./" + dir + "/EMUL_" + Main.uniqueID + ".log";
//          }
            logStream = new LogPrintStream(null);
            logStream.setMobiEmulatorInstance(mobiEmulatorInstance);
            // logTextArea.read(new FileReader(logFilePath),null);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.out.println("Error : while creating Log File");
        }
    }
    private void findMatch(String text) {
        // String wholeString=logTextArea.getText();
        previousSearchText = text;
        doc                = logTextArea.getDocument();
        try {
            logText = doc.getText(0, doc.getLength());
        } catch (BadLocationException ex) {}
        previouspos = logText.indexOf(text, previouspos);
        if (previouspos != -1) {
            logTextArea.setCaretPosition(previouspos);
            previouspos += text.length();
        }
        highlight(logTextArea, text);
    }
    // Creates highlights around all occurrences of pattern in textComp
    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights
        removeHighlights(textComp);
        try {
            Highlighter hilite = textComp.getHighlighter();
            int         pos    = 0;
            // Search for pattern
            while ((pos = logText.indexOf(pattern, pos)) > 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {}
    }
//  Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter             hilite  = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (Highlighter.Highlight hilite1 : hilites) {
            if (hilite1.getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilite1);
            }
        }
        hilites = null;
    }
    public void deInitiazlie() {
        logTextArea = null;
    }
//  A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}
