package javax.microedition.pim;

// ~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMList, EventImpl, KeySortUtility
class EventListImpl extends AbstractPIMList implements EventList {

  EventListImpl(final String name, final int mode) throws PIMException {
    super(2, name, mode);
  }

  @Override
  public Event createEvent() {
    return new EventImpl(this);
  }

  @Override
  public int[] getSupportedRepeatRuleFields(final int frequency) {
    switch (frequency) {
      case 16: // '\020'
        return (new int[] {
            32, 128, 64
        });
      case 17: // '\021'
        return (new int[] {
            32, 128, 64, 2
        });
      case 18: // '\022'
        return (new int[] {
            32, 128, 64, 2, 16, 1
        });
      case 19: // '\023'
        return (new int[] {
            32, 128, 64, 2, 16, 1, 8, 4
        });
    }

    throw new IllegalArgumentException("Unsupported frequency: " + frequency);
  }

  @Override
  public Event importEvent(final Event item) {
    return new EventImpl(this, item);
  }

  @Override
  public Enumeration items(final int searchType, final long startDate, final long endDate, final boolean initialEventOnly)
      throws PIMException {
    switch (searchType) {
      default:
        throw new IllegalArgumentException("Invalid search type: " + searchType);
      case 0: // '\0'
      case 1: // '\001'
      case 2: // '\002'
        break;
    }
    if (startDate > endDate) { throw new IllegalArgumentException("Start date must be earlier than end date"); }
    final Vector selectedItems = new Vector();
    final Vector itemKeys = new Vector();
    final Enumeration e = items();
    do {
      if (!e.hasMoreElements()) {
        break;
      }
      final Event event = (Event)e.nextElement();
      long eventStart = 0L;
      long eventEnd = 0L;
      if (event.countValues(106) != 0) {
        eventStart = event.getDate(106, 0);
        if (event.countValues(102) != 0) {
          eventEnd = event.getDate(102, 0);
        }
        else {
          eventEnd = eventStart;
        }
      }
      else {
        if (event.countValues(102) == 0) {
          continue;
        }
        eventEnd = event.getDate(102, 0);
        eventStart = eventEnd;
      }
      final long duration = Math.max(0L, eventEnd - eventStart);
      final RepeatRule repeatRule = event.getRepeat();
      boolean includeItem = false;
      if (repeatRule != null) {
        final long timeSlot = eventEnd - eventStart;
        final Enumeration dates = repeatRule.dates(eventStart, Math.max(startDate - duration, 0L), endDate);
        do {
          if (!dates.hasMoreElements()) {
            break;
          }
          final Date date = (Date)dates.nextElement();
          eventStart = date.getTime();
          eventEnd = eventStart + timeSlot;
          if (eventStart > endDate) {
            break;
          }
          includeItem = checkRange(searchType, startDate, endDate, eventStart, eventEnd);
        }
        while (!includeItem && !initialEventOnly);
      }
      else {
        includeItem = checkRange(searchType, startDate, endDate, eventStart, eventEnd);
      }
      if (includeItem) {
        KeySortUtility.store(itemKeys, selectedItems, eventStart, event);
      }
    }
    while (true);

    return selectedItems.elements();
  }

  private boolean checkRange(final int searchType, final long startDate, final long endDate, final long eventStart, final long eventEnd) {
    switch (searchType) {
      case 0: // '\0'
        return (eventStart >= startDate) && (eventStart <= endDate);
      case 1: // '\001'
        return (eventEnd >= startDate) && (eventEnd <= endDate);
      case 2: // '\002'
        return (eventStart <= endDate) && (eventEnd >= startDate);
    }

    return false;
  }

  @Override
  public void removeEvent(final Event item) throws PIMException {
    removeItem(item);
  }
}
