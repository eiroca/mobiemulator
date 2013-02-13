package javax.microedition.lcdui;
/*
* GifDecoder.java
*
* Created on October 19, 2010, 11:26 AM
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
 */

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Class GifDecoder - Decodes a GIF file into one or more frames.
 *
 * Example:
 * GifDecoder d = new GifDecoder();
 * d.read("sample.gif");
 * int n = d.getFrameCount();
 * for (int i = 0; i < n; i++) {
 * BufferedImage frame = d.getFrame(i); // frame i
 * int t = d.getDelay(i); // display duration of frame in milliseconds
 * // do something with frame
 * }
 *
 * No copyright asserted on the source code of this class. May be used for
 * any purpose, however, refer to the Unisys LZW patent for any additional
 * restrictions. I am not responsible if your phone blows up. : )
 *
 * @author: Jitsu Wan with reference to work done by Kevin Weiner, FM Software; LZW decoder adapted from John Cristy's ImageMagick.
 * @version 1.03 November 2004
 *
 */
public class GifDecoder {
    private static final int MaxStackSize        = 4096;
//  max decoder pixel stack size

    /**
     * File read status: Error decoding file (may be partially decoded)
     */
    private static final int    STATUS_FORMAT_ERROR = 1;

    /**
     * File read status: No errors.
     */
    private static final int    STATUS_OK           = 0;

    /**
     * File read status: Unable to open source.
     */
    private static final int    STATUS_OPEN_ERROR   = 2;
    private byte[]           block               = new byte[256];    // current data block
    private int              blockSize           = 0;                // block size
    private int              delay               = 0;                // delay in milliseconds
//  last graphic control extension info
private int              dispose             = 0;
//  0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
private int              lastDispose         = 0;
    private int              loopCount           = 1;                // iterations; 0 = repeat forever
    private int              readCounter         = 0;
    private boolean          transparency        = false;            // use transparent color
    private int[]            act;                                    // active color table
    private int              bgColor;                                // background color
    private int              bgIndex;                                // background color index
    private int              frameCount;
    private Vector           frames;                                 // frames read from current file
    private int[]            gct;                                    // global color table
    private boolean          gctFlag;                                // global color table used
    private int              gctSize;                                // size of global color table
    private int              height;                                 // full image height
    private Image            image;                                  // current frame
    private InputStream      in;
    private boolean          interlace;                              // interlace flag
    private int              ix;
    private int iy;
    private int iw;
    private int ih;                         // current image rectangle
    private int              lastBgColor;                            // previous bg color
    private Image            lastImage;                              // previous frame
    private int[]            lastRect;                               // last image rect
    private int[]            lct;                                    // local color table
    private boolean          lctFlag;                                // local color table flag
    private int              lctSize;                                // local color table size
    private int              pixelAspect;                            // pixel aspect ratio
    private byte[]           pixelStack;
    private byte[]           pixels;
//  LZW decoder working arrays
private short[]          prefix;
    private int              status;
    private byte[]           suffix;
    private int              transIndex;                             // transparent color index
    private int              width;                                  // full image width

    /**
     * Gets display duration for specified frame.
     *
     * @param n int index of frame
     * @return delay in milliseconds
     */
    public int getDelay(int n) {
//
        delay = -1;
        if ((n >= 0) && (n < frameCount)) {
            delay = ((GifFrame) frames.elementAt(n)).delay;
        }
        if (delay < 100) {
            delay = 100;
        }

        return delay;
    }

    /**
     * Gets the number of frames read from file.
     * @return frame count
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Gets the first (or only) image read.
     *
     * @return BufferedImage containing first frame, or null if none.
     */
    public Image getImage() {
        return getFrame(0);
    }

    /**
     * Gets the "Netscape" iteration count, if any.
     * A count of 0 means repeat indefinitiely.
     *
     * @return iteration count if one was specified, else 1.
     */
    public int getLoopCount() {
        return loopCount;
    }

    /**
     * Creates new frame image from current data (and previous
     * frames as specified by their disposition codes).
     */
    void setPixels() {
//      expose destination image's pixels as int array
        int[] dest = new int[image.getWidth() * image.getHeight()];
        image.getRGB(dest, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
//      fill in starting image contents based on last image's dispose code
        if (lastDispose > 0) {
            if (lastDispose == 3) {
//              use image before last
                int n = frameCount - 2;
                if (n > 0) {
                    lastImage = getFrame(n - 1);
                } else {
                    lastImage = null;
                }
            }
            if (lastImage != null) {
                int[] prev = new int[lastImage.getHeight() * lastImage.getWidth()];
                lastImage.getRGB(prev, 0, lastImage.getWidth(), 0, 0, lastImage.getWidth(), lastImage.getHeight());
                System.arraycopy(prev, 0, dest, 0, width * height);
//              copy pixels
                if (lastDispose == 2) {
//                  fill last image rect area with background color
                    Graphics g = image.getGraphics();
                    if (transparency) {
                        g.setColor(0x00000000);     // assume background is transparent
                    } else {
                        g.setColor(lastBgColor);    // use given background color
                    }
                    g.fillRect(lastRect[0], lastRect[1], lastRect[2], lastRect[3]);
                }
            }
        }
//      copy each source line to the appropriate place in the destination
        int pass  = 1;
        int inc   = 8;
        int iline = 0;
        for (int i = 0;i < ih;i++) {
            int line = i;
            if (interlace) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                    case 2 :
                        iline = 4;

                        break;
                    case 3 :
                        iline = 2;
                        inc   = 4;

                        break;
                    case 4 :
                        iline = 1;
                        inc   = 2;
                    }
                }
                line  = iline;
                iline += inc;
            }
            line += iy;
            if (line < height) {
                int k    = line * width;
                int dx   = k + ix;       // start of line in dest
                int dlim = dx + iw;      // end of dest line
                if ((k + width) < dlim) {
                    dlim = k + width;    // past dest edge
                }
                int sx = i * iw;         // start of line in source
                while (dx < dlim) {
//                  map color and insert in destination
                    int index = ((int) pixels[sx++]) & 0xff;
                    int c     = act[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                }
            }
        }
        Graphics imageGraphics = image.getGraphics();
        imageGraphics.drawRGB(dest, 0, width, 0, 0, width, height, true);
    }

    /**
     * Gets the image contents of frame n.
     *
     * @return BufferedImage representation of frame, or null if n is invalid.
     */
    public Image getFrame(int n) {
        Image im = null;
        if ((n >= 0) && (n < frameCount)) {
            im = ((GifFrame) frames.elementAt(n)).image;
        }

        return im;
    }

    /**
     * Gets image size.
     *
     * @return GIF image dimensions
     */
    public int[] getFrameSize() {
        int[] dimension = new int[2];
        dimension[0] = width;
        dimension[1] = height;

        return dimension;
    }

    /**
     *
     * This method loads the GifDecoder with a GIF image byte array
     *
     * This is a modification from the original GifDecoder, which accepts a InputStream and is geared towards direct loading from the connection.
     * The Helix_Parser takes care of this in this case and therefore no streaming is required.
     *
     * CHANGES:
     *
     * Remove any DataInputStream functionality
     * Replace with byte[] reading functionality.
     *
     * Strip away unwanted methods.
     * Better understand LZW compression
     *
     * Consider legal issues.
     *
     *
     */
    public int read(InputStream raw) {
        init();
        if (raw != null) {
            in = raw;
            readHeader();
            if (!err()) {
                readContents();
                if (frameCount < 0) {
                    status = STATUS_FORMAT_ERROR;
                }
            }
        } else {
            status = STATUS_OPEN_ERROR;
        }

        return status;
    }

    /**
     * Decodes LZW image data into pixel array.
     * Adapted from John Cristy's ImageMagick.
     */
    void decodeImageData() {
        int NullCode = -1;
        int npix     = iw * ih;
        int available, clear, code_mask, code_size, end_of_information, in_code, old_code, bits, code, count, i, datum,
            data_size, first, top, bi, pi;
        if ((pixels == null) || (pixels.length < npix)) {
            pixels = new byte[npix];    // allocate new pixel array
        }
        if (prefix == null) {
            prefix = new short[MaxStackSize];
        }
        if (suffix == null) {
            suffix = new byte[MaxStackSize];
        }
        if (pixelStack == null) {
            pixelStack = new byte[MaxStackSize + 1];
        }
//      Initialize GIF data stream decoder.
        data_size          = read();
        clear              = 1 << data_size;
        end_of_information = clear + 1;
        available          = clear + 2;
        old_code           = NullCode;
        code_size          = data_size + 1;
        code_mask          = (1 << code_size) - 1;
        for (code = 0;code < clear;code++) {
            prefix[code] = 0;
            suffix[code] = (byte) code;
        }
//      Decode GIF pixel stream.
        datum = bits = count = first = top = pi = bi = 0;
        for (i = 0;i < npix;) {
            if (top == 0) {
                if (bits < code_size) {
//                  Load bytes until there are enough bits for a code.
                    if (count == 0) {
//                      Read a new data block.
                        count = readBlock();
                        if (count <= 0) {
                            break;
                        }
                        bi = 0;
                    }
                    datum += (((int) block[bi]) & 0xff) << bits;
                    bits  += 8;
                    bi++;
                    count--;

                    continue;
                }
//              Get the next code.
                code  = datum & code_mask;
                datum >>= code_size;
                bits  -= code_size;
//              Interpret the code
                if ((code > available) || (code == end_of_information)) {
                    break;
                }
                if (code == clear) {
//                  Reset decoder.
                    code_size = data_size + 1;
                    code_mask = (1 << code_size) - 1;
                    available = clear + 2;
                    old_code  = NullCode;

                    continue;
                }
                if (old_code == NullCode) {
                    pixelStack[top++] = suffix[code];
                    old_code          = code;
                    first             = code;

                    continue;
                }
                in_code = code;
                if (code == available) {
                    pixelStack[top++] = (byte) first;
                    code              = old_code;
                }
                while (code > clear) {
                    pixelStack[top++] = suffix[code];
                    code              = prefix[code];
                }
                first = ((int) suffix[code]) & 0xff;
//              Add a new string to the string table,
                if (available >= MaxStackSize) {
                    break;
                }
                pixelStack[top++] = (byte) first;
                prefix[available] = (short) old_code;
                suffix[available] = (byte) first;
                available++;
                if (((available & code_mask) == 0) && (available < MaxStackSize)) {
                    code_size++;
                    code_mask += available;
                }
                old_code = in_code;
            }
//          Pop a pixel off the pixel stack.
            top--;
            pixels[pi++] = pixelStack[top];
            i++;
        }
        for (i = pi;i < npix;i++) {
            pixels[i] = 0;    // clear missing pixels
        }
    }

    /**
     * Returns true if an error was encountered during reading/decoding
     */
    boolean err() {
        return status != STATUS_OK;
    }

    /**
     * Initializes or re-initializes reader
     */
    void init() {
        status     = STATUS_OK;
        frameCount = 0;
        frames     = new Vector();
        gct        = null;
        lct        = null;
    }

    /**
     * Reads a single byte from the input stream.
     */
    int read() {
        int curByte = 0;
        try {
            curByte = in.read();
            readCounter++;
        } catch (Exception e) {
            status = STATUS_FORMAT_ERROR;
        }

        return curByte;
    }

    /**
     * Reads next variable length block from input.
     *
     * @return number of bytes stored in "buffer"
     */
    int readBlock() {
        blockSize = read();
        int n = 0;
        if (blockSize > 0) {
            try {
                int count = 0;
                while (n < blockSize) {
                    count = in.read(block, n, blockSize - n);
                    if (count == -1) {
                        break;
                    }
                    n += count;
                }
            } catch (Exception e) {}
            if (n < blockSize) {
                status = STATUS_FORMAT_ERROR;
            }
        }

        return n;
    }

    /**
     * Reads color table as 256 RGB integer values
     *
     * @param ncolors int number of colors to read
     * @return int array containing 256 colors (packed ARGB with full alpha)
     */
    int[] readColorTable(int ncolors) {
        int    nbytes = 3 * ncolors;
        int[]  tab    = null;
        byte[] c      = new byte[nbytes];
        int    n      = 0;
        try {
            n = in.read(c);
        } catch (IOException e) {}
        if (n < nbytes) {
            status = STATUS_FORMAT_ERROR;
        } else {
            tab = new int[256];    // max size to avoid bounds checks
            int i = 0;
            int j = 0;
            while (i < ncolors) {
                int r = ((int) c[j++]) & 0xff;
                int g = ((int) c[j++]) & 0xff;
                int b = ((int) c[j++]) & 0xff;
                tab[i++] = 0xff000000 | (r << 16) | (g << 8) | b;
            }
        }

        return tab;
    }

    /**
     * Main file parser. Reads GIF content blocks.
     */
    void readContents() {
//      read GIF file content blocks
        boolean done = false;
        while (!(done || err())) {
            int code = read();
            switch (code) {
            case 0x2C :            // image separator
                readImage();

                break;
            case 0x21 :            // extension
                code = read();
                switch (code) {
                case 0xf9 :        // graphics control extension
                    readGraphicControlExt();

                    break;
                case 0xff :        // application extension
                    readBlock();
                    String app = "";
                    for (int i = 0;i < 11;i++) {
                        app += (char) block[i];
                    }
                    if (app.equals("NETSCAPE2.0")) {
                        readNetscapeExt();
                    } else {
                        skip();    // don't care
                    }

                    break;
                default :          // uninteresting extension
                    skip();
                }

                break;
            case 0x3b :            // terminator
                done = true;

                break;
            case 0x00 :            // bad byte, but keep going and see what happens
                break;
            default :
                status = STATUS_FORMAT_ERROR;
            }
        }
    }

    /**
     * Reads Graphics Control Extension values
     */
    void readGraphicControlExt() {
        read();    // block size
        int packed = read();    // packed fields
        dispose = (packed & 0x1c) >> 2;    // disposal method
        if (dispose == 0) {
            dispose = 1;    // elect to keep old image if discretionary
        }
        transparency = (packed & 1) != 0;
        delay        = readShort() * 10;    // delay in milliseconds
        transIndex   = read();              // transparent color index
        read();    // block terminator
    }

    /**
     * Reads GIF file header information.
     */
    void readHeader() {
        String id = "";
        for (int i = 0;i < 6;i++) {
            id += (char) read();
        }
        if (!id.startsWith("GIF")) {
            status = STATUS_FORMAT_ERROR;

            return;
        }
        readLSD();
        if (gctFlag &&!err()) {
            gct     = readColorTable(gctSize);
            bgColor = gct[bgIndex];
        }
    }

    /**
     * Reads next frame image
     */
    void readImage() {
        ix = readShort();    // (sub)image position & size
        iy = readShort();
        iw = readShort();
        ih = readShort();
        int packed = read();
        lctFlag   = (packed & 0x80) != 0;    // 1 - local color table flag
        interlace = (packed & 0x40) != 0;    // 2 - interlace flag
//      3 - sort flag
//      4-5 - reserved
        lctSize   = 2 << (packed & 7);       // 6-8 - local color table size
        if (lctFlag) {
            lct = readColorTable(lctSize);    // read table
            act = lct;                        // make local table active
        } else {
            act = gct;                        // make global table active
            if (bgIndex == transIndex) {
                bgColor = 0;
            }
        }
        int save = 0;
        if (transparency) {
            save            = act[transIndex];
            act[transIndex] = 0;    // set transparent color if specified
        }
        if (act == null) {
            status = STATUS_FORMAT_ERROR;    // no color table defined
        }
        if (err()) {
            return;
        }
        decodeImageData();    // decode pixel data
        skip();
        if (err()) {
            return;
        }
        frameCount++;
//      create new image to receive frame data
        image = Image.createImage(width, height);
        setPixels();                                      // transfer pixel data to image
        frames.addElement(new GifFrame(image, delay));    // add image to frame list
        if (transparency) {
            act[transIndex] = save;
        }
        resetFrame();
    }

    /**
     * Reads Logical Screen Descriptor
     */
    void readLSD() {
//      logical screen size
        width  = readShort();
        height = readShort();
//      packed fields
        int packed = read();
        gctFlag     = (packed & 0x80) != 0;    // 1 : global color table flag
//      2-4 : color resolution
//      5 : gct sort flag
        gctSize     = 2 << (packed & 7);       // 6-8 : gct size
        bgIndex     = read();                  // background color index
        pixelAspect = read();                  // pixel aspect ratio
    }

    /**
     * Reads Netscape extenstion to obtain iteration count
     */
    void readNetscapeExt() {
        do {
            readBlock();
            if (block[0] == 1) {
//              loop count sub-block
                int b1 = ((int) block[1]) & 0xff;
                int b2 = ((int) block[2]) & 0xff;
                loopCount = (b2 << 8) | b1;
            }
        } while ((blockSize > 0) &&!err());
    }

    /**
     * Reads next 16-bit value, LSB first
     */
    int readShort() {
//      read 16-bit value, LSB first
        return read() | (read() << 8);
    }

    /**
     * Resets frame state for reading next image.
     */
    void resetFrame() {
        lastDispose = dispose;
        lastRect    = new int[4];
        lastRect[0] = ix;
        lastRect[1] = iy;
        lastRect[2] = iw;
        lastRect[3] = ih;
        lastImage   = image;
        lastBgColor = bgColor;
        int     dispose      = 0;
        boolean transparency = false;
        int     delay        = 0;
        lct = null;
    }

    /**
     * Skips variable length blocks up to and including
     * next zero length block.
     */
    void skip() {
        do {
            readBlock();
        } while ((blockSize > 0) &&!err());
    }
    static class GifFrame {
        public int   delay;
        public Image image;
        public GifFrame(Image im, int del) {
            image = im;
            delay = del;
        }
    }
}
