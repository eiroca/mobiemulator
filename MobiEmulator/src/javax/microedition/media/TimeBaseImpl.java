/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package javax.microedition.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class TimeBaseImpl implements TimeBase {
    long currtime = System.currentTimeMillis();
    public long getTime() {
        return (System.currentTimeMillis() - currtime) * 1000;
    }
}
