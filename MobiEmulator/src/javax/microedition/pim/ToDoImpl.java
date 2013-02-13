package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.formats.VCalendar10Format;

//Referenced classes of package javax.microedition.pim:
//           AbstractPIMItem, ToDoListImpl, PIMFormat
public class ToDoImpl extends AbstractPIMItem implements ToDo {
    public ToDoImpl(ToDoListImpl list) {
        super(list, 3);
    }

    ToDoImpl(ToDoListImpl list, ToDo base) {
        super(list, base);
    }

    PIMFormat getEncodingFormat() {
        return new VCalendar10Format();
    }
    static boolean isValidPIMField(int field) {
        switch (field) {
        case 100 :    // 'd'
        case 101 :    // 'e'
        case 102 :    // 'f'
        case 103 :    // 'g'
        case 104 :    // 'h'
        case 105 :    // 'i'
        case 106 :    // 'j'
        case 107 :    // 'k'
        case 108 :    // 'l'
            return true;
        }

        return false;
    }
    public void addInt(int field, int attributes, int value) {
        switch (field) {
        case 100 :    // 'd'
            validateClass(value);

            break;
        case 105 :    // 'i'
            validatePriority(value);

            break;
        }
        super.addInt(field, attributes, value);
    }
    public void setInt(int field, int index, int attributes, int value) {
        switch (field) {
        case 100 :    // 'd'
            validateClass(value);

            break;
        case 105 :    // 'i'
            validatePriority(value);

            break;
        }
        super.setInt(field, index, attributes, value);
    }
    private void validateClass(int value) {
        switch (value) {
        case 200 :
        case 201 :
        case 202 :
            return;
        }

        throw new IllegalArgumentException("Invalid CLASS value: " + value);
    }
    private void validatePriority(int value) {
        if ((value < 0) || (value > 9)) {
            throw new IllegalArgumentException("Invalid PRIORITY value: " + value);
        } else {
            return;
        }
    }
    protected int getRevisionField() {
        return 106;
    }
    protected int getUIDField() {
        return 108;
    }
    protected String toDisplayableString() {
        return "ToDo[" + formatData() + "]";
    }
}
