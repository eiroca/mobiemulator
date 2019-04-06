package javax.microedition.midlet;

/**
 * Created with IntelliJ IDEA. User: Ashok Date: 7/14/12 Time: 11:21 PM To change this template use
 * File | Settings | File Templates.
 */
public class MidletUtils {

  private static MidletListener midletListener;
  private static MidletUtils utilsInstance;

  public MidletUtils() {
    MidletUtils.utilsInstance = this;
  }

  public MidletUtils(final MidletListener listener) {
    MidletUtils.midletListener = listener;
    MidletUtils.utilsInstance = this;
  }

  public static MidletUtils getInstance() {
    if (MidletUtils.utilsInstance == null) { return new MidletUtils(); }
    return MidletUtils.utilsInstance;
  }

  public void setMidletListener(final MidletListener listener) {
    MidletUtils.midletListener = listener;
  }

  public MidletListener getMidletListener() {
    return MidletUtils.midletListener;
  }

}
