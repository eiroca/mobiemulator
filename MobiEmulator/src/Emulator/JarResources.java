package Emulator;

//~--- JDK imports ------------------------------------------------------------

/*
*Emulatorge this template, choose Tools | Templates
* and open the template in the editor.
 */

/**
 *
 * @author Ashok Kumar Gujarathi
 */

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * JarResources: JarResources maps all resources included in a Zip or Jar file.
 * Additionaly, it provides a method to extract one as a blob.
 */
public final class JarResources {
    // external debug flag
    public boolean    debugOn       = false;
    // jar resource mapping tables
    private Hashtable htSizes       = new Hashtable();
    private Hashtable htJarContents = new Hashtable();
    // a jar file
    private String    jarFileName;
    public JarResources() {}

    /**
     * creates a JarResources. It extracts all resources from a Jar into an
     * internal hashtable, keyed by resource names.
     *
     * @param jarFileName
     *            a jar or zip file
     */
    public JarResources(String jarFileName) {
        this.jarFileName = jarFileName;
        init();
    }

    /**
     * Extracts a jar resource as a blob.
     *
     * @param name
     *            a resource name.
     */
    public byte[] getResource(String name) {
        /*
         * if (classNameReplacementChar == '\u0000') { // '/' is used to map the
         * package to the path name= name.replace('.', '/') ; } else {
         * Replace '.' with custom char, such as '_' name= name.replace('.',
         * classNameReplacementChar) ; }
         */
        if (htJarContents.get(name) == null) {
            return null;
        }
        System.out.println(">> Cached jar resource name " + name + " size is "
                           + ((byte[]) htJarContents.get(name)).length + " bytes");

        return (byte[]) htJarContents.get(name);
    }

    /** initializes internal hash tables with Jar file resources. */
    private void init() {
        try {
            // extracts just sizes only.
            ZipFile     zf     = new ZipFile(jarFileName);
            Enumeration e      = zf.entries();
            ZipEntry    zentry = null;
            while (e.hasMoreElements()) {
                zentry = (ZipEntry) e.nextElement();
                if (debugOn) {
                    System.out.println(dumpZipEntry(zentry));
                }
                htSizes.put(zentry.getName(), (int) zentry.getSize());
            }
            if (zf != null) {
                zf.close();
            }
            zf     = null;
            e      = null;
            zentry = null;
            // extract resources and put them into the hashtable.
            FileInputStream     fis = new FileInputStream(jarFileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipInputStream      zis = new ZipInputStream(bis);
            ZipEntry            ze  = null;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }
                if (debugOn) {
                    System.out.println("ze.getName()=" + ze.getName() + "," + "getSize()=" + ze.getSize());
                }
                int size = (int) ze.getSize();
                // -1 means unknown size.
                if (size == -1) {
                    size = ((Integer) htSizes.get(ze.getName())).intValue();
                }
                byte[] b     = new byte[size];
                int    rb    = 0;
                int    chunk = 0;
                while ((size - rb) > 0) {
                    chunk = zis.read(b, rb, size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }
                // add to internal resource hashtable
                htJarContents.put(ze.getName(), b);
                b = null;
                if (debugOn) {
                    System.out.println(ze.getName() + "  rb=" + rb + ",size=" + size + ",csize="
                                       + ze.getCompressedSize());
                }
            }
            if (fis != null) {
                fis.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (zis != null) {
                zis.close();
            }
            fis = null;
            bis = null;
            zis = null;
            ze  = null;
        } catch (NullPointerException e) {
            System.out.println("done.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dumps a zip entry into a string.
     *
     * @param ze
     *            a ZipEntry
     */
    private String dumpZipEntry(ZipEntry ze) {
        StringBuffer sb = new StringBuffer();
        if (ze.isDirectory()) {
            sb.append("d ");
        } else {
            sb.append("f ");
        }
        if (ze.getMethod() == ZipEntry.STORED) {
            sb.append("stored   ");
        } else {
            sb.append("defalted ");
        }
        sb.append(ze.getName());
        sb.append("\t");
        sb.append("");
        sb.append(ze.getSize());
        if (ze.getMethod() == ZipEntry.DEFLATED) {
            sb.append("/");
            sb.append(ze.getCompressedSize());
        }
        String str = sb.toString();
        sb = null;

        return (str);
    }

    /**
     * Is a test driver. Given a jar file and a resource name, it trys to
     * extract the resource and then tells us whether it could or not.
     *
     * <strong>Example</strong> Let's say you have a JAR file which jarred up a
     * bunch of gif image files. Now, by using JarResources, you could extract,
     * create, and display those images on-the-fly.
     *
     * <pre>
     *     ...
     *     JarResources JR=new JarResources("GifBundle.jar");
     *     Image image=Toolkit.createImage(JR.getResource("logo.gif");
     *     Image logo=Toolkit.getDefaultToolkit().createImage(
     *                   JR.getResources("logo.gif")
     *                   );
     *     ...
     * </pre>
     */
    /*
     * public static void main(String[] args) throws IOException { if
     * (args.length!=2) { System.err.println(
     * "usage: java JarResources <jar file name> <resource name>" );
     * System.exit(1); }
     *
     * JarResources jr=new JarResources(args[0]); byte[]
     * buff=jr.getResource(args[1]); if (buff==null) {
     * System.out.println("Could not find "+args[1]+"."); } else {
     * System.out.println("Found "+args[1]+ " (length="+buff.length+")."); } }
     */
    public Hashtable getJarCache() {
        return htJarContents;
    }
    public void setJarCache(Hashtable jarcache) {
        htJarContents = jarcache;
    }
    public void deInitialize() {
        htSizes.clear();
        htSizes = null;
        htJarContents.clear();
        htJarContents = null;
    }
}    // End of JarResources class.

