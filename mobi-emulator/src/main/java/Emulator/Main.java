package Emulator;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
// ~--- JDK imports ------------------------------------------------------------
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import mobiemulator.Environment;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Main {

  public static void main(final String args[]) {
    try {
      for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Windows".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }
    catch (final Exception e) {
      try {
        // If Nimbus is not available, you can set the GUI to another look and feel.
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      }
      catch (final Exception ex) {
      }
    }
    final String params[] = args;
    try {
      if (MobiEmulator.jarUrl.startsWith("/")) {
        MobiEmulator.jarUrl = MobiEmulator.jarUrl.substring(1);
      }
      MobiEmulator.jarDir = MobiEmulator.jarUrl.substring(0, MobiEmulator.jarUrl.lastIndexOf("/"));
      MobiEmulator.jarUrl = MobiEmulator.jarUrl.replace("%20", " ");
      MobiEmulator.jarUrl = "\"" + MobiEmulator.jarUrl + "\"";
      // if(exeMode){jarDir=".";}
      boolean isStartCmd = !false;
      if (params.length > 1) {
        isStartCmd = (params[1].equals("true") ? true : false); // arg 0 used for cmd startup
      }
      if (params.length > 0) {
        MobiEmulator.midpJarurl = params[0];
      }
      MobiEmulator.setUniqueId(params);
      final boolean isConsoleEnabled = MobiEmulator.getConsoleEnabled();
      final String classespath = ((MobiEmulator.getValueFromContents("Classespath") != null) ? MobiEmulator.getValueFromContents("Classespath") : "");
      if (((isConsoleEnabled || !classespath.equals("") || MobiEmulator.includeNoVerify) && isStartCmd)) {
        MobiEmulator.getJavaExecutable();
        String jarLocation = MobiEmulator.jarUrl.substring(0, MobiEmulator.jarUrl.lastIndexOf("/"));
        if (jarLocation.startsWith("\"")) {
          jarLocation = jarLocation.substring(1);
        }
        String cmdRun = "cmd.exe /c start ";
        if (!classespath.equals("")) {
          cmdRun += "javaw";
          // for jreloader.jar
          cmdRun += " -noverify -javaagent:\"" + jarLocation + "/" + "JR.jar\" -Djreloader.dirs=\"" + classespath + "\"";
        }
        else {
          cmdRun += (MobiEmulator.includeNoVerify ? "javaw" : "java");
        }
        // cmdRunclassespath -cp .;\""+classpath+"\"";
        cmdRun += " -noverify -jar " + MobiEmulator.jarUrl + ((MobiEmulator.midpJarurl != null) ? " " + MobiEmulator.midpJarurl : "") + " false";
        // System.out.println(".. " + cmdRun);
        try {
          Runtime.getRuntime().exec(cmdRun);
        }
        catch (final IOException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(1);
      }
      else {
        MobiEmulator frame = new MobiEmulator();
        Environment.instance = frame;
        frame.setVisible(true);
        if ((MobiEmulator.midpJarurl != null) && !MobiEmulator.midpJarurl.equals("")) {
          frame.setJarFilePath(MobiEmulator.midpJarurl);
          frame.loadJarFile(new File(MobiEmulator.midpJarurl));
        }
      }
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

}
