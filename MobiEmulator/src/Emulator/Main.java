/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Main {
    private static String   midpJarurl      = null;
    public static int       uniqueID        = 0;
    private static boolean  useCmd          = false;
    private static String   jarUrl          = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();    // for jar
    private static boolean  includeNoVerify = !true;
    private static Vector   content;
    // private static String cmdTag = Options.debugLabels[Options.EnableConsole];
    private static String[] contents;
    public static MobiEmulator frame;
    // path;
    private static String   jarDir;
    public static void main(String args[]) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }
        } catch (Exception e) {
            try {
                // If Nimbus is not available, you can set the GUI to another look and feel.
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ex) {}
        }
        final String params[] = args;
        try {
            if (jarUrl.startsWith("/")) {
                jarUrl = jarUrl.substring(1);
            }
            jarDir = jarUrl.substring(0, jarUrl.lastIndexOf("/"));
            jarUrl = jarUrl.replace("%20", " ");
            jarUrl = "\"" + jarUrl + "\"";
            // if(exeMode){
            // jarDir=".";
            // }
            boolean isStartCmd = !false;
            if (params.length > 1) {
                isStartCmd = (params[1].equals("true") ? true
                        : false);                      // arg 0 used for cmd startup
            }
            if (params.length > 0) {
                midpJarurl = params[0];
            }
            setUniqueId(params);
            boolean isConsoleEnabled = getConsoleEnabled();
            String  classespath      = ((getValueFromContents("Classespath") != null)
                                        ? getValueFromContents("Classespath")
                                        : "");
            if (((isConsoleEnabled ||!classespath.equals("") || includeNoVerify) && isStartCmd)) {
                getJavaExecutable();
                String jarLocation = jarUrl.substring(0, jarUrl.lastIndexOf("/"));
                if (jarLocation.startsWith("\"")) {
                    jarLocation = jarLocation.substring(1);
                }
                String cmdRun = "cmd.exe /c start ";
                if (!classespath.equals("")) {
                    cmdRun += "javaw";
                    cmdRun += " -noverify -javaagent:\"" + jarLocation + "/" + "JR.jar\" -Djreloader.dirs=\""
                              + classespath + "\"";    // for jreloader.jar
                } else {
                    cmdRun += (includeNoVerify ? "javaw"
                                               : "java");
                }
                // cmdRunclassespath -cp .;\""+classpath+"\"";
                cmdRun += " -noverify -jar " + jarUrl + ((midpJarurl != null) ? " " + midpJarurl
                        : "") + " false";
                // System.out.println(".. " + cmdRun);
                try {
                    Runtime.getRuntime().exec(cmdRun);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(1);
            } else {
                frame = new MobiEmulator();
                frame.setVisible(true);
                if ((midpJarurl != null) &&!midpJarurl.equals("")) {
                    frame.setJarFilePath(midpJarurl);
                    frame.loadJarFile(new File(midpJarurl));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getJavaExecutable() {
        String javaString = "";
        File   javaFile;
        if (((javaString = System.getProperty("java.home")) == null) || (javaString.length() < 1)) {
            javaString = "java";
        } else {
            javaString = javaString + "\\bin\\java.exe";
            if (((javaFile = new File(javaString)).exists()) && ((javaFile).isFile())) {
                javaString = "\"" + javaString + "\"";
            } else {
                javaString = "java";
            }
        }

        return javaString;
    }
    private static void setUniqueId(String[] params) {
        try {
            if (params.length > 1) {
                uniqueID = Integer.parseInt(params[1]);
            }
        } catch (Exception e) {}
    }
    public static boolean getConsoleEnabled() {
        // System.out.println("jarDir is " + jarDir);
        String filename = "./settings.txt";
        useCmd = false;
        if (new File(filename).exists()) {
            content = new Vector();
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(filename));
                String text = null;
                // repeat until all lines is read
                while ((text = reader.readLine()) != null) {
                    content.addElement(text);
                }
                if (contents == null) {
                    contents = new String[content.size()];
                }
                content.copyInto(contents);
                useCmd               = getValueFromContents(Options.debugLabels[Options.EnableConsole]).equals("TRUE")
                                       ? true
                                       : false;
                MobiEmulator.isAlwaysShowLog =
                    getValueFromContents(Options.debugLabels[Options.IsAlwaysLogVisible]).equals("TRUE") ? true
                        : false;
                MobiEmulator.isPrintOff = getValueFromContents(Options.debugLabels[Options.IsPrintOff]).equals("TRUE") ? true
                        : false;

                return useCmd;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        useCmd = false;

        return false;
    }
    public static String getRmsDir() {
        String filename = jarDir + "/settings.txt";
        String rmsDir   = "./RMS";
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return rmsDir;
    }
    private static String getValueFromContents(String string) {
        if (contents == null) {
            return null;
        }
        int len = contents.length;
        for (String content : contents) {
            if (content.startsWith(string)) {
                return (content.substring(content.indexOf("=") + 1));
            }
        }

        return "";
    }
}
