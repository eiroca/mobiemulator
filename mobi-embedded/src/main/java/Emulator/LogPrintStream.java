package Emulator;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class LogPrintStream extends PrintStream {

  private PrintStream log = null;
  public int logLength = 0;
  private MobiEmulator mobiEmulatorInstance;

  public LogPrintStream(final String logFilePath) throws IOException {
    super(System.out, true);
    if (logFilePath != null) {
      log = new PrintStream(new FileOutputStream(logFilePath));
    }
    System.setOut(this);
    System.setErr(this);
  }

  public void setMobiEmulatorInstance(final MobiEmulator mobiEmulatorInstance) {
    this.mobiEmulatorInstance = mobiEmulatorInstance;
  }

  public void setLogFile(final String logfilepath) throws IOException {
    log = new PrintStream(new FileOutputStream(logfilepath));
  }

  @Override
  public void print(final String str) {
    if (MobiEmulator.isPrintOff) {
      return;
    }
    else {
      try {
        if (log != null) {
          log.print(str);
        }
        MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.getDocument().insertString(logLength, str, null);
        logLength += (str.length());
        MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.setCaretPosition(logLength);
        // super.print(logLength);
        super.print(str);
      }
      catch (final Exception ex) {
      }
    }
  }

  @Override
  public void println(final String str) {
    if (MobiEmulator.isPrintOff) {
      return;
    }
    else {
      try {
        print(str);
        if (log != null) {
          log.print("\n");
        }
        MobiEmulator.mobiEmulatorInstance.logFrame.logTextArea.getDocument().insertString(logLength, "\n", null);
        logLength += "\n".length();
        super.println();
      }
      catch (final Exception ex) {
      }
    }
  }

  @Override
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
