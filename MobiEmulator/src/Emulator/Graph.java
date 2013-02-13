/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

/**
 *
 * @author Ashok Kumar Gujarathi
 */

import javax.swing.JPanel;
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

class Graph extends JPanel {
    private static final String    DELIM          = " ";
    private static final int       HISTORY_LENGTH = 200;
    private static final int       UPDATE_PERIOD  = 1000;
    private static final Dimension PREFERRED_SIZE = new Dimension(200, 200);
    private static final Dimension MINIMUM_SIZE   = new Dimension(40, 40);
    public boolean                 isVisible      = false;
    private long                   heapSize;
    private long[]                 history;
    private long                   hwm;
    private int                    index;
    // MemoryMonitor monitor;
    Dimension                      size;
    private Timer                  timer;
    private long                   usedMem;
    public Graph() {
        // this.monitor = paramMemoryMonitor;
        this.history = new long[200];
        this.index   = 0;
        this.hwm     = 0L;
        setBackground(Color.black);
        this.timer = new Timer();
        this.timer.schedule(new GraphTimerTask(), 0L, UPDATE_PERIOD);
    }

    public void setHeapSize(long paramLong) {
        this.heapSize = paramLong;
    }
    public Dimension getPreferredSize() {
        return PREFERRED_SIZE;
    }
    public Dimension getMinimumSize() {
        return MINIMUM_SIZE;
    }
    public void paint(Graphics paramGraphics) {
        super.paint(paramGraphics);
        if (!isVisible) {
            return;
        }
        Graphics2D g2D = (Graphics2D) paramGraphics;
        int        i               = getWidth();
        int        j               = getHeight();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setColor(Color.darkGray);
        g2D.fillRect(0, (int) (j * 0.25D), i, 1);
        g2D.fillRect(0, (int) (j * 0.5D), i, 1);
        g2D.fillRect(0, (int) (j * 0.75D), i, 1);
        if (this.heapSize > 0L) {
            g2D.setColor(Color.red);
            int         k                = (int) (j - j * this.hwm / this.heapSize);
            Stroke      localStroke      = g2D.getStroke();
            float[]     arrayOfFloat     = { 2.0F };
            BasicStroke localBasicStroke = new BasicStroke(1.0F, 2, 0, 10.0F, arrayOfFloat, 0.0F);
            g2D.setStroke(localBasicStroke);
            g2D.drawLine(0, k, i, k);
            g2D.setStroke(localStroke);
            int m = 0;
            int n = j;
            g2D.setColor(Color.green);
            int i1 = this.history.length - 1;
            int i3;
            int i4;
            for (int i2 = this.index;i2 < this.history.length;i2++) {
                i3 = (i2 - this.index) * i / i1;
                i4 = (int) (j - j * this.history[i2] / this.heapSize);
                if (i2 > this.index) {
                    g2D.drawLine(m, n, i3, i4);
                }
                m = i3;
                n = i4;
            }
            for (int i2 = 0;i2 < this.index;i2++) {
                i3 = (i2 + (this.history.length - this.index)) * i / i1;
                i4 = (int) (j - j * this.history[i2] / this.heapSize);
                g2D.drawLine(m, n, i3, i4);
                m = i3;
                n = i4;
            }
        }
        g2D = null;
    }
    public void stop() {
        this.timer.cancel();
    }
    void updateData() {
        setHeapSize(Runtime.getRuntime().totalMemory());
        usedMem                  = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();    // 0;//this.monitor.getUsed();
        this.hwm                 = Math.max(usedMem, this.hwm);
        this.history[this.index] = usedMem;
        this.index               = ((this.index + 1) % this.history.length);
        if (isVisible) {
            repaint();
        }
    }
    void writeAllTo(PrintWriter paramPrintWriter) {
        paramPrintWriter.print(this.history.length);
        paramPrintWriter.print(" " + this.heapSize);
        paramPrintWriter.print(" " + this.hwm);
        for (int i = this.index;i < this.history.length;i++) {
            paramPrintWriter.print(" " + this.history[i]);
        }
        for (int i = 0;i < this.index;i++) {
            paramPrintWriter.print(" " + this.history[i]);
        }
        paramPrintWriter.println();
    }
    void readAllFrom(BufferedReader paramBufferedReader) throws IOException {
        stop();
        String          str                  = paramBufferedReader.readLine();
        StringTokenizer localStringTokenizer = new StringTokenizer(str, " ");
        int             i                    = Integer.parseInt(localStringTokenizer.nextToken());
        this.history = new long[i];
        setHeapSize(Long.parseLong(localStringTokenizer.nextToken()));
        this.hwm   = Integer.parseInt(localStringTokenizer.nextToken());
        this.index = (i - 1);
        for (int j = 0;j < i;j++) {
            this.history[j] = Long.parseLong(localStringTokenizer.nextToken());
        }
        updateData();
    }
    public void deInitialize() {
        this.stop();
        this.heapSize = 0L;
        this.hwm      = 0L;
        this.history  = null;
        this.index    = 0;
        size          = null;
    }
    private class GraphTimerTask extends TimerTask {
        private GraphTimerTask() {}

        public void run() {
            Graph.this.updateData();
        }
    }
}
