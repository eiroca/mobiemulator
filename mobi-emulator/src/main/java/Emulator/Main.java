package Emulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
// ~--- JDK imports ------------------------------------------------------------
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Main {

  private static String midpJarurl = null;
  public static int uniqueID = 0;
  private static boolean useCmd = false;
  private static String jarUrl = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(); // for jar
  private static boolean includeNoVerify = !true;
  private static Vector content;
  // private static String cmdTag = Options.debugLabels[Options.EnableConsole];
  private static String[] contents;
  public static MobiEmulator frame;
  // path;
  private static String jarDir;

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
      if (Main.jarUrl.startsWith("/")) {
        Main.jarUrl = Main.jarUrl.substring(1);
      }
      Main.jarDir = Main.jarUrl.substring(0, Main.jarUrl.lastIndexOf("/"));
      Main.jarUrl = Main.jarUrl.replace("%20", " ");
      Main.jarUrl = "\"" + Main.jarUrl + "\"";
      // if(exeMode){
      // jarDir=".";
      // }
      boolean isStartCmd = !false;
      if (params.length > 1) {
        isStartCmd = (params[1].equals("true") ? true
            : false); // arg 0 used for cmd startup
      }
      if (params.length > 0) {
        Main.midpJarurl = params[0];
      }
      Main.setUniqueId(params);
      final boolean isConsoleEnabled = Main.getConsoleEnabled();
      final String classespath = ((Main.getValueFromContents("Classespath") != null)
          ? Main.getValueFromContents("Classespath")
          : "");
      if (((isConsoleEnabled || !classespath.equals("") || Main.includeNoVerify) && isStartCmd)) {
        Main.getJavaExecutable();
        String jarLocation = Main.jarUrl.substring(0, Main.jarUrl.lastIndexOf("/"));
        if (jarLocation.startsWith("\"")) {
          jarLocation = jarLocation.substring(1);
        }
        String cmdRun = "cmd.exe /c start ";
        if (!classespath.equals("")) {
          cmdRun += "javaw";
          cmdRun += " -noverify -javaagent:\"" + jarLocation + "/" + "JR.jar\" -Djreloader.dirs=\""
              + classespath + "\""; // for jreloader.jar
        }
        else {
          cmdRun += (Main.includeNoVerify ? "javaw"
              : "java");
        }
        // cmdRunclassespath -cp .;\""+classpath+"\"";
        cmdRun += " -noverify -jar " + Main.jarUrl + ((Main.midpJarurl != null) ? " " + Main.midpJarurl
            : "") + " false";
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
        Main.frame = new MobiEmulator();
        Main.frame.setVisible(true);
        if ((Main.midpJarurl != null) && !Main.midpJarurl.equals("")) {
          Main.frame.setJarFilePath(Main.midpJarurl);
          Main.frame.loadJarFile(new File(Main.midpJarurl));
        }
      }
    }
    catch (final Exception e) {
      e.printStackTrace();
    }
  }

  private static String getJavaExecutable() {
    String javaString = "";
    File javaFile;
    if (((javaString = System.getProperty("java.home")) == null) || (javaString.length() < 1)) {
      javaString = "java";
    }
    else {
      javaString = javaString + "\\bin\\java.exe";
      if (((javaFile = new File(javaString)).exists()) && ((javaFile).isFile())) {
        javaString = "\"" + javaString + "\"";
      }
      else {
        javaString = "java";
      }
    }

    return javaString;
  }

  private static void setUniqueId(final String[] params) {
    try {
      if (params.length > 1) {
        Main.uniqueID = Integer.parseInt(params[1]);
      }
    }
    catch (final Exception e) {
    }
  }

  public static boolean getConsoleEnabled() {
    // System.out.println("jarDir is " + jarDir);
    final String filename = "./settings.txt";
    Main.useCmd = false;
    if (new File(filename).exists()) {
      Main.content = new Vector();
      BufferedReader reader;
      try {
        reader = new BufferedReader(new FileReader(filename));
        String text = null;
        // repeat until all lines is read
        while ((text = reader.readLine()) != null) {
          Main.content.addElement(text);
        }
        if (Main.contents == null) {
          Main.contents = new String[Main.content.size()];
        }
        Main.content.copyInto(Main.contents);
        Main.useCmd = Main.getValueFromContents(Options.debugLabels[Options.EnableConsole]).equals("TRUE")
            ? true
            : false;
        MobiEmulator.isAlwaysShowLog = Main.getValueFromContents(Options.debugLabels[Options.IsAlwaysLogVisible]).equals("TRUE") ? true
            : false;
        MobiEmulator.isPrintOff = Main.getValueFromContents(Options.debugLabels[Options.IsPrintOff]).equals("TRUE") ? true
            : false;

        return Main.useCmd;
      }
      catch (final FileNotFoundException e) {
        e.printStackTrace();
      }
      catch (final IOException ex) {
        ex.printStackTrace();
      }
    }
    Main.useCmd = false;

    return false;
  }

  public static String getRmsDir() {
    final String filename = Main.jarDir + "/settings.txt";
    String rmsDir = "./RMS";
    if (new File(filename).exists()) {
      BufferedReader reader;
      try {
        reader = new BufferedReader(new FileReader(filename));
        String text = null;
        // repeat until all lines is read
        while ((text = reader.readLine()) != null) {
          if (text.startsWith("RMSPATH")) {
            rmsDir = text.substring(text.indexOf(text) + "RMSPATH".length() + 1);
          }
        }

        return rmsDir;
      }
      catch (final FileNotFoundException e) {
        e.printStackTrace();
      }
      catch (final IOException ex) {
        ex.printStackTrace();
      }
    }

    return rmsDir;
  }

  private static String getValueFromContents(final String string) {
    if (Main.contents == null) { return null; }
    for (final String content : Main.contents) {
      if (content.startsWith(string)) { return (content.substring(content.indexOf("=") + 1)); }
    }

    return "";
  }

}
