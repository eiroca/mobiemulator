/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package Emulator;

//~--- JDK imports ------------------------------------------------------------

import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class Capture {
    public int gifRecordHeight = 0;
    public int gifRecordWidth = 0;
    public int gifRecordX = 0;
    public int gifRecordY = 0;
    private String             aviFilePath        = "./Captured/Avi";
    MJPEGGenerator             aviRecorder        = null;
    private int                captureCounter     = 1;
    private String             captureFilePath    = "./Captured/Screenshots";
    private String             captureGifFilePath = "./Captured/Gif";
    public boolean             captureArea        = true;
    BufferedOutputStream       fOut               = null;
    public BufferedImage       gifImage           = null;
    private final Object             gifSync            = new Object();
    private MobiEmulator emulatorInstance;
    private AnimatedGifEncoder gifencoder;
    Capture(MobiEmulator MainInstance) {
        emulatorInstance = MainInstance;
    }

    public void takePicture(int x, int y, int framewidth, int frameheight, boolean toClipBoard) {
        if ((framewidth == 0) || (frameheight == 0)) {
            return;
        }
        BufferedImage captureImage = new BufferedImage(framewidth, frameheight, BufferedImage.TYPE_INT_RGB);
        if (!new File(captureFilePath).exists()) {
            new File(captureFilePath).mkdirs();
        }
        String captureGameFilePath = captureFilePath + "/" + emulatorInstance.mainclass;
        if (!new File(captureGameFilePath).exists()) {
            new File(captureGameFilePath).mkdirs();
        }
         if (emulatorInstance.canvasPanel.defaultBackBuffer) {
            captureImage.createGraphics().drawImage(emulatorInstance.canvasPanel.midp_screen_backscreen, 0, 0,
                    framewidth, frameheight, x, y, x + framewidth, y + frameheight, null);
        } else {
            captureImage.createGraphics().drawImage(emulatorInstance.canvasPanel.midp_screen_image, 0, 0, framewidth,
                    frameheight, x, y, x + framewidth, y + frameheight, null);
        }
        try {
            DateFormat     dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
            java.util.Date date       = new java.util.Date();
            File           fileName   = new File(captureGameFilePath + "/" + emulatorInstance.mainclass + "_"
                                            + dateFormat.format(date) + ".png");
            if (fileName.exists()) {
                fileName = new File(captureGameFilePath + "/" + emulatorInstance.mainclass + "_"
                                    + dateFormat.format(date) + "_" + captureCounter + ".png");
                captureCounter++;
            }
            if (toClipBoard) {
                ImageTransferable imageSelection = new ImageTransferable(captureImage);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageSelection, null);

                return;
            } else {
                ImageIO.write(captureImage, "png", fileName);
            }
        } catch (IOException ex) {
            System.out.println("Exception while Capturing Screenshot.");
        }
    }

    /* Capturing GIF encoder */
    public void captureGIFArea() {
        try {
            synchronized (gifSync) {
                if (emulatorInstance.canvasPanel.defaultBackBuffer) {
                    // gifImage.createGraphics().drawImage(midp_screen_backscreen,
                    // 0, 0, null);
                    gifencoder.addFrame(emulatorInstance.canvasPanel.midp_screen_backscreen.getSubimage(gifRecordX,
                            gifRecordY, gifRecordWidth - 1, gifRecordHeight - 1));
                } else {
                    gifencoder.addFrame(emulatorInstance.canvasPanel.midp_screen_image.getSubimage(gifRecordX,
                            gifRecordY, gifRecordWidth, gifRecordHeight));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void captureAVIArea() {
        if (aviRecorder != null) {
            try {
                aviRecorder.addImage(emulatorInstance.canvasPanel.defaultBackBuffer
                                     ? emulatorInstance.canvasPanel.midp_screen_backscreen.getSubimage(gifRecordX,
                        gifRecordY, gifRecordWidth, gifRecordHeight)
                                     : emulatorInstance.canvasPanel.midp_screen_image.getSubimage(gifRecordX,
                        gifRecordY, gifRecordWidth, gifRecordHeight));
            } catch (Exception e) {}
        }
    }
    public void startGIF() {
        if (gifencoder == null) {
            DateFormat     dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
            java.util.Date date       = new java.util.Date();
            if (!new File(captureGifFilePath).exists()) {
                new File(captureGifFilePath).mkdir();
            }
            String captureGameFilePath = captureGifFilePath + "/" + emulatorInstance.mainclass;
            if (!new File(captureGameFilePath).exists()) {
                new File(captureGameFilePath).mkdir();
            }
            gifencoder = new AnimatedGifEncoder();
            gifencoder.start(captureGameFilePath + "/" + emulatorInstance.mainclass + "_" + dateFormat.format(date)
                             + ".gif");
            gifencoder.setRepeat(0);
            gifencoder.setDelay(1000 / emulatorInstance.fpsValue);
            gifencoder.setQuality(3);
            // gifImage = new
            // BufferedImage(emulatorInstance.canvasPanel.getCanvasWidth(),
            // emulatorInstance.canvasPanel.getCanvasHeight(),
            // BufferedImage.TYPE_INT_RGB);
        }
        if (gifRecordWidth == 0) {
            gifRecordX = 0;
            gifRecordWidth = emulatorInstance.canvasPanel.getCanvasWidth();
        }
        if (gifRecordHeight == 0) {
            gifRecordY = 0;
            gifRecordHeight = emulatorInstance.canvasPanel.getCanvasHeight();
        }
    }
    public void stopGIF() {
        if (gifencoder != null) {
            gifencoder.finish();
        }
        gifencoder = null;
    }
    public void finishAVI() {
        if (aviRecorder != null) {
            try {
                aviRecorder.finishAVI();
            } catch (Exception ex) {}
        }
        aviRecorder = null;
    }
    public void startAVI() {
        if (!new File(aviFilePath).exists()) {
            new File(aviFilePath).mkdir();
        }
        if (gifRecordWidth == 0) {
            gifRecordX = 0;
            gifRecordWidth = emulatorInstance.canvasPanel.getCanvasWidth();
        }
        if (gifRecordHeight == 0) {
            gifRecordY = 0;
            gifRecordHeight = emulatorInstance.canvasPanel.getCanvasHeight();
        }
        aviRecorder = new MJPEGGenerator(new File(aviFilePath + "/" + "1.avi"), gifRecordWidth, gifRecordHeight);
    }
    void setCaptureRect(int x, int y, int width, int height) {
        gifRecordX = x;
        gifRecordY = y;
        gifRecordWidth = width;
        gifRecordHeight = height;
    }
}
