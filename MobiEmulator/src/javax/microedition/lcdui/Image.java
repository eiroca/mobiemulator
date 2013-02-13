/*
 * This program is free software. It comes without any warranty, to
 * the extent permitted by applicable law. You can redistribute it
 * and/or modify it under the terms of the Do What The Fuck You Want
 * To Public License, Version 2, as published by Sam Hocevar. See
 * http://sam.zoy.org/wtfpl/COPYING for more details.
 */


package javax.microedition.lcdui;

//~--- non-JDK imports --------------------------------------------------------


//~--- JDK imports ------------------------------------------------------------


import javax.imageio.ImageIO;
import javax.microedition.midlet.MidletUtils;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Image {
    static boolean gif = false;
    static final int IHDR = 1229472850;
    static final int PLTE = 1347179589;
    static final int IDAT = 1229209940;
    static final int IEND = 1229278788;
    static final int bKGD = 1649100612;
    static final int cHRM = 1665684045;
    static final int gAMA = 1732332865;
    static final int hIST = 1749635924;
    static final int pHYs = 1883789683;
    static final int sBIT = 1933723988;
    static final int tEXt = 1950701684;
    static final int tIME = 1950960965;
    static final int tRNS = 1951551059;
    static final int zTXt = 2052348020;
    static final int sRGB = 1934772034;
    static final int sPLT = 1934642260;
    static final int oFFs = 1866876531;
    static final int sCAL = 1933787468;
    static final int iCCP = 1766015824;
    static final int pCAL = 1883455820;
    static final int iTXt = 1767135348;
    static final int gIFg = 1732855399;
    static final int gIFx = 1732855416;
    private static int chunks[] = {
            1229472850, 1347179589, 1229209940, 1229278788, 1649100612, 1665684045, 1732332865, 1749635924, 1883789683,
            1933723988, 1950701684, 1950960965, 1951551059, 2052348020, 1934772034, 1934642260, 1866876531, 1933787468,
            1766015824, 1883455820, 1767135348, 1732855399, 1732855416,
    };
    public int drawCount = 0;
    GifDecoder gifDcoder = null;
    private boolean isAnimated = false;

    /**
     * Reference to an AWT Image. Most likely to be a BufferedImage instance
     */
    public java.awt.image.BufferedImage _image;

    /** Each image object has an associated Graphics object. */
    // public javax.microedition.lcdui.Graphics graphics;

    /**
     * Image mutability flag
     */
    private boolean mutable;
    private BufferedImage mutableStackImage;

    protected Image() {
    }

    public Image(BufferedImage image) {
        this._image = image;
        this.mutable = true;
        MidletUtils.getInstance().getMidletListener().incrImageCount();
        // Graphics.setGraphicsImage(_image);
        // graphics = new Graphics(_image.createGraphics());
    }

    /**
     * Constructor for creating an Image from an InputStream of image data.
     * Currently only supports PNG data, using the sixlegs.com library.
     */
    Image(InputStream is) {
        try {
            //
            // Java Advanced Imaging code
            //
            // javax.media.jai.RenderedOp ro =
            // javax.media.jai.JAI.create("stream",
            // com.sun.media.jai.codec.SeekableStream.wrapInputStream( is, false
            // ) );
            // this._image = ro.getAsBufferedImage();
            //
            // Sixlegs PNG library code
            //
            // System.out.println("is ss is null" + (is == null));
            // com.sixlegs.png.PngImage png = new com.sixlegs.png.PngImage();
            if (gif) {
                gifDcoder = new GifDecoder();
                gifDcoder.read(is);
                isAnimated = true;
            }

            try {
                this._image = createPng(is);    // )png.read(is,true);
            } catch (Exception e) {

            }

            // this._image=png.read(is,true);
            //
            // Built in Java ImageIO (only JDK 1.5 supports PNGs).
            // Unfortunately, fails on most PNGs!!!
            //
            // this._image = javax.imageio.ImageIO.read(is);
            MidletUtils.getInstance().getMidletListener().incrImageCount();
        } catch (Exception ex) {
            System.out.println("Exception thrown while loading image!:" + ex.toString());
            ex.printStackTrace();
        }
        // graphics = new Graphics(_image.createGraphics());
    }

    /**
     * Constructor for creating a new Image with a given width and height.
     *
     * @param w
     * @param h
     */
    Image(int w, int h) {
        this._image = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        this.mutable = true;
        mutableStackImage = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        MidletUtils.getInstance().getMidletListener().incrImageCount();
        // Graphics.setGraphicsImage(_image);
        // graphics = new Graphics(_image.createGraphics());
    }

    public static java.awt.image.BufferedImage createPng(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        byte b[] = null;
        try {
            /*
             * if (emptyBytes == 8) { baos.write(137); baos.write(80);
             * baos.write(78); baos.write(71); baos.write(13); baos.write(10);
             * baos.write(26); baos.write(10); }
             */
            while (in.available() > 0) {
                int size = in.read(buffer);
                baos.write(buffer, 0, size);
            }
            b = baos.toByteArray();

            baos.close();

            baos = null;
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }
        in = null;

        return createPNG(b);
    }

    public static Image createImage(int w, int h) {
        return new Image(w, h);
    }

    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
        boolean WidthHeightReversed = false;
        // Multiplication of 90 degress with Odd Numbers makes Width = height
        // and vice versa
        if ((transform != 4) && (transform != 5) && (transform != 6) && (transform != 7)) {
            WidthHeightReversed = true;
        }
        Image midpimage = createImage(WidthHeightReversed ? width
                : height, WidthHeightReversed ? height
                : width);
        midpimage.getGraphics().drawRegion(image, x, y, width, height, transform, 0, 0, 20);

        return midpimage;
    }

    public static Image createImage(byte[] data, int start, int len) {
        return new Image(new ByteArrayInputStream(data, start, len));
    }

    public static Image createImage(String name) throws IOException {
        // System.out.println("image path is "+com.longsteve.jammy.Main.Classpath+" "+name);
        InputStream is = null;
        try {
            gif = name.endsWith(".gif");
            is = MidletUtils.getInstance().getMidletListener().getResourceStream(name);

            /*
             * ByteArrayOutputStream bytearrayoutputstream = new
             * ByteArrayOutputStream(); byte abyte0[] = new byte[512]; int i;
             * for(; is.available() > 0; bytearrayoutputstream.write(abyte0, 0,
             * i)) { i = is.read(abyte0); }
             *
             * is.close(); abyte0 = bytearrayoutputstream.toByteArray(); return
             * new Image(new ByteArrayInputStream(abyte0));
             */
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return new Image(is);
    }

    public static Image createImage(InputStream is) {
        return new Image(is);
    }

    public static Image createImage(Image source) {
        if (source == null) {
            throw new NullPointerException();
        }
        if (!source.isMutable()) {
            return source;
        }
        Image copy = new Image(source.getWidth(), source.getHeight());
        copy.getGraphics().drawImage(source, 0, 0, Graphics.TOP | Graphics.LEFT);
        copy.mutable = false;

        return copy;
    }

    public javax.microedition.lcdui.Graphics getGraphics() {
        if (!mutable) {
            throw new IllegalStateException();
        }

        return new Graphics(this._image.createGraphics(), this._image);
    }

    private BufferedImage getStackImage() {
        return mutableStackImage;
    }

    public int getWidth() {
        return _image.getWidth(null);
    }

    public int getHeight() {
        return _image.getHeight(null);
    }

    public boolean isMutable() {
        return mutable;
    }

    private static java.awt.image.BufferedImage createPNG(byte[] imageData) {
        int width = 0;
        int height = 0;
        int colorType = 0;
        boolean trans = false;
        boolean done = false;
        DataInputStream in = null;
        try {
            in = new DataInputStream(new ByteArrayInputStream(imageData));
            in.skip(8L);
            while (!done) {
                int chunk_size = in.readInt();
                int chunk_name = in.readInt();
                // if image is JPG pass it to ImageIO
                switch (chunk_name) {
                    case IHDR:    // IHDR chunk
                        width = in.readInt();
                        height = in.readInt();
                        in.readByte();
                        colorType = in.readByte();
                        in.skip(chunk_size - 10);

                        break;
                    case tRNS:
                        trans = true;
                        in.skip(chunk_size);

                        break;
                    case IEND:
                        done = true;
                        in.skip(chunk_size);

                        break;
                    case PLTE:
                    default:
                        in.skip(chunk_size);
                }
                in.readInt();
            }
            if (in != null) {
                in.close();
            }
            in = null;
        } catch (Exception e) {
            System.out.println("Not a Valid Png image");
            // e.printStackTrace();
            if (in != null) {
                try {
                    in.reset();
                    return ImageIO.read(in);
                } catch (IOException ex) {
                    System.out.println("Invalid Image");
                }
            }
        }
        if ((colorType == 4) || (colorType == 6)) {
            trans = true;
        }
        BufferedImage image = new BufferedImage(width, height, (trans) ? 2
                : 1);
        if ((colorType == 0) || (colorType == 4) || (colorType == 2) || (colorType == 6)) {
            image.createGraphics().drawImage(new ImageIcon(imageData).getImage(), 0, 0, null);
        } else {
            try {
                image.createGraphics().drawImage(new ImageIcon(imageData).getImage(), 0, 0, null);
            } catch (Exception e) {
                System.out.println("in creating image");
                e.printStackTrace();
            }
        }

        return (image);
    }

    private static boolean isChunk(int chunkname) {
        for (int chunk : chunks) {
            if (chunk == chunkname) {
                return true;
            }
        }

        return false;
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        BufferedImage image = new BufferedImage(width, height, (processAlpha) ? 2
                : 1);
//      MemoryImageSource mis = new MemoryImageSource(width, height, rgb,
//                      0, width);
//      java.awt.Image img = Toolkit.getDefaultToolkit().createImage(mis);
//        mis=null;
//      // drawRegion(img, 0, 0, width , height, aat,0,0,0);
//      image.createGraphics().drawImage(img, 0, 0 ,null);
//        img=null;
        // WritableRaster raster = (WritableRaster) image.getData();
        // raster.setPixels(0,0,width,height,rgb);
        // image.getAlphaRaster().setDataElements(0, 0, width, height, rgb);
//        
        int[] rgbDataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int i = 0;
        for (int j = 0; j < height; j++) {
            System.arraycopy(rgb, i, rgbDataBuffer, i, width);
            if (!processAlpha) {
                for (int k = 0; k < width; k++) {
                    // to make full transparent pixel &= 0x00ffffff or pixel
                    // |=0xff000000
                    // for full opaque rgbImage[i] = (0xff << 24) | (rgbImage[i]
                    // & 0x00ffffff);
                    rgbDataBuffer[(i + k)] &= 0x00FFFFFF;    // -16777215;
                }
            }
            i += image.getWidth();
        }
        rgbDataBuffer = null;
        Image rgbImage = new Image(image);
        image = null;

        return rgbImage;
    }

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        int[] data = ((DataBufferInt) _image.getRaster().getDataBuffer()).getData();
        int sourceOffset = ((x != 0) || (y != 0)) ? y * _image.getWidth() + x
                : 0;    // if
        // x=0
        // or
        // y=0
        // it
        // is
        // 0
        // else
        // starting
        // from
        // somewhere
        // in
        // image
        for (int i = 0; i < height; i++, offset += scanlength) {
            System.arraycopy(data, sourceOffset, rgbData, offset, width);
            // we have to process alpha again as we know image.getRGB will give
            // ARGB but as MIDP2.0 specs returned values are not guaranteed ARGB
            if (_image.getType() == BufferedImage.TYPE_INT_RGB)    // if it is RGB
            // we must
            // process Alpha
            {
                for (int j = 0; j < width; j++) {
                    rgbData[(offset + j)] &= 0x00FFFFFF;
                }
            }
            sourceOffset += _image.getWidth();
        }
        MidletUtils.getInstance().getMidletListener().incrRgbCount();

        data = null;
    }

    public boolean isScalable() {
        return false;
    }

    public boolean isAnimated() {
        return isAnimated;
    }
}
