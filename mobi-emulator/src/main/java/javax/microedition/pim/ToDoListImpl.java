package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.Vector;
// AbstractPIMList, ToDoImpl, KeySortUtility

class ToDoListImpl extends AbstractPIMList implements ToDoList {

  ToDoListImpl(final String name, final int mode) throws PIMException {
    super(3, name, mode);
  }

  @Override
  public ToDo createToDo() {
    return new ToDoImpl(this);
  }

  @Override
  public ToDo importToDo(final ToDo item) {
    return new ToDoImpl(this, item);
  }

  @Override
  public Enumeration items(final int field, final long startDate, final long endDate) throws PIMException {
    if (getFieldDataType(field) != 2) { throw new IllegalArgumentException("Not a DATE field"); }
    if (endDate < startDate) { throw new IllegalArgumentException("Start date must precede end date"); }
    final Vector results = new Vector();
    final Vector keys = new Vector();
    final Enumeration e = items();
    while (e.hasMoreElements()) {
      final ToDo item = (ToDo)e.nextElement();
      final int indices = item.countValues(field);
      for (int j = 0;; j++) {
        if (j >= indices) {
          continue;
        }
        final long date = item.getDate(field, j);
        if ((date >= startDate) && (date <= endDate)) {
          KeySortUtility.store(keys, results, date, item);

          continue;
        }
      }
    }

    return results.elements();
  }

  @Override
  public void removeToDo(final ToDo item) throws PIMException {
    removeItem(item);
  }
}
