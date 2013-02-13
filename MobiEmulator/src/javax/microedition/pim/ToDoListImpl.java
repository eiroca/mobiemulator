package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.Vector;
//AbstractPIMList, ToDoImpl, KeySortUtility

class ToDoListImpl extends AbstractPIMList implements ToDoList {
    ToDoListImpl(String name, int mode) throws PIMException {
        super(3, name, mode);
    }

    public ToDo createToDo() {
        return new ToDoImpl(this);
    }
    public ToDo importToDo(ToDo item) {
        return new ToDoImpl(this, item);
    }
    public Enumeration items(int field, long startDate, long endDate) throws PIMException {
        if (getFieldDataType(field) != 2) {
            throw new IllegalArgumentException("Not a DATE field");
        }
        if (endDate < startDate) {
            throw new IllegalArgumentException("Start date must precede end date");
        }
        Vector      results = new Vector();
        Vector      keys    = new Vector();
        Enumeration e       = items();
        while (e.hasMoreElements()) {
            ToDo item    = (ToDo) e.nextElement();
            int  indices = item.countValues(field);
            for (int j = 0;;j++) {
                if (j >= indices) {
                    continue;
                }
                long date = item.getDate(field, j);
                if ((date >= startDate) && (date <= endDate)) {
                    KeySortUtility.store(keys, results, date, item);

                    continue;
                }
            }
        }

        return results.elements();
    }
    public void removeToDo(ToDo item) throws PIMException {
        removeItem(item);
    }
}
