/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- non-JDK imports --------------------------------------------------------

import bsh.Interpreter;
import bsh.util.JConsole;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------
/**
 *
 * @author Ashok Kumar Gujarathi
 */

public class BeanShell extends JFrame implements Runnable {
    private String       helpUrl = "http://www.beanshell.org/manual/bshmanual.html";
    private JConsole     beanConsole;
    private JPanel       beanConsolePanel;
    private Interpreter  beanInterpreter;
    private Thread       beanThread;
    private JMenu        fileMenu;
    private JMenu        helpMenu;
    private JMenuItem    helpMenuItem;
    private JFileChooser jFileChooser;
    private JMenuItem    loadScriptMenuItem;
    private JMenuBar     menuBar;
    public BeanShell() {
        initComponents();
        setTitle("BeanShell");
        setSize(400, 300);
        beanConsole = new JConsole();
        getContentPane().add(beanConsole, "Center");
        beanInterpreter = new Interpreter(beanConsole);
        beanInterpreter.setClassLoader(MobiEmulator.mobiEmulatorInstance.getCustomCLassLoader());
        try {
            beanInterpreter.eval("setAccessibility(true);");
        } catch (Exception e) {}
        beanThread = new Thread(this);
        beanThread.start();
    }

    public void run() {
        beanInterpreter.run();
    }
    private void initComponents() {
        beanConsolePanel   = new JPanel();
        menuBar            = new JMenuBar();
        fileMenu           = new JMenu();
        loadScriptMenuItem = new JMenuItem();
        helpMenu           = new JMenu();
        helpMenuItem       = new JMenuItem();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });
        beanConsolePanel.setLayout(new BorderLayout());
        getContentPane().add(beanConsolePanel, "North");
        fileMenu.setText("File");
        loadScriptMenuItem.setText("Load script...");
        loadScriptMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loadScript(evt);
            }
        });
        fileMenu.add(loadScriptMenuItem);
        menuBar.add(fileMenu);
        helpMenu.setText("Help");
        helpMenuItem.setText("BeanShell online documentation");
        helpMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                help(evt);
            }
        });
        helpMenu.add(helpMenuItem);
        // menuBar.add(helpMenu);
        setJMenuBar(menuBar);
        pack();
    }
    private void help(ActionEvent evt) {
        try {
            Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + helpUrl);
        } catch (Exception e) {}
    }
    private void loadScript(ActionEvent evt) {
        if (jFileChooser == null) {
            jFileChooser = new JFileChooser(new File("."));
            jFileChooser.setFileFilter(new ScriptFileFilter());
        }
        if (jFileChooser.showSaveDialog(this) == 0) {
            try {
                String path = jFileChooser.getSelectedFile().getAbsolutePath();
                beanConsole.print("executing " + path, new Color(0, 128, 0));
                beanConsole.println();
                beanInterpreter.source(path);
                beanConsole.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void formComponentResized(ComponentEvent evt) {}
    private void exitForm(WindowEvent evt) {
        // deinitialize();
    }
    public void deinitialize() {
        beanConsolePanel = null;
        beanInterpreter  = null;
        beanInterpreter  = null;
        beanConsole      = null;
        helpMenu         = null;
        fileMenu         = null;
        if ((beanThread != null) && beanThread.isAlive()) {
            try {
                beanThread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(BeanShell.class.getName()).log(Level.SEVERE, null, ex);
            }
            beanThread = null;
        }
    }
    class ScriptFileFilter extends FileFilter {
        ScriptFileFilter() {}

        @Override
        public boolean accept(File f) {
            return (f.isDirectory()) || (f.getName().toLowerCase().endsWith(".bsh"));
        }
        @Override
        public String getDescription() {
            return "BeanShell Script Files (*.bsh)";
        }
    }
}
