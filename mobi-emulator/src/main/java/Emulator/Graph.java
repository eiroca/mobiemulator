package Emulator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
/**
 *
 * @author Ashok Kumar Gujarathi
 */
import javax.swing.JPanel;

class Graph extends JPanel {

  /**
   *
   */
  private static final long serialVersionUID = 5438475564280794641L;
  private static final String DELIM = " ";
  private static final int HISTORY_LENGTH = 200;
  private static final int UPDATE_PERIOD = 1000;
  private static final Dimension PREFERRED_SIZE = new Dimension(200, 200);
  private static final Dimension MINIMUM_SIZE = new Dimension(40, 40);
  public boolean isVisible = false;
  private long heapSize;
  private long[] history;
  private long hwm;
  private int index;
  // MemoryMonitor monitor;
  Dimension size;
  private final Timer timer;
  private long usedMem;

  public Graph() {
    // this.monitor = paramMemoryMonitor;
    history = new long[200];
    index = 0;
    hwm = 0L;
    setBackground(Color.black);
    timer = new Timer();
    timer.schedule(new GraphTimerTask(), 0L, Graph.UPDATE_PERIOD);
  }

  public void setHeapSize(final long paramLong) {
    heapSize = paramLong;
  }

  @Override
  public Dimension getPreferredSize() {
    return Graph.PREFERRED_SIZE;
  }

  @Override
  public Dimension getMinimumSize() {
    return Graph.MINIMUM_SIZE;
  }

  @Override
  public void paint(final Graphics paramGraphics) {
    super.paint(paramGraphics);
    if (!isVisible) { return; }
    Graphics2D g2D = (Graphics2D)paramGraphics;
    final int i = getWidth();
    final int j = getHeight();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setColor(Color.darkGray);
    g2D.fillRect(0, (int)(j * 0.25D), i, 1);
    g2D.fillRect(0, (int)(j * 0.5D), i, 1);
    g2D.fillRect(0, (int)(j * 0.75D), i, 1);
    if (heapSize > 0L) {
      g2D.setColor(Color.red);
      final int k = (int)(j - ((j * hwm) / heapSize));
      final Stroke localStroke = g2D.getStroke();
      final float[] arrayOfFloat = {
          2.0F
      };
      final BasicStroke localBasicStroke = new BasicStroke(1.0F, 2, 0, 10.0F, arrayOfFloat, 0.0F);
      g2D.setStroke(localBasicStroke);
      g2D.drawLine(0, k, i, k);
      g2D.setStroke(localStroke);
      int m = 0;
      int n = j;
      g2D.setColor(Color.green);
      final int i1 = history.length - 1;
      int i3;
      int i4;
      for (int i2 = index; i2 < history.length; i2++) {
        i3 = ((i2 - index) * i) / i1;
        i4 = (int)(j - ((j * history[i2]) / heapSize));
        if (i2 > index) {
          g2D.drawLine(m, n, i3, i4);
        }
        m = i3;
        n = i4;
      }
      for (int i2 = 0; i2 < index; i2++) {
        i3 = ((i2 + (history.length - index)) * i) / i1;
        i4 = (int)(j - ((j * history[i2]) / heapSize));
        g2D.drawLine(m, n, i3, i4);
        m = i3;
        n = i4;
      }
    }
    g2D = null;
  }

  public void stop() {
    timer.cancel();
  }

  void updateData() {
    setHeapSize(Runtime.getRuntime().totalMemory());
    usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); // 0;//this.monitor.getUsed();
    hwm = Math.max(usedMem, hwm);
    history[index] = usedMem;
    index = ((index + 1) % history.length);
    if (isVisible) {
      repaint();
    }
  }

  void writeAllTo(final PrintWriter paramPrintWriter) {
    paramPrintWriter.print(history.length);
    paramPrintWriter.print(" " + heapSize);
    paramPrintWriter.print(" " + hwm);
    for (int i = index; i < history.length; i++) {
      paramPrintWriter.print(" " + history[i]);
    }
    for (int i = 0; i < index; i++) {
      paramPrintWriter.print(" " + history[i]);
    }
    paramPrintWriter.println();
  }

  void readAllFrom(final BufferedReader paramBufferedReader) throws IOException {
    stop();
    final String str = paramBufferedReader.readLine();
    final StringTokenizer localStringTokenizer = new StringTokenizer(str, " ");
    final int i = Integer.parseInt(localStringTokenizer.nextToken());
    history = new long[i];
    setHeapSize(Long.parseLong(localStringTokenizer.nextToken()));
    hwm = Integer.parseInt(localStringTokenizer.nextToken());
    index = (i - 1);
    for (int j = 0; j < i; j++) {
      history[j] = Long.parseLong(localStringTokenizer.nextToken());
    }
    updateData();
  }

  public void deInitialize() {
    stop();
    heapSize = 0L;
    hwm = 0L;
    history = null;
    index = 0;
    size = null;
  }

  private class GraphTimerTask extends TimerTask {

    private GraphTimerTask() {
    }

    @Override
    public void run() {
      updateData();
    }
  }

}
