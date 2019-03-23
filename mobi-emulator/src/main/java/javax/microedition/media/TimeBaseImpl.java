package javax.microedition.media;

/**
 *
 * @author Ashok Kumar Gujarathi
 */
class TimeBaseImpl implements TimeBase {

  long currtime = System.currentTimeMillis();

  @Override
  public long getTime() {
    return (System.currentTimeMillis() - currtime) * 1000;
  }

}
