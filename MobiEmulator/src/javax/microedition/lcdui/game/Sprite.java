// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 3/18/2011 12:42:56 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Sprite.java



package javax.microedition.lcdui.game;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

//Referenced classes of package javax.microedition.lcdui.game:
//           Layer, TiledLayer
public class Sprite extends Layer {
    public static final int TRANS_MIRROR        = 2;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public static final int TRANS_MIRROR_ROT90  = 7;
    public static final int TRANS_NONE          = 0;
    public static final int TRANS_ROT180        = 3;
    public static final int TRANS_ROT270        = 6;
    public static final int TRANS_ROT90         = 5;
    int                     collisionRectHeight;
    int                     collisionRectWidth;
    int                     collisionRectX;
    int                     collisionRectY;
    private boolean         customSequenceDefined;
    int                     dRefX;
    int                     dRefY;
    int                     frameCoordsX[];
    int                     frameCoordsY[];
    int                     frameSequence[];
    int                     numberFrames;
    private int             sequenceIndex;
    Image                   sourceImage;
    int                     srcFrameHeight;
    int                     srcFrameWidth;
    int                     t_collisionRectHeight;
    int                     t_collisionRectWidth;
    int                     t_collisionRectX;
    int                     t_collisionRectY;
    int                     t_currentTransformation;
    public Sprite(Image image) {
        super(image.getWidth(), image.getHeight());
        initializeFrames(image, image.getWidth(), image.getHeight(), false);
        initCollisionRectBounds();
        setTransformImpl(0);
    }

    public Sprite(Sprite s) {
        super((s == null) ? 0
                          : s.getWidth(), (s == null) ? 0
                : s.getHeight());
        if (s == null) {
            throw new NullPointerException();
        } else {
            sourceImage  = Image.createImage(s.sourceImage);
            numberFrames = s.numberFrames;
            frameCoordsX = new int[numberFrames];
            frameCoordsY = new int[numberFrames];
            System.arraycopy(s.frameCoordsX, 0, frameCoordsX, 0, s.getRawFrameCount());
            System.arraycopy(s.frameCoordsY, 0, frameCoordsY, 0, s.getRawFrameCount());
            x                   = s.getX();
            y                   = s.getY();
            dRefX               = s.dRefX;
            dRefY               = s.dRefY;
            collisionRectX      = s.collisionRectX;
            collisionRectY      = s.collisionRectY;
            collisionRectWidth  = s.collisionRectWidth;
            collisionRectHeight = s.collisionRectHeight;
            srcFrameWidth       = s.srcFrameWidth;
            srcFrameHeight      = s.srcFrameHeight;
            setTransformImpl(s.t_currentTransformation);
            setVisible(s.isVisible());
            frameSequence = new int[s.getFrameSequenceLength()];
            setFrameSequence(s.frameSequence);
            setFrame(s.getFrame());
            setRefPixelPosition(s.getRefPixelX(), s.getRefPixelY());

            return;
        }
    }

    public Sprite(Image image, int frameWidth, int frameHeight) {
        super(frameWidth, frameHeight);
        if ((frameWidth < 1) || (frameHeight < 1) || (image.getWidth() % frameWidth != 0)
                || (image.getHeight() % frameHeight != 0)) {
            throw new IllegalArgumentException();
        } else {
            initializeFrames(image, frameWidth, frameHeight, false);
            initCollisionRectBounds();
            setTransformImpl(0);

            return;
        }
    }

    public void defineReferencePixel(int x, int y) {
        dRefX = x;
        dRefY = y;
    }
    public void setRefPixelPosition(int x, int y) {
        this.x = x - getTransformedPtX(dRefX, dRefY, t_currentTransformation);
        this.y = y - getTransformedPtY(dRefX, dRefY, t_currentTransformation);
    }
    public int getRefPixelX() {
        return x + getTransformedPtX(dRefX, dRefY, t_currentTransformation);
    }
    public int getRefPixelY() {
        return y + getTransformedPtY(dRefX, dRefY, t_currentTransformation);
    }
    public void setFrame(int sequenceIndex) {
        if ((sequenceIndex < 0) || (sequenceIndex >= frameSequence.length)) {
            throw new IndexOutOfBoundsException();
        } else {
            this.sequenceIndex = sequenceIndex;

            return;
        }
    }
    public final int getFrame() {
        return sequenceIndex;
    }
    public int getRawFrameCount() {
        return numberFrames;
    }
    public int getFrameSequenceLength() {
        return frameSequence.length;
    }
    public void nextFrame() {
        sequenceIndex = (sequenceIndex + 1) % frameSequence.length;
    }
    public void prevFrame() {
        if (sequenceIndex == 0) {
            sequenceIndex = frameSequence.length - 1;
        } else {
            sequenceIndex--;
        }
    }
    @Override
    public final void paint(Graphics g) {
        if (g == null) {
            throw new NullPointerException();
        }
        if (visible) {
            g.drawRegion(sourceImage, frameCoordsX[frameSequence[sequenceIndex]],
                         frameCoordsY[frameSequence[sequenceIndex]], srcFrameWidth, srcFrameHeight,
                         t_currentTransformation, x, y, 20);
        }
    }
    public void setFrameSequence(int sequence[]) {
        if (sequence == null) {
            sequenceIndex         = 0;
            customSequenceDefined = false;
            frameSequence         = new int[numberFrames];
            for (int i = 0;i < numberFrames;i++) {
                frameSequence[i] = i;
            }

            return;
        }
        if (sequence.length < 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0;i < sequence.length;i++) {
            if ((sequence[i] < 0) || (sequence[i] >= numberFrames)) {
                throw new ArrayIndexOutOfBoundsException();
            }
        }
        customSequenceDefined = true;
        frameSequence         = new int[sequence.length];
        System.arraycopy(sequence, 0, frameSequence, 0, sequence.length);
        sequenceIndex = 0;
    }
    public void setImage(Image img, int frameWidth, int frameHeight) {
        if ((frameWidth < 1) || (frameHeight < 1) || (img.getWidth() % frameWidth != 0)
                || (img.getHeight() % frameHeight != 0)) {
            throw new IllegalArgumentException();
        }
        int     noOfFrames       = (img.getWidth() / frameWidth) * (img.getHeight() / frameHeight);
        boolean maintainCurFrame = true;
        if (noOfFrames < numberFrames) {
            maintainCurFrame      = false;
            customSequenceDefined = false;
        }
        if ((srcFrameWidth != frameWidth) || (srcFrameHeight != frameHeight)) {
            int oldX = x + getTransformedPtX(dRefX, dRefY, t_currentTransformation);
            int oldY = y + getTransformedPtY(dRefX, dRefY, t_currentTransformation);
            setWidthImpl(frameWidth);
            setHeightImpl(frameHeight);
            initializeFrames(img, frameWidth, frameHeight, maintainCurFrame);
            initCollisionRectBounds();
            x = oldX - getTransformedPtX(dRefX, dRefY, t_currentTransformation);
            y = oldY - getTransformedPtY(dRefX, dRefY, t_currentTransformation);
            computeTransformedBounds(t_currentTransformation);
        } else {
            initializeFrames(img, frameWidth, frameHeight, maintainCurFrame);
        }
    }
    public void defineCollisionRectangle(int x, int y, int width, int height) {
        if ((width < 0) || (height < 0)) {
            throw new IllegalArgumentException();
        } else {
            collisionRectX      = x;
            collisionRectY      = y;
            collisionRectWidth  = width;
            collisionRectHeight = height;
            setTransformImpl(t_currentTransformation);

            return;
        }
    }
    public void setTransform(int transform) {
        setTransformImpl(transform);
    }
    public final boolean collidesWith(Sprite s, boolean pixelLevel) {
        if (!s.visible ||!visible) {
            return false;
        }
        int otherLeft   = s.x + s.t_collisionRectX;
        int otherTop    = s.y + s.t_collisionRectY;
        int otherRight  = otherLeft + s.t_collisionRectWidth;
        int otherBottom = otherTop + s.t_collisionRectHeight;
        int left        = x + t_collisionRectX;
        int top         = y + t_collisionRectY;
        int right       = left + t_collisionRectWidth;
        int bottom      = top + t_collisionRectHeight;
        if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) {
            if (pixelLevel) {
                if (t_collisionRectX < 0) {
                    left = x;
                }
                if (t_collisionRectY < 0) {
                    top = y;
                }
                if (t_collisionRectX + t_collisionRectWidth > width) {
                    right = x + width;
                }
                if (t_collisionRectY + t_collisionRectHeight > height) {
                    bottom = y + height;
                }
                if (s.t_collisionRectX < 0) {
                    otherLeft = s.x;
                }
                if (s.t_collisionRectY < 0) {
                    otherTop = s.y;
                }
                if (s.t_collisionRectX + s.t_collisionRectWidth > s.width) {
                    otherRight = s.x + s.width;
                }
                if (s.t_collisionRectY + s.t_collisionRectHeight > s.height) {
                    otherBottom = s.y + s.height;
                }
                if (!intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) {
                    return false;
                } else {
                    int intersectLeft    = (left >= otherLeft) ? left
                            : otherLeft;
                    int intersectTop     = (top >= otherTop) ? top
                            : otherTop;
                    int intersectRight   = (right >= otherRight) ? otherRight
                            : right;
                    int intersectBottom  = (bottom >= otherBottom) ? otherBottom
                            : bottom;
                    int intersectWidth   = Math.abs(intersectRight - intersectLeft);
                    int intersectHeight  = Math.abs(intersectBottom - intersectTop);
                    int thisImageXOffset = getImageTopLeftX(intersectLeft, intersectTop, intersectRight,
                                               intersectBottom);
                    int thisImageYOffset = getImageTopLeftY(intersectLeft, intersectTop, intersectRight,
                                               intersectBottom);
                    int otherImageXOffset = s.getImageTopLeftX(intersectLeft, intersectTop, intersectRight,
                                                intersectBottom);
                    int otherImageYOffset = s.getImageTopLeftY(intersectLeft, intersectTop, intersectRight,
                                                intersectBottom);

                    return doPixelCollision(thisImageXOffset, thisImageYOffset, otherImageXOffset, otherImageYOffset,
                                            sourceImage, t_currentTransformation, s.sourceImage,
                                            s.t_currentTransformation, intersectWidth, intersectHeight);
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    public final boolean collidesWith(TiledLayer t, boolean pixelLevel) {
        if (!t.visible ||!visible) {
            return false;
        }
        int tLx1     = t.x;
        int tLy1     = t.y;
        int tLx2     = tLx1 + t.width;
        int tLy2     = tLy1 + t.height;
        int tW       = t.getCellWidth();
        int tH       = t.getCellHeight();
        int sx1      = x + t_collisionRectX;
        int sy1      = y + t_collisionRectY;
        int sx2      = sx1 + t_collisionRectWidth;
        int sy2      = sy1 + t_collisionRectHeight;
        int tNumCols = t.getColumns();
        int tNumRows = t.getRows();
        if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) {
            return false;
        }
        int startCol = (sx1 > tLx1) ? (sx1 - tLx1) / tW
                                    : 0;
        int startRow = (sy1 > tLy1) ? (sy1 - tLy1) / tH
                                    : 0;
        int endCol   = (sx2 >= tLx2) ? tNumCols - 1
                                     : (sx2 - 1 - tLx1) / tW;
        int endRow   = (sy2 >= tLy2) ? tNumRows - 1
                                     : (sy2 - 1 - tLy1) / tH;
        if (!pixelLevel) {
            for (int row = startRow;row <= endRow;row++) {
                for (int col = startCol;col <= endCol;col++) {
                    if (t.getCell(col, row) != 0) {
                        return true;
                    }
                }
            }

            return false;
        }
        if (t_collisionRectX < 0) {
            sx1 = x;
        }
        if (t_collisionRectY < 0) {
            sy1 = y;
        }
        if (t_collisionRectX + t_collisionRectWidth > width) {
            sx2 = x + width;
        }
        if (t_collisionRectY + t_collisionRectHeight > height) {
            sy2 = y + height;
        }
        if (!intersectRect(tLx1, tLy1, tLx2, tLy2, sx1, sy1, sx2, sy2)) {
            return false;
        }
        startCol = (sx1 > tLx1) ? (sx1 - tLx1) / tW
                                : 0;
        startRow = (sy1 > tLy1) ? (sy1 - tLy1) / tH
                                : 0;
        endCol   = (sx2 >= tLx2) ? tNumCols - 1
                                 : (sx2 - 1 - tLx1) / tW;
        endRow   = (sy2 >= tLy2) ? tNumRows - 1
                                 : (sy2 - 1 - tLy1) / tH;
        int cellTop    = startRow * tH + tLy1;
        int cellBottom = cellTop + tH;
        for (int row = startRow;row <= endRow;) {
            int cellLeft  = startCol * tW + tLx1;
            int cellRight = cellLeft + tW;
            for (int col = startCol;col <= endCol;) {
                int tileIndex = t.getCell(col, row);
                if (tileIndex != 0) {
                    int intersectLeft   = (sx1 >= cellLeft) ? sx1
                            : cellLeft;
                    int intersectTop    = (sy1 >= cellTop) ? sy1
                            : cellTop;
                    int intersectRight  = (sx2 >= cellRight) ? cellRight
                            : sx2;
                    int intersectBottom = (sy2 >= cellBottom) ? cellBottom
                            : sy2;
                    if (intersectLeft > intersectRight) {
                        int temp = intersectRight;
                        intersectRight = intersectLeft;
                        intersectLeft  = temp;
                    }
                    if (intersectTop > intersectBottom) {
                        int temp = intersectBottom;
                        intersectBottom = intersectTop;
                        intersectTop    = temp;
                    }
                    int intersectWidth  = intersectRight - intersectLeft;
                    int intersectHeight = intersectBottom - intersectTop;
                    int image1XOffset   = getImageTopLeftX(intersectLeft, intersectTop, intersectRight,
                                              intersectBottom);
                    int image1YOffset   = getImageTopLeftY(intersectLeft, intersectTop, intersectRight,
                                              intersectBottom);
                    int image2XOffset   = t.tileSetX[tileIndex] + (intersectLeft - cellLeft);
                    int image2YOffset   = t.tileSetY[tileIndex] + (intersectTop - cellTop);
                    if (doPixelCollision(image1XOffset, image1YOffset, image2XOffset, image2YOffset, sourceImage,
                                         t_currentTransformation, t.sourceImage, 0, intersectWidth, intersectHeight)) {
                        return true;
                    }
                }
                col++;
                cellLeft  += tW;
                cellRight += tW;
            }
            row++;
            cellTop    += tH;
            cellBottom += tH;
        }

        return false;
    }
    public final boolean collidesWith(Image image, int x, int y, boolean pixelLevel) {
        if (!visible) {
            return false;
        }
        int otherLeft   = x;
        int otherTop    = y;
        int otherRight  = x + image.getWidth();
        int otherBottom = y + image.getHeight();
        int left        = this.x + t_collisionRectX;
        int top         = this.y + t_collisionRectY;
        int right       = left + t_collisionRectWidth;
        int bottom      = top + t_collisionRectHeight;
        if (intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) {
            if (pixelLevel) {
                if (t_collisionRectX < 0) {
                    left = this.x;
                }
                if (t_collisionRectY < 0) {
                    top = this.y;
                }
                if (t_collisionRectX + t_collisionRectWidth > width) {
                    right = this.x + width;
                }
                if (t_collisionRectY + t_collisionRectHeight > height) {
                    bottom = this.y + height;
                }
                if (!intersectRect(otherLeft, otherTop, otherRight, otherBottom, left, top, right, bottom)) {
                    return false;
                } else {
                    int intersectLeft    = (left >= otherLeft) ? left
                            : otherLeft;
                    int intersectTop     = (top >= otherTop) ? top
                            : otherTop;
                    int intersectRight   = (right >= otherRight) ? otherRight
                            : right;
                    int intersectBottom  = (bottom >= otherBottom) ? otherBottom
                            : bottom;
                    int intersectWidth   = Math.abs(intersectRight - intersectLeft);
                    int intersectHeight  = Math.abs(intersectBottom - intersectTop);
                    int thisImageXOffset = getImageTopLeftX(intersectLeft, intersectTop, intersectRight,
                                               intersectBottom);
                    int thisImageYOffset = getImageTopLeftY(intersectLeft, intersectTop, intersectRight,
                                               intersectBottom);
                    int otherImageXOffset = intersectLeft - x;
                    int otherImageYOffset = intersectTop - y;

                    return doPixelCollision(thisImageXOffset, thisImageYOffset, otherImageXOffset, otherImageYOffset,
                                            sourceImage, t_currentTransformation, image, 0, intersectWidth,
                                            intersectHeight);
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
    private void initializeFrames(Image image, int fWidth, int fHeight, boolean maintainCurFrame) {
        int imageW              = image.getWidth();
        int imageH              = image.getHeight();
        int numHorizontalFrames = imageW / fWidth;
        int numVerticalFrames   = imageH / fHeight;
        sourceImage    = image;
        srcFrameWidth  = fWidth;
        srcFrameHeight = fHeight;
        numberFrames   = numHorizontalFrames * numVerticalFrames;
        frameCoordsX   = new int[numberFrames];
        frameCoordsY   = new int[numberFrames];
        if (!maintainCurFrame) {
            sequenceIndex = 0;
        }
        if (!customSequenceDefined) {
            frameSequence = new int[numberFrames];
        }
        int currentFrame = 0;
        for (int yy = 0;yy < imageH;yy += fHeight) {
            for (int xx = 0;xx < imageW;xx += fWidth) {
                frameCoordsX[currentFrame] = xx;
                frameCoordsY[currentFrame] = yy;
                if (!customSequenceDefined) {
                    frameSequence[currentFrame] = currentFrame;
                }
                currentFrame++;
            }
        }
    }
    private void initCollisionRectBounds() {
        collisionRectX      = 0;
        collisionRectY      = 0;
        collisionRectWidth  = width;
        collisionRectHeight = height;
    }
    private boolean intersectRect(int r1x1, int r1y1, int r1x2, int r1y2, int r2x1, int r2y1, int r2x2, int r2y2) {
        return (r2x1 < r1x2) && (r2y1 < r1y2) && (r2x2 > r1x1) && (r2y2 > r1y1);
    }
    private static boolean doPixelCollision(int image1XOffset, int image1YOffset, int image2XOffset, int image2YOffset,
            Image image1, int transform1, Image image2, int transform2, int width, int height) {
        int numPixels   = height * width;
        int argbData1[] = new int[numPixels];
        int argbData2[] = new int[numPixels];
        int startY1;
        int xIncr1;
        int yIncr1;
        if (0 != (transform1 & 4)) {
            if (0 != (transform1 & 1)) {
                xIncr1  = -height;
                startY1 = numPixels - height;
            } else {
                xIncr1  = height;
                startY1 = 0;
            }
            if (0 != (transform1 & 2)) {
                yIncr1  = -1;
                startY1 += height - 1;
            } else {
                yIncr1 = 1;
            }
            image1.getRGB(argbData1, 0, height, image1XOffset, image1YOffset, height, width);
        } else {
            if (0 != (transform1 & 1)) {
                startY1 = numPixels - width;
                yIncr1  = -width;
            } else {
                startY1 = 0;
                yIncr1  = width;
            }
            if (0 != (transform1 & 2)) {
                xIncr1  = -1;
                startY1 += width - 1;
            } else {
                xIncr1 = 1;
            }
            image1.getRGB(argbData1, 0, width, image1XOffset, image1YOffset, width, height);
        }
        int startY2;
        int xIncr2;
        int yIncr2;
        if (0 != (transform2 & 4)) {
            if (0 != (transform2 & 1)) {
                xIncr2  = -height;
                startY2 = numPixels - height;
            } else {
                xIncr2  = height;
                startY2 = 0;
            }
            if (0 != (transform2 & 2)) {
                yIncr2  = -1;
                startY2 += height - 1;
            } else {
                yIncr2 = 1;
            }
            image2.getRGB(argbData2, 0, height, image2XOffset, image2YOffset, height, width);
        } else {
            if (0 != (transform2 & 1)) {
                startY2 = numPixels - width;
                yIncr2  = -width;
            } else {
                startY2 = 0;
                yIncr2  = width;
            }
            if (0 != (transform2 & 2)) {
                xIncr2  = -1;
                startY2 += width - 1;
            } else {
                xIncr2 = 1;
            }
            image2.getRGB(argbData2, 0, width, image2XOffset, image2YOffset, width, height);
        }
        int numIterRows  = 0;
        int xLocalBegin1 = startY1;
        int xLocalBegin2 = startY2;
        for (;numIterRows < height;numIterRows++) {
            int numIterColumns = 0;
            int x1             = xLocalBegin1;
            int x2             = xLocalBegin2;
            for (;numIterColumns < width;numIterColumns++) {
                if ((argbData1[x1] & 0xff000000) != 0 && (argbData2[x2] & 0xff000000) != 0) {
                    return true;
                }
                x1 += xIncr1;
                x2 += xIncr2;
            }
            xLocalBegin1 += yIncr1;
            xLocalBegin2 += yIncr2;
        }

        return false;
    }
    private int getImageTopLeftX(int x1, int y1, int x2, int y2) {
        int retX = 0;
        switch (t_currentTransformation) {
        case 0 :    // '\0'
        case 1 :    // '\001'
            retX = x1 - x;

            break;
        case 2 :    // '\002'
        case 3 :    // '\003'
            retX = (x + width) - x2;

            break;
        case 4 :    // '\004'
        case 5 :    // '\005'
            retX = y1 - y;

            break;
        case 6 :    // '\006'
        case 7 :    // '\007'
            retX = (y + height) - y2;

            break;
        }
        retX += frameCoordsX[frameSequence[sequenceIndex]];

        return retX;
    }
    private int getImageTopLeftY(int x1, int y1, int x2, int y2) {
        int retY = 0;
        switch (t_currentTransformation) {
        case 0 :    // '\0'
        case 2 :    // '\002'
            retY = y1 - y;

            break;
        case 1 :    // '\001'
        case 3 :    // '\003'
            retY = (y + height) - y2;

            break;
        case 4 :    // '\004'
        case 6 :    // '\006'
            retY = x1 - x;

            break;
        case 5 :    // '\005'
        case 7 :    // '\007'
            retY = (x + width) - x2;

            break;
        }
        retY += frameCoordsY[frameSequence[sequenceIndex]];

        return retY;
    }
    private void setTransformImpl(int transform) {
        x = (x + getTransformedPtX(dRefX, dRefY, t_currentTransformation)) - getTransformedPtX(dRefX, dRefY, transform);
        y = (y + getTransformedPtY(dRefX, dRefY, t_currentTransformation)) - getTransformedPtY(dRefX, dRefY, transform);
        computeTransformedBounds(transform);
        t_currentTransformation = transform;
    }
    private void computeTransformedBounds(int transform) {
        switch (transform) {
        case 0 :    // '\0'
            t_collisionRectX      = collisionRectX;
            t_collisionRectY      = collisionRectY;
            t_collisionRectWidth  = collisionRectWidth;
            t_collisionRectHeight = collisionRectHeight;
            width                 = srcFrameWidth;
            height                = srcFrameHeight;

            break;
        case 2 :    // '\002'
            t_collisionRectX      = srcFrameWidth - (collisionRectX + collisionRectWidth);
            t_collisionRectY      = collisionRectY;
            t_collisionRectWidth  = collisionRectWidth;
            t_collisionRectHeight = collisionRectHeight;
            width                 = srcFrameWidth;
            height                = srcFrameHeight;

            break;
        case 1 :    // '\001'
            t_collisionRectY      = srcFrameHeight - (collisionRectY + collisionRectHeight);
            t_collisionRectX      = collisionRectX;
            t_collisionRectWidth  = collisionRectWidth;
            t_collisionRectHeight = collisionRectHeight;
            width                 = srcFrameWidth;
            height                = srcFrameHeight;

            break;
        case 5 :    // '\005'
            t_collisionRectX      = srcFrameHeight - (collisionRectHeight + collisionRectY);
            t_collisionRectY      = collisionRectX;
            t_collisionRectHeight = collisionRectWidth;
            t_collisionRectWidth  = collisionRectHeight;
            width                 = srcFrameHeight;
            height                = srcFrameWidth;

            break;
        case 3 :    // '\003'
            t_collisionRectX      = srcFrameWidth - (collisionRectWidth + collisionRectX);
            t_collisionRectY      = srcFrameHeight - (collisionRectHeight + collisionRectY);
            t_collisionRectWidth  = collisionRectWidth;
            t_collisionRectHeight = collisionRectHeight;
            width                 = srcFrameWidth;
            height                = srcFrameHeight;

            break;
        case 6 :    // '\006'
            t_collisionRectX      = collisionRectY;
            t_collisionRectY      = srcFrameWidth - (collisionRectWidth + collisionRectX);
            t_collisionRectHeight = collisionRectWidth;
            t_collisionRectWidth  = collisionRectHeight;
            width                 = srcFrameHeight;
            height                = srcFrameWidth;

            break;
        case 7 :    // '\007'
            t_collisionRectX      = srcFrameHeight - (collisionRectHeight + collisionRectY);
            t_collisionRectY      = srcFrameWidth - (collisionRectWidth + collisionRectX);
            t_collisionRectHeight = collisionRectWidth;
            t_collisionRectWidth  = collisionRectHeight;
            width                 = srcFrameHeight;
            height                = srcFrameWidth;

            break;
        case 4 :    // '\004'
            t_collisionRectY      = collisionRectX;
            t_collisionRectX      = collisionRectY;
            t_collisionRectHeight = collisionRectWidth;
            t_collisionRectWidth  = collisionRectHeight;
            width                 = srcFrameHeight;
            height                = srcFrameWidth;

            break;
        default :
            throw new IllegalArgumentException();
        }
    }
    int getTransformedPtX(int x, int y, int transform) {
        int t_x = 0;
        switch (transform) {
        case 0 :    // '\0'
            t_x = x;

            break;
        case 2 :    // '\002'
            t_x = srcFrameWidth - x - 1;

            break;
        case 1 :    // '\001'
            t_x = x;

            break;
        case 5 :    // '\005'
            t_x = srcFrameHeight - y - 1;

            break;
        case 3 :    // '\003'
            t_x = srcFrameWidth - x - 1;

            break;
        case 6 :    // '\006'
            t_x = y;

            break;
        case 7 :    // '\007'
            t_x = srcFrameHeight - y - 1;

            break;
        case 4 :    // '\004'
            t_x = y;

            break;
        default :
            throw new IllegalArgumentException();
        }

        return t_x;
    }
    int getTransformedPtY(int x, int y, int transform) {
        int t_y = 0;
        switch (transform) {
        case 0 :    // '\0'
            t_y = y;

            break;
        case 2 :    // '\002'
            t_y = y;

            break;
        case 1 :    // '\001'
            t_y = srcFrameHeight - y - 1;

            break;
        case 5 :    // '\005'
            t_y = x;

            break;
        case 3 :    // '\003'
            t_y = srcFrameHeight - y - 1;

            break;
        case 6 :    // '\006'
            t_y = srcFrameWidth - x - 1;

            break;
        case 7 :    // '\007'
            t_y = srcFrameWidth - x - 1;

            break;
        case 4 :    // '\004'
            t_y = x;

            break;
        default :
            throw new IllegalArgumentException();
        }

        return t_y;
    }
}
