package Emulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
// ~--- JDK imports ------------------------------------------------------------
/*
 * MJPEGGenerator.java Created on April 17, 2006, 11:48 PM To change this template, choose Tools |
 * Options and locate the template under the Source Creation and Management node. Right-click the
 * template and choose Open. You can then make changes to the template in the Source Editor.
 */
import javax.imageio.ImageIO;

/**
 *
 * @author monceaux
 */
public class MJPEGGenerator {

  private static double framerate = 24.0;
  private static int slideDuration = 3; // seconds
  private static int transitionDuration = 1; // seconds
  private static int numFrames = (int)((MJPEGGenerator.framerate * (MJPEGGenerator.slideDuration + MJPEGGenerator.transitionDuration))
      + (MJPEGGenerator.transitionDuration * MJPEGGenerator.framerate));
  private FileChannel aviChannel = null;
  private File aviFile = null;
  private long aviMovieOffset = 0;
  private FileOutputStream aviOutput = null;
  private int height = 0;
  AVIIndexList indexlist = null;
  /*
   * Info needed for MJPEG AVI
   *
   * - size of file minus "RIFF & 4 byte file size"
   */
  private int width = 0;

  /** creates a new instancce of MJPEGGenerator */
  public MJPEGGenerator(final File aviFile, final int width, final int height) {
    // defaultVideoSettings();
    this(aviFile, width, height, MJPEGGenerator.framerate, MJPEGGenerator.numFrames);
  }

  /** Creates a new instance of MJPEGGenerator */
  public MJPEGGenerator(final File aviFile, final int width, final int height, final double framerate, final int numFrames) {
    try {
      this.aviFile = aviFile;
      this.width = width;
      this.height = height;
      MJPEGGenerator.framerate = framerate;
      MJPEGGenerator.numFrames = numFrames;
      aviOutput = new FileOutputStream(aviFile);
      aviChannel = aviOutput.getChannel();
      final RIFFHeader rh = new RIFFHeader();
      aviOutput.write(rh.toBytes());
      aviOutput.write(new AVIMainHeader().toBytes());
      aviOutput.write(new AVIStreamList().toBytes());
      aviOutput.write(new AVIStreamHeader().toBytes());
      aviOutput.write(new AVIStreamFormat().toBytes());
      aviOutput.write(new AVIJunk().toBytes());
      aviMovieOffset = aviChannel.position();
      aviOutput.write(new AVIMovieList().toBytes());
      indexlist = new AVIIndexList();
    }
    catch (final Exception ex) {
      Logger.getLogger(MJPEGGenerator.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void addImage(final Image image) throws Exception {
    final byte[] fcc = new byte[] {
        '0', '0', 'd', 'b'
    };
    byte[] imagedata = writeImageToBytes(image);
    int useLength = imagedata.length;
    final long position = aviChannel.position();
    final int extra = (useLength + (int)position) % 4;
    if (extra > 0) {
      useLength = useLength + extra;
    }
    indexlist.addAVIIndex((int)position, useLength);
    aviOutput.write(fcc);
    aviOutput.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(useLength)));
    aviOutput.write(imagedata);
    if (extra > 0) {
      for (int i = 0; i < extra; i++) {
        aviOutput.write(0);
      }
    }
    imagedata = null;
  }

  public void finishAVI() throws Exception {
    final byte[] indexlistBytes = indexlist.toBytes();
    aviOutput.write(indexlistBytes);
    aviOutput.close();
    final long size = aviFile.length();
    final RandomAccessFile raf = new RandomAccessFile(aviFile, "rw");
    raf.seek(4);
    raf.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt((int)size - 8)));
    raf.seek(aviMovieOffset + 4);
    raf.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt((int)(size - 8 - aviMovieOffset - indexlistBytes.length))));
    raf.close();
  }

  // public void writeAVI(File file) throws Exception
  // {
  // OutputStream os = new FileOutputStream(file);
  //
  // // RIFFHeader
  // // AVIMainHeader
  // // AVIStreamList
  // // AVIStreamHeader
  // // AVIStreamFormat
  // // write 00db and image bytes...
  // }
  public static int swapInt(final int v) {
    return (v >>> 24) | (v << 24) | ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
  }

  public static short swapShort(final short v) {
    return (short)((v >>> 8) | (v << 8));
  }

  public static byte[] intBytes(final int i) {
    final byte[] b = new byte[4];
    b[0] = (byte)(i >>> 24);
    b[1] = (byte)((i >>> 16) & 0x000000FF);
    b[2] = (byte)((i >>> 8) & 0x000000FF);
    b[3] = (byte)(i & 0x000000FF);

    return b;
  }

  public static byte[] shortBytes(final short i) {
    final byte[] b = new byte[2];
    b[0] = (byte)(i >>> 8);
    b[1] = (byte)(i & 0x000000FF);

    return b;
  }

  private byte[] writeImageToBytes(final Image image) throws Exception {
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Graphics2D g = bi.createGraphics();
    g.drawImage(image, 0, 0, width, height, null);
    ImageIO.write(bi, "jpg", baos);
    baos.close();
    bi = null;
    g = null;

    return baos.toByteArray();
  }

  public void defaultVideoSettings() {
    MJPEGGenerator.framerate = 12.0;
    MJPEGGenerator.transitionDuration = 1; // seconds
    MJPEGGenerator.slideDuration = 3; // seconds
    MJPEGGenerator.numFrames = (int)((MJPEGGenerator.framerate * (MJPEGGenerator.slideDuration + MJPEGGenerator.transitionDuration))
        + (MJPEGGenerator.transitionDuration * MJPEGGenerator.framerate));
  }

  public double getFrameRate() {
    return MJPEGGenerator.framerate;
  }

  public int getNumFrames() {
    return MJPEGGenerator.numFrames;
  }

  private class AVIIndex {

    public int dwFlags = 16;
    public int dwOffset = 0;
    public int dwSize = 0;
    public byte[] fcc = new byte[] {
        '0', '0', 'd', 'b'
    };

    public AVIIndex(final int dwOffset, final int dwSize) {
      this.dwOffset = dwOffset;
      this.dwSize = dwSize;
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwFlags)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwOffset)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwSize)));
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIIndexList {

    public int cb = 0;
    public byte[] fcc = new byte[] {
        'i', 'd', 'x', '1'
    };
    public ArrayList ind = new ArrayList();

    public AVIIndexList() {
    }

    public void addAVIIndex(final int dwOffset, final int dwSize) {
      ind.add(new AVIIndex(dwOffset, dwSize));
    }

    public byte[] toBytes() throws Exception {
      cb = 16 * ind.size();
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(cb)));
      for (int i = 0; i < ind.size(); i++) {
        final AVIIndex in = (AVIIndex)ind.get(i);
        baos.write(in.toBytes());
      }
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIJunk {

    public byte[] fcc = new byte[] {
        'J', 'U', 'N', 'K'
    };
    public int size = 1808;
    public byte[] data = new byte[size];

    public AVIJunk() {
      Arrays.fill(data, (byte)0);
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(size)));
      baos.write(data);
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIMainHeader {

    public int cb = 56;
    public int dwFlags = 65552;
    public int dwHeight = 0; // replace with correct value
    public int dwInitialFrames = 0;
    public int dwMaxBytesPerSec = 10000000;
    public int dwMicroSecPerFrame = 0; // (1 / frames per sec) * 1,000,000
    public int dwPaddingGranularity = 0;
    public int[] dwReserved = new int[4];
    public int dwStreams = 1;
    public int dwSuggestedBufferSize = 0;
    public int dwTotalFrames = 0; // replace with correct value
    public int dwWidth = 0; // replace with correct value
    /*
     *
     * FOURCC fcc; DWORD cb; DWORD dwMicroSecPerFrame; DWORD
     * dwMaxBytesPerSec; DWORD dwPaddingGranularity; DWORD dwFlags; DWORD
     * dwTotalFrames; DWORD dwInitialFrames; DWORD dwStreams; DWORD
     * dwSuggestedBufferSize; DWORD dwWidth; DWORD dwHeight; DWORD
     * dwReserved[4];
     */
    public byte[] fcc = new byte[] {
        'a', 'v', 'i', 'h'
    };

    public AVIMainHeader() {
      dwMicroSecPerFrame = (int)((1.0 / MJPEGGenerator.framerate) * 1000000.0);
      dwWidth = width;
      dwHeight = height;
      dwTotalFrames = MJPEGGenerator.numFrames;
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(cb)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwMicroSecPerFrame)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwMaxBytesPerSec)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwPaddingGranularity)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwFlags)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwTotalFrames)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwInitialFrames)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwStreams)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwSuggestedBufferSize)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwWidth)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwHeight)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwReserved[0])));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwReserved[1])));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwReserved[2])));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwReserved[3])));
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIMovieList {

    public byte[] fcc = new byte[] {
        'L', 'I', 'S', 'T'
    };
    public byte[] fcc2 = new byte[] {
        'm', 'o', 'v', 'i'
    };
    public int listSize = 0;

    // 00db size jpg image data ...
    public AVIMovieList() {
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(listSize)));
      baos.write(fcc2);
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIStreamFormat {

    public short biBitCount = 24;
    public int biClrImportant = 0;
    public int biClrUsed = 0;
    public byte[] biCompression = new byte[] {
        'M', 'J', 'P', 'G'
    };
    public int biHeight = 0;
    public short biPlanes = 1;
    public int biSize = 40; // same as cb
    public int biSizeImage = 0; // width x height in pixels
    public int biWidth = 0;
    public int biXPelsPerMeter = 0;
    public int biYPelsPerMeter = 0;
    public int cb = 40;
    /*
     * FOURCC fcc; DWORD cb; DWORD biSize; LONG biWidth; LONG biHeight; WORD
     * biPlanes; WORD biBitCount; DWORD biCompression; DWORD biSizeImage;
     * LONG biXPelsPerMeter; LONG biYPelsPerMeter; DWORD biClrUsed; DWORD
     * biClrImportant;
     */
    public byte[] fcc = new byte[] {
        's', 't', 'r', 'f'
    };

    public AVIStreamFormat() {
      biWidth = width;
      biHeight = height;
      biSizeImage = width * height;
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(cb)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biSize)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biWidth)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biHeight)));
      baos.write(MJPEGGenerator.shortBytes(MJPEGGenerator.swapShort(biPlanes)));
      baos.write(MJPEGGenerator.shortBytes(MJPEGGenerator.swapShort(biBitCount)));
      baos.write(biCompression);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biSizeImage)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biXPelsPerMeter)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biYPelsPerMeter)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biClrUsed)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(biClrImportant)));
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIStreamHeader {

    public int bottom = 0;
    public int cb = 64;
    public int dwFlags = 0;
    public int dwInitialFrames = 0;
    public int dwLength = 0; // num frames
    public int dwQuality = -1;
    public int dwRate = 1000000; // dwRate / dwScale = frame rate
    public int dwSampleSize = 0;
    public int dwScale = 0; // microseconds per frame
    public int dwStart = 0;
    public int dwSuggestedBufferSize = 0;
    /*
     * FOURCC fcc; DWORD cb; FOURCC fccType; FOURCC fccHandler; DWORD
     * dwFlags; WORD wPriority; WORD wLanguage; DWORD dwInitialFrames; DWORD
     * dwScale; DWORD dwRate; DWORD dwStart; DWORD dwLength; DWORD
     * dwSuggestedBufferSize; DWORD dwQuality; DWORD dwSampleSize; struct {
     * short int left; short int top; short int right; short int bottom; }
     * rcFrame;
     */
    public byte[] fcc = new byte[] {
        's', 't', 'r', 'h'
    };
    public byte[] fccHandler = new byte[] {
        'M', 'J', 'P', 'G'
    };
    public byte[] fccType = new byte[] {
        'v', 'i', 'd', 's'
    };
    public int left = 0;
    public int right = 0;
    public int top = 0;
    public short wLanguage = 0;
    public short wPriority = 0;

    public AVIStreamHeader() {
      dwScale = (int)((1.0 / MJPEGGenerator.framerate) * 1000000.0);
      dwLength = MJPEGGenerator.numFrames;
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(cb)));
      baos.write(fccType);
      baos.write(fccHandler);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwFlags)));
      baos.write(MJPEGGenerator.shortBytes(MJPEGGenerator.swapShort(wPriority)));
      baos.write(MJPEGGenerator.shortBytes(MJPEGGenerator.swapShort(wLanguage)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwInitialFrames)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwScale)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwRate)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwStart)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwLength)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwSuggestedBufferSize)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwQuality)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(dwSampleSize)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(left)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(top)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(right)));
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(bottom)));
      baos.close();

      return baos.toByteArray();
    }
  }

  private class AVIStreamList {

    public byte[] fcc = new byte[] {
        'L', 'I', 'S', 'T'
    };
    public byte[] fcc2 = new byte[] {
        's', 't', 'r', 'l'
    };
    public int size = 124;

    public AVIStreamList() {
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(size)));
      baos.write(fcc2);
      baos.close();

      return baos.toByteArray();
    }
  }

  private class RIFFHeader {

    public byte[] fcc = new byte[] {
        'R', 'I', 'F', 'F'
    };
    public byte[] fcc2 = new byte[] {
        'A', 'V', 'I', ' '
    };
    public byte[] fcc3 = new byte[] {
        'L', 'I', 'S', 'T'
    };
    public byte[] fcc4 = new byte[] {
        'h', 'd', 'r', 'l'
    };
    public int fileSize = 0;
    public int listSize = 200;

    public RIFFHeader() {
    }

    public byte[] toBytes() throws Exception {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      baos.write(fcc);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(fileSize)));
      baos.write(fcc2);
      baos.write(fcc3);
      baos.write(MJPEGGenerator.intBytes(MJPEGGenerator.swapInt(listSize)));
      baos.write(fcc4);
      baos.close();
      return baos.toByteArray();
    }
  }
}
