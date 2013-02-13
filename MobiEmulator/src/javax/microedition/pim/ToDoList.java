package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;

//Referenced classes of package javax.microedition.pim:
//           PIMList, PIMException, ToDo
public interface ToDoList extends PIMList {
    public abstract ToDo createToDo();
    public abstract ToDo importToDo(ToDo todo);
    public abstract void removeToDo(ToDo todo) throws PIMException;
    public abstract Enumeration items(int i, long l, long l1) throws PIMException;
}
