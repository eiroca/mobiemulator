package javax.microedition.pim;

import java.util.Date;
import java.util.Enumeration;
// ~--- JDK imports ------------------------------------------------------------
import javax.microedition.pim.formats.VCalendar10Format;

// Referenced classes of package javax.microedition.pim:
// AbstractPIMItem, PIMHandler, EventListImpl, PIMFormat
public class EventImpl extends AbstractPIMItem implements Event {

  private RepeatRule repeatRule;

  public EventImpl(final EventListImpl list) {
    super(list, 2);
    repeatRule = null;
  }

  EventImpl(final EventListImpl list, final Event base) {
    super(list, base);
    repeatRule = null;
    repeatRule = base.getRepeat();
  }

  @Override
  public RepeatRule getRepeat() {
    if (repeatRule == null) { return null; }
    final int fields[] = repeatRule.getFields();
    final RepeatRule newRule = new RepeatRule();
    for (final int field : fields) {
      switch (field) {
        case 0: // '\0'
        case 1: // '\001'
        case 2: // '\002'
        case 4: // '\004'
        case 8: // '\b'
        case 16: // '\020'
        case 32: // ' '
        case 128:
          newRule.setInt(field, repeatRule.getInt(field));

          break;
        case 64: // '@'
          newRule.setDate(field, repeatRule.getDate(field));

          break;
      }
    }
    Date date;
    for (final Enumeration dates = repeatRule.getExceptDates(); dates.hasMoreElements(); newRule.addExceptDate(date.getTime())) {
      date = (Date)dates.nextElement();
    }

    return newRule;
  }

  @Override
  public void setRepeat(final RepeatRule value) {
    repeatRule = value;
    setModified(true);
  }

  @Override
  PIMFormat getEncodingFormat() {
    return new VCalendar10Format();
  }

  static boolean isValidPIMField(final int field) {
    switch (field) {
      case 100: // 'd'
      case 101: // 'e'
      case 102: // 'f'
      case 103: // 'g'
      case 104: // 'h'
      case 105: // 'i'
      case 106: // 'j'
      case 107: // 'k'
      case 108: // 'l'
        return true;
    }

    return false;
  }

  @Override
  public void addInt(final int field, final int attributes, final int value) {
    if (field == 101) {
      validateClass(value);
    }
    super.addInt(field, attributes, value);
  }

  @Override
  public void setInt(final int field, final int index, final int attributes, final int value) {
    if (field == 101) {
      validateClass(value);
    }
    super.setInt(field, index, attributes, value);
  }

  private void validateClass(final int value) {
    switch (value) {
      case 200:
      case 201:
      case 202:
        return;
    }

    throw new IllegalArgumentException("Invalid CLASS value: " + value);
  }

  @Override
  protected int getRevisionField() {
    return 105;
  }

  @Override
  protected int getUIDField() {
    return 108;
  }

  @Override
  protected void setDefaultValues() {
    super.setDefaultValues();
    if (repeatRule != null) {
      try {
        repeatRule.getInt(0);
      }
      catch (final FieldEmptyException e) {
        repeatRule.setInt(0, 16);
      }
      try {
        repeatRule.getInt(128);
      }
      catch (final FieldEmptyException e) {
        repeatRule.setInt(128, 1);
      }
    }
  }

  @Override
  protected String toDisplayableString() {
    final StringBuffer sb = new StringBuffer("Event[");
    final String data = formatData();
    sb.append(data);
    final RepeatRule rule = getRepeat();
    if (rule != null) {
      if (data.length() > 0) {
        sb.append(", ");
      }
      sb.append("Rule=[");
      final int fields[] = rule.getFields();
      for (int i = 0; i < fields.length; i++) {
        if (i != 0) {
          sb.append(",");
        }
        final int field = fields[i];
        switch (field) {
          case 0: // '\0'
            sb.append("Frequency=");
            switch (rule.getInt(field)) {
              case 16: // '\020'
                sb.append("Daily");

                break;
              case 17: // '\021'
                sb.append("Weekly");

                break;
              case 18: // '\022'
                sb.append("Monthly");

                break;
              case 19: // '\023'
                sb.append("Yearly");

                break;
              default:
                sb.append("<Unknown: " + rule.getInt(field) + ">");

                break;
            }

            break;
          case 64: // '@'
            sb.append("End=" + PIMHandler.getInstance().composeDateTime(rule.getDate(field)));

            break;
          case 32: // ' '
            sb.append("Count=" + rule.getInt(field));

            break;
          case 128:
            sb.append("Interval=" + rule.getInt(field));

            break;
          case 2: // '\002'
            sb.append("DayInWeek=0x" + Integer.toHexString(rule.getInt(field)));

            break;
          case 1: // '\001'
            sb.append("DayInMonth=" + rule.getInt(field));

            break;
          case 4: // '\004'
            sb.append("DayInYear=" + rule.getInt(field));

            break;
          case 16: // '\020'
            sb.append("WeekInMonth=0x" + Integer.toHexString(rule.getInt(field)));

            break;
          case 8: // '\b'
            sb.append("MonthInYear=0x" + Integer.toHexString(rule.getInt(field)));

            break;
          default:
            sb.append("<Unknown: " + field + "=" + rule.getInt(field));

            break;
        }
      }
      sb.append("]");
    }
    sb.append("]");

    return sb.toString();
  }
}
