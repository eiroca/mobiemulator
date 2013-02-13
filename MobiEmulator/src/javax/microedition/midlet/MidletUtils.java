package javax.microedition.midlet;

/**
 * Created with IntelliJ IDEA.
 * User: Ashok
 * Date: 7/14/12
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class MidletUtils {

    private static MidletListener midletListener;
    private static MidletUtils utilsInstance;

    public MidletUtils(){
        utilsInstance=this;
    }

    public MidletUtils(MidletListener listener){
        midletListener=listener;
        utilsInstance=this;
    }



    public static MidletUtils getInstance(){
        if(utilsInstance==null){
            return new MidletUtils();
        }
        return utilsInstance;
    }

    public void setMidletListener(MidletListener listener){
        midletListener=listener;
    }

    public MidletListener getMidletListener(){
        return midletListener;
    }




}
