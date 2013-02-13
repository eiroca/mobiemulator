/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.lcdui;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public class AnimatedImage extends Image {
    public int getFrameCount() {
        if (gifDcoder != null) {
            return gifDcoder.getFrameCount();
        }

        return 0;
    }
    public Image getFrame(int index) {
        if (gifDcoder != null) {
            return gifDcoder.getFrame(index);
        }

        return null;
    }
    public int getFrameDelay(int index) {
        if (gifDcoder != null) {
            return gifDcoder.getDelay(index);
        }

        return 0;
    }
    public int getLoopCount() {
        if (gifDcoder != null) {
            return gifDcoder.getLoopCount();
        }

        return 0;
    }
}
