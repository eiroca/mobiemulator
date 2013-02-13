/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
public interface Player extends Controllable {
    public static final int  CLOSED       = 0;
    public static final int  PREFETCHED   = 300;
    public static final int  REALIZED     = 200;
    public static final int  STARTED      = 400;
    public static final long TIME_UNKNOWN = -1;
    public static final int  UNREALIZED   = 100;
    abstract public void realize();
    abstract public void prefetch();
    abstract public void start();
    abstract public void stop();
    abstract public void deallocate();
    abstract public void close();
    abstract public int getState();
    abstract public long getDuration();
    abstract public String getContentType();
    abstract public long setMediaTime(long Time);
    abstract public long getMediaTime();
    abstract public void setLoopCount(int Count);
    abstract public void addPlayerListener(PlayerListener playerListener) throws IllegalStateException;
    abstract public void removePlayerListener(PlayerListener playerListener) throws IllegalStateException;
    abstract public void setTimeBase(TimeBase master) throws MediaException;
    abstract public TimeBase getTimeBase();
}
