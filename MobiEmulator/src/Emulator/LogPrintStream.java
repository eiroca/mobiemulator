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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LogPrintStream extends PrintStream {
    private PrintStream log       = null;
    public int          logLength = 0;
    private MobiEmulator mobiEmulatorInstance;
    public LogPrintStream(String logFilePath) throws IOException {
        super(System.out, true);
        if (logFilePath != null) {
            this.log = new PrintStream(new FileOutputStream(logFilePath));
        }
        System.setOut(this);
        System.setErr(this);
    }

    public void setMobiEmulatorInstance(MobiEmulator mobiEmulatorInstance) {
        this.mobiEmulatorInstance = mobiEmulatorInstance;
    }
    public void setLogFile(String logfilepath) throws IOException {
        this.log = new PrintStream(new FileOutputStream(logfilepath));
    }
    public void print(String str) {
        if (MobiEmulator.isPrintOff) {
            return;
        } else {
            try {
                if (log != null) {
                    this.log.print(str);
                }
                MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.getDocument().insertString(logLength, str, null);
                logLength += (str.length());
                MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.setCaretPosition(logLength);
                // super.print(logLength);
                super.print(str);
            } catch (Exception ex) {}
        }
    }
    public void println(String str) {
        if (MobiEmulator.isPrintOff) {
            return;
        } else {
            try {
                print(str);
                if (log != null) {
                    this.log.print("\n");
                }
                MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.getDocument().insertString(logLength, "\n", null);
                logLength += "\n".length();
                super.println();
            } catch (Exception ex) {}
        }
    }
    public void println() {
        println("");
    }
//  @Override
//  public void flush() {
//          this.log.flush();
//            super.flush();
//  }
//
//  @Override
//  public void write(byte[] b) {
//          try {
//                  this.log.write(b);
//                    MobiEmulator.mobiEmulatorInstance.canvasPanel.logTextArea.append(new String(b));
//                  super.write(b);
//          } catch (IOException e) {
//          }
//  }
//
//  @Override
//  public void write(byte[] b, int off, int len) {
//          this.log.write(b, off, len);
//          mobiEmulatorInstance.canvasPanel.logTextArea.append(new String(b, off, len));
//          mobiEmulatorInstance.canvasPanel.logTextArea
//                          .setCaretPosition(mobiEmulatorInstance.canvasPanel.logTextArea
//                                          .getDocument().getLength());
//          super.write(b, off, len);
//  }
//
//  @Override
//  public void write(int b) {
//          this.log.write(b);
//          mobiEmulatorInstance.canvasPanel.logTextArea.append(new String("" + b));
//
//          super.write(b);
//  }
}
