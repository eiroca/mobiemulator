package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;

//Referenced classes of package javax.microedition.pim:
//           PIMList, PIMException, Event
public abstract interface EventList extends PIMList {
    public static final int ENDING    = 1;
    public static final int OCCURRING = 2;
    public static final int STARTING  = 0;
    public abstract Event createEvent();
    public abstract Event importEvent(Event event);
    public abstract void removeEvent(Event event) throws PIMException;
    public abstract Enumeration items(int i, long l, long l1, boolean flag) throws PIMException;
    public abstract int[] getSupportedRepeatRuleFields(int i);
}
