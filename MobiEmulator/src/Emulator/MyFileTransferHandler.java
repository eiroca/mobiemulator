/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class MyFileTransferHandler extends TransferHandler {
    private String        tempZipdir = "./tempDir";
    private MobiEmulator mobiEmulatorInstance;
    private BufferedImage image;
    private String        jarfileName;
    public MyFileTransferHandler(BufferedImage image) {
        this.image = image;
    }

    public MyFileTransferHandler(MobiEmulator Instance) {
        mobiEmulatorInstance = Instance;
    }

    /**
     * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent,
     *      java.awt.datatransfer.DataFlavor[])
     */
    @Override
    public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
        for (int i = 0;i < arg1.length;i++) {
            DataFlavor flavor = arg1[i];
            if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                flavor = null;

                return true;
            }
            if (flavor.equals(DataFlavor.stringFlavor)) {
                flavor = null;

                return true;
            }
            System.err.println("canImport: Rejected Flavor: " + flavor);
        }

        // Didn't find any that match, so:
        return false;
    }

    /**
     * Do the actual import.
     *
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent,
     *      java.awt.datatransfer.Transferable)
     */
    @Override
    public boolean importData(JComponent comp, Transferable t) {
        DataFlavor[] flavors = t.getTransferDataFlavors();
        for (int i = 0;i < flavors.length;i++) {
            DataFlavor flavor = flavors[i];
            try {
                if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                    List     l    = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iter = l.iterator();
                    while (iter.hasNext()) {
                        File file = (File) iter.next();
                        System.out.println("GOT FILE: " + file.getCanonicalPath());
                        if (file.getCanonicalPath().endsWith("zip")) {
                            unZip(file.getCanonicalPath(), tempZipdir);
                            if (jarfileName != null) {
                                file = new File(jarfileName);
                            }
                        }
                        if (file.getCanonicalPath().endsWith("jar")) {
                            mobiEmulatorInstance.unLoadCurrentJar();
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {}
                            if (mobiEmulatorInstance.isCurrentJarUnloaded) {
                                mobiEmulatorInstance.setJarFileName(file.getName());
                                mobiEmulatorInstance.setJarFilePath(file.getCanonicalPath());
                                mobiEmulatorInstance.appendMRUToList(file.getCanonicalPath());
                                mobiEmulatorInstance.loadJarFile(file);
                                System.out.println(" jar file name is " + file.getName());
                            }
                        }
                        file = null;
                    }
                    l    = null;
                    iter = null;

                    return true;
                } else if (flavor.equals(DataFlavor.stringFlavor)) {
                    String fileOrURL = (String) t.getTransferData(flavor);
                    if (fileOrURL.substring(fileOrURL.lastIndexOf(".") + 1).equals("jar")) {
                        mobiEmulatorInstance.setJarFileName(new File(fileOrURL).getName());
                        mobiEmulatorInstance.setJarFilePath(fileOrURL);
                        File jarFile = new File(fileOrURL);
                        mobiEmulatorInstance.loadJarFile(jarFile);
                        jarFile = null;
                    }

                    return true;
                    // now do something with the String.
                } else {
                    System.out.println("importData rejected: " + flavor);
                    // Don't return; try next flavor.
                }
            } catch (IOException ex) {
                System.err.println("IOError getting data: " + ex);
            } catch (UnsupportedFlavorException e) {
                System.err.println("Unsupported Flavor: " + e);
            }
        }
        flavors = null;

        return false;
    }
    // Returns Image object housed by Transferable object
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }

        // else return the payload
        return image;
    }
    private void delete(File f) throws IOException {
        if (f.isDirectory()) {
            File files[] = f.listFiles();
            int  len     = files.length;
            for (int i = 0;i < len;i++) {
                delete(files[i]);
            }
            files = null;
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
    }
    private void unZip(String canonicalPath, String tempZipdir) throws FileNotFoundException, IOException {
        File tempFolder = new File(tempZipdir);
        if (tempFolder.exists()) {
            delete(tempFolder);
        }
        if (!new File(tempZipdir).exists()) {
            new File(tempZipdir).mkdirs();
        }
        ZipInputStream zinstream  = new ZipInputStream(new FileInputStream(canonicalPath));
        ZipEntry       zentry     = zinstream.getNextEntry();
        boolean        isJarFound = false;
        while (zentry != null) {
            String entryName = zentry.getName();
            if (zentry.isDirectory()) {
                new File(tempZipdir + "/" + zentry.getName()).mkdirs();
            }
            if (entryName.endsWith("jar") || entryName.endsWith("jad")) {
                if (entryName.endsWith("jar")) {
                    isJarFound = true;
                }
                byte             buf[]     = new byte[1024];
                FileOutputStream outstream = new FileOutputStream(tempZipdir + "/" + entryName);
                int              n;
                while ((n = zinstream.read(buf, 0, 1024)) > -1) {
                    outstream.write(buf, 0, n);
                }
                outstream.close();
                outstream = null;
            }
            if (isJarFound) {
                jarfileName = (tempZipdir + "/" + entryName);
            }
            zinstream.closeEntry();
            zentry = zinstream.getNextEntry();
        }
        zentry = null;
        zinstream.close();
        zinstream = null;
    }
}
