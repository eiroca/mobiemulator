package Emulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
// ~--- JDK imports ------------------------------------------------------------
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

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class LogFrame extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = -3392832734913203014L;
  private final String logFilePath = "./logs/";
  private final int logFrameHeight = 600;
  private final int logFrameWidth = 600;
  private String previousSearchText = "";
  private int previouspos = 0;
  //  An instance of the private subclass of the default highlight painter
  Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.green);

  /** Log Frame */
  private LogPrintStream logStream;
  public JTextArea logTextArea;
  private JScrollPane logscrollPane;
  private JButton clearButton;
  private Document doc;
  private final MobiEmulator mobiEmulatorInstance;
  private JButton findButton;
  private JPanel findPanel;
  private String logText;
  private JTextField searchBar;
  private JLabel searchLabel;

  public LogFrame(final MobiEmulator mobiEmulatorInstance) {
    this.mobiEmulatorInstance = mobiEmulatorInstance;
    createLogFrame();
  }

  void setLogFile(final String path) {
    try {
      logStream.setLogFile(logFilePath + path);
    }
    catch (final IOException ex) {
      ex.printStackTrace();
    }
  }

  private void createLogFrame() {

    /* Log Frame */
    setTitle("Log");
    setSize(logFrameWidth, logFrameHeight);
    setLocation(new Point(getX() + getWidth(), getY()));
    setLayout(new BorderLayout());
    logscrollPane = new JScrollPane();
    add(logscrollPane, BorderLayout.CENTER);
    logTextArea = new JTextArea();
    logTextArea.setBackground(Color.WHITE);
    logTextArea.setForeground(Color.BLACK);
    logTextArea.setFont(Font.getFont("Arial"));
    logscrollPane.setViewportView(logTextArea);
    findPanel = new JPanel();
    searchLabel = new JLabel("Search");
    findPanel.add(searchLabel);
    searchBar = new JTextField();
    searchBar.setColumns(35);
    searchBar.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(final KeyEvent e) {
        if (!searchBar.getText().equals(previousSearchText)) {
          previouspos = 0;
        }
      }

      @Override
      public void keyPressed(final KeyEvent e) {
        // findButton.requestFocus(true);
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          findMatch(searchBar.getText());
        }
      }

      @Override
      public void keyReleased(final KeyEvent e) {
        // findButton.requestFocus(true);
      }
    });
    findPanel.add(searchBar);
    findButton = new JButton();
    findButton.setText("Find");
    findButton.addActionListener(e -> findMatch(searchBar.getText()));
    findButton.setFocusable(true);
    findPanel.add(findButton);
    clearButton = new JButton();
    clearButton.setText("ClearLog");
    clearButton.addActionListener(e -> {
      logTextArea.setText("");
      logStream.logLength = 0;
    });
    findPanel.add(clearButton);
    add(findPanel, BorderLayout.NORTH);
    try {
      final String dir = logFilePath.substring(logFilePath.indexOf("/") + 1, logFilePath.lastIndexOf("/"));
      if (!new File(dir).exists()) {
        new File(dir).mkdir();
      }
      //          if (Main.uniqueID != 0) {
      //              logFilePath = "./" + dir + "/EMUL_" + Main.uniqueID + ".log";
      //          }
      logStream = new LogPrintStream(null);
      logStream.setMobiEmulatorInstance(mobiEmulatorInstance);
      // logTextArea.read(new FileReader(logFilePath),null);
    }
    catch (final IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      System.out.println("Error : while creating Log File");
    }
  }

  private void findMatch(final String text) {
    // String wholeString=logTextArea.getText();
    previousSearchText = text;
    doc = logTextArea.getDocument();
    try {
      logText = doc.getText(0, doc.getLength());
    }
    catch (final BadLocationException ex) {
    }
    previouspos = logText.indexOf(text, previouspos);
    if (previouspos != -1) {
      logTextArea.setCaretPosition(previouspos);
      previouspos += text.length();
    }
    highlight(logTextArea, text);
  }

  // Creates highlights around all occurrences of pattern in textComp
  public void highlight(final JTextComponent textComp, final String pattern) {
    // First remove all old highlights
    removeHighlights(textComp);
    try {
      final Highlighter hilite = textComp.getHighlighter();
      int pos = 0;
      // Search for pattern
      while ((pos = logText.indexOf(pattern, pos)) > 0) {
        // Create highlighter using private painter and apply around pattern
        hilite.addHighlight(pos, pos + pattern.length(), myHighlightPainter);
        pos += pattern.length();
      }
    }
    catch (final BadLocationException e) {
    }
  }

  //  Removes only our private highlights
  public void removeHighlights(final JTextComponent textComp) {
    final Highlighter hilite = textComp.getHighlighter();
    Highlighter.Highlight[] hilites = hilite.getHighlights();
    for (final Highlighter.Highlight hilite1 : hilites) {
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

    public MyHighlightPainter(final Color color) {
      super(color);
    }
  }

}
