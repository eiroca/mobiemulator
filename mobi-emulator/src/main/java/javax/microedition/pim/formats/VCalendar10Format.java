package javax.microedition.pim.formats;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

// ~--- JDK imports ------------------------------------------------------------

import javax.microedition.pim.AbstractPIMItem;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventImpl;
import javax.microedition.pim.LineReader;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMFormat;
import javax.microedition.pim.PIMHandler;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.RepeatRule;
import javax.microedition.pim.ToDo;
import javax.microedition.pim.ToDoImpl;
import javax.microedition.pim.UnsupportedPIMFormatException;

// Referenced classes of package javax.microedition.pim.formats:
// EndMatcher, FormatSupport, VEventSupport, VToDoSupport
public class VCalendar10Format extends EndMatcher implements PIMFormat {

  private static final int DAYS_OF_WEEK[] = {
      0x10000, 32768, 16384, 8192, 4096, 2048, 1024
  };
  private static final String DAYS_OF_WEEK_CODES[] = {
      "SU", "MO", "TU", "WE", "TH", "FR", "SA"
  };
  private static final int MONTHS_IN_YEAR[] = {
      0, 0x20000, 0x40000, 0x80000, 0x100000, 0x200000, 0x400000, 0x800000, 0x1000000, 0x2000000, 0x4000000,
      0x8000000, 0x10000000
  };
  private static final int WEEKS_OF_MONTH[] = {
      1, 2, 4, 8, 16, 32, 64, 128, 256, 512
  };
  private static final String WEEKS_OF_MONTH_CODES[] = {
      "1+", "2+", "3+", "4+", "5+", "1-", "2-", "3-", "4-", "5-"
  };

  public VCalendar10Format() {
    super("VCALENDAR");
  }

  @Override
  public String getName() {
    return "VCALENDAR/1.0";
  }

  @Override
  public boolean isTypeSupported(final int pimListType) {
    return (pimListType == 3) || (pimListType == 2);
  }

  @Override
  public void encode(final OutputStream out, final String encoding, final PIMItem pimItem) throws IOException {
    final Writer w = new OutputStreamWriter(out, encoding);
    w.write("BEGIN:VCALENDAR\r\n");
    w.write("VERSION:1.0\r\n");
    if (pimItem instanceof Event) {
      encode(w, (Event)pimItem);
    }
    else if (pimItem instanceof ToDo) {
      encode(w, (ToDo)pimItem);
    }
    w.write("END:VCALENDAR\r\n");
    w.flush();
  }

  private void encode(final Writer w, final Event event) throws IOException {
    w.write("BEGIN:VEVENT\r\n");
    final int fields[] = event.getFields();
    for (final int field : fields) {
      final int valueCount = event.countValues(field);
      for (int j = 0; j < valueCount; j++) {
        writeValue(w, event, field, j);
      }
    }
    final String categories = FormatSupport.join(event.getCategories(), ",");
    if (categories.length() > 0) {
      w.write("CATEGORIES:");
      w.write(categories);
      w.write("\r\n");
    }
    final RepeatRule rule = event.getRepeat();
    if (rule != null) {
      final String s = encodeRepeatRule(rule);
      if (s != null) {
        w.write("RRULE:");
        w.write(s);
        w.write("\r\n");
      }
      final Enumeration exDates = rule.getExceptDates();
      if (exDates.hasMoreElements()) {
        w.write("EXDATE:");
        do {
          if (!exDates.hasMoreElements()) {
            break;
          }
          final long time = ((Date)exDates.nextElement()).getTime();
          w.write(PIMHandler.getInstance().composeDateTime(time));
          if (exDates.hasMoreElements()) {
            w.write(";");
          }
        }
        while (true);
        w.write("\r\n");
      }
    }
    w.write("END:VEVENT\r\n");
  }

  private void encode(final Writer w, final ToDo todo) throws IOException {
    w.write("BEGIN:VTODO\r\n");
    final int fields[] = todo.getFields();
    for (final int field : fields) {
      final int valueCount = todo.countValues(field);
      for (int j = 0; j < valueCount; j++) {
        writeValue(w, todo, field, j);
      }
    }
    final String categories = FormatSupport.join(todo.getCategories(), ",");
    if (categories.length() > 0) {
      w.write("CATEGORIES:");
      w.write(categories);
      w.write("\r\n");
    }
    w.write("END:VTODO\r\n");
  }

  private void writeValue(final Writer w, final Event event, final int field, final int index) throws IOException {
    switch (field) {
      default:
        break;
      case 101: // 'e'
      {
        final int iValue = event.getInt(field, index);
        final String sValue = VEventSupport.getClassType(iValue);
        if (sValue != null) {
          w.write("CLASS:");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
      case 100: // 'd'
      {
        final int iValue = event.getInt(field, index);
        try {
          final long startTime = event.getDate(106, 0);
          w.write("DALARM:");
          w.write(PIMHandler.getInstance().composeDateTime(startTime - iValue * 1000));
          w.write("\r\n");
        }
        catch (final Exception e) {
        }

        break;
      }
      case 103: // 'g'
      case 104: // 'h'
      case 107: // 'k'
      case 108: // 'l'
      {
        final String sValue = event.getString(field, index);
        if (sValue != null) {
          final String property = VEventSupport.getFieldLabel(field);
          w.write(property);
          w.write(":");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
      case 102: // 'f'
      case 105: // 'i'
      case 106: // 'j'
      {
        final long date = event.getDate(field, index);
        w.write(VEventSupport.getFieldLabel(field));
        w.write(":");
        w.write(PIMHandler.getInstance().composeDateTime(date));
        w.write("\r\n");

        break;
      }
    }
  }

  private void writeValue(final Writer w, final ToDo todo, final int field, final int index) throws IOException {
    switch (field) {
      default:
        break;
      case 100: // 'd'
      {
        final int iValue = todo.getInt(field, index);
        final String sValue = VToDoSupport.getClassType(iValue);
        if (sValue != null) {
          w.write("CLASS:");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
      case 104: // 'h'
      case 107: // 'k'
      case 108: // 'l'
      {
        final String sValue = todo.getString(field, index);
        if (sValue != null) {
          final String property = VToDoSupport.getFieldLabel(field);
          w.write(property);
          w.write(":");
          w.write(sValue);
          w.write("\r\n");
        }

        break;
      }
      case 102: // 'f'
      case 103: // 'g'
      case 106: // 'j'
      {
        final long date = todo.getDate(field, index);
        w.write(VToDoSupport.getFieldLabel(field));
        w.write(":");
        w.write(PIMHandler.getInstance().composeDateTime(date));
        w.write("\r\n");

        break;
      }
      case 101: // 'e'
      {
        w.write("STATUS:COMPLETED\r\n");

        break;
      }
      case 105: // 'i'
      {
        w.write(VToDoSupport.getFieldLabel(field));
        w.write(":");
        w.write(String.valueOf(todo.getInt(field, index)));
        w.write("\r\n");

        break;
      }
    }
  }

  private String encodeRepeatRule(final RepeatRule rule) {
    final StringBuffer sb = new StringBuffer();
    final int fields[] = rule.getFields();
    FormatSupport.sort(fields);
    if (!FormatSupport.contains(fields, 0)) { return null; }
    final int frequency = rule.getInt(0);
    int interval = 1;
    if (FormatSupport.contains(fields, 128)) {
      interval = rule.getInt(128);
    }
    String encodedCount = " #0";
    if (FormatSupport.contains(fields, 32)) {
      encodedCount = " #" + rule.getInt(32);
    }
    switch (frequency) {
      case 16: // '\020'
        sb.append("D");
        sb.append(interval);
        sb.append(encodedCount);

        break;
      case 17: // '\021'
        sb.append("W");
        sb.append(interval);
        if (FormatSupport.contains(fields, 2)) {
          sb.append(encodeRepeatRuleDaysInWeek(rule));
        }
        sb.append(encodedCount);

        break;
      case 18: // '\022'
        if (FormatSupport.contains(fields, 1)) {
          sb.append("MD");
          sb.append(interval);
          sb.append(" ");
          sb.append(rule.getInt(1));
        }
        else if (FormatSupport.contains(fields, 16)) {
          sb.append("MP");
          sb.append(interval);
          sb.append(encodeRepeatRuleWeeksInMonth(fields, rule));
        }
        sb.append(encodedCount);

        break;
      case 19: // '\023'
        if (FormatSupport.contains(fields, 4)) {
          sb.append("YD");
          sb.append(interval);
          sb.append(" ");
          sb.append(rule.getInt(4));
          sb.append(encodedCount);

          break;
        }
        if (!FormatSupport.contains(fields, 8)) {
          break;
        }
        sb.append("YM");
        sb.append(interval);
        sb.append(" ");
        sb.append(encodeRepeatRuleMonthsInYear(fields, rule));
        sb.append(encodedCount);
        if (FormatSupport.contains(fields, 1)) {
          sb.append(" MD1 ");
          sb.append(rule.getInt(1));
          sb.append(" #1");

          break;
        }
        if (FormatSupport.contains(fields, 16)) {
          sb.append(" MP1 ");
          sb.append(encodeRepeatRuleWeeksInMonth(fields, rule));
          sb.append(" #1");
        }

        break;
      default:
        return null;
    }

    return sb.toString();
  }

  private String encodeRepeatRuleDaysInWeek(final RepeatRule rule) {
    final StringBuffer sb = new StringBuffer();
    final int daysInWeek = rule.getInt(2);
    for (int i = 0; i < VCalendar10Format.DAYS_OF_WEEK.length; i++) {
      if ((daysInWeek & VCalendar10Format.DAYS_OF_WEEK[i]) != 0) {
        sb.append(" ");
        sb.append(VCalendar10Format.DAYS_OF_WEEK_CODES[i]);
      }
    }

    return sb.toString();
  }

  private String encodeRepeatRuleWeeksInMonth(final int fields[], final RepeatRule rule) {
    final StringBuffer sb = new StringBuffer();
    final int weeksInMonth = rule.getInt(16);
    String daysInWeek = "";
    if (FormatSupport.contains(fields, 2)) {
      daysInWeek = encodeRepeatRuleDaysInWeek(rule);
    }
    for (int i = 0; i < VCalendar10Format.WEEKS_OF_MONTH.length; i++) {
      if ((weeksInMonth & VCalendar10Format.WEEKS_OF_MONTH[i]) != 0) {
        sb.append(" ");
        sb.append(VCalendar10Format.WEEKS_OF_MONTH_CODES[i]);
        sb.append(daysInWeek);
      }
    }

    return sb.toString();
  }

  private String encodeRepeatRuleMonthsInYear(final int fields[], final RepeatRule rule) {
    final StringBuffer sb = new StringBuffer();
    final int monthsInYear = rule.getInt(8);
    for (int i = 0; i < VCalendar10Format.MONTHS_IN_YEAR.length; i++) {
      if ((monthsInYear & VCalendar10Format.MONTHS_IN_YEAR[i]) != 0) {
        sb.append(" ");
        sb.append(i);
      }
    }

    return sb.toString();
  }

  @Override
  public PIMItem[] decode(final InputStream in, final String encoding, final PIMList list) throws IOException {
    final LineReader r = new LineReader(in, encoding, this);
    final String line = r.readLine();
    if (line == null) { return null; }
    if (!line.toUpperCase().equals("BEGIN:VCALENDAR")) { throw new UnsupportedPIMFormatException("Not a vCalendar object"); }
    final Vector items = new Vector();
    AbstractPIMItem item;
    while ((item = decode(r)) != null) {
      items.addElement(item);
    }
    if (items.size() == 0) {
      return null;
    }
    else {
      final AbstractPIMItem a[] = new AbstractPIMItem[items.size()];
      items.copyInto(a);

      return a;
    }
  }

  private AbstractPIMItem decode(final LineReader in) throws IOException {
    String line;
    FormatSupport.DataElement element;
    label0: do {
      do {
        do {
          line = in.readLine();
          if (line == null) { return null; }
          element = FormatSupport.parseObjectLine(line);
          if (element.propertyName.equals("BEGIN")) {
            if (element.data.toUpperCase().equals("VEVENT")) { return decodeEvent(in); }
            if (element.data.toUpperCase().equals("VTODO")) {
              return decodeToDo(in);
            }
            else {
              throw new UnsupportedPIMFormatException("Bad argument to BEGIN: " + element.data);
            }
          }
          if (element.propertyName.equals("END")) {
            if (element.data.toUpperCase().equals("VCALENDAR")) {
              return null;
            }
            else {
              throw new UnsupportedPIMFormatException("Bad argument to END: " + element.data);
            }
          }
        }
        while (element.propertyName.equals("PRODID"));
        if (!element.propertyName.equals("VERSION")) {
          continue label0;
        }
      }
      while (element.data.equals("1.0"));

      throw new UnsupportedPIMFormatException("vCalendar version '" + element.data + "' not supported");
    }
    while (element.propertyName.equals("CATEGORIES"));

    throw new UnsupportedPIMFormatException("Unrecognized item: " + line);
  }

  private EventImpl decodeEvent(final LineReader in) throws IOException {
    final EventImpl event = new EventImpl(null);
    do {
      String line;
      if ((line = in.readLine()) == null) {
        break;
      }
      final FormatSupport.DataElement element = FormatSupport.parseObjectLine(line);
      if (element.propertyName.equals("END")) {
        int alarmValues = event.countValues(100);
        if ((alarmValues > 0) && (event.countValues(106) > 0)) {
          final int startTime = (int)(event.getDate(106, 0) / 1000L);
          int i = 0;
          for (; i < alarmValues;) {
            final int alarmTime = event.getInt(100, i);
            if ((alarmTime * 1000) < startTime) {
              event.setInt(100, i, 0, startTime - alarmTime);
            }
            else {
              event.removeValue(100, i);
              alarmValues--;
              i--;
            }
            i++;
          }
        }

        return event;
      }
      if (element.propertyName.equals("VERSION")) {
        if (!element.data.equals("1.0")) { throw new UnsupportedPIMFormatException("Version " + element.data + " not supported"); }
      }
      else if (element.propertyName.equals("CATEGORIES")) {
        final String categories[] = FormatSupport.split(element.data, ',', 0);
        int j = 0;
        while (j < categories.length) {
          try {
            event.addToCategory(categories[j]);
          }
          catch (final PIMException e) {
          }
          j++;
        }
      }
      else {
        importData(event, element.propertyName, element.attributes, element.data);
      }
    }
    while (true);

    throw new IOException("Unterminated vEvent");
  }

  private void importData(final Event event, final String propertyName, final String attributes[], final String data) {
    final int field = VEventSupport.getFieldCode(propertyName);
    switch (field) {
      default:
        break;
      case 103: // 'g'
      case 104: // 'h'
      case 107: // 'k'
      case 108: // 'l'
      {
        final String sdata = FormatSupport.parseString(attributes, data);
        event.addString(field, 0, sdata);

        break;
      }
      case 102: // 'f'
      case 105: // 'i'
      case 106: // 'j'
      {
        final long date = PIMHandler.getInstance().parseDateTime(data);
        event.addDate(field, 0, date);

        break;
      }
      case 101: // 'e'
      {
        final String sdata = FormatSupport.parseString(attributes, data);
        final int c = VEventSupport.getClassCode(sdata);
        event.addInt(101, 0, c);

        break;
      }
      case 100: // 'd'
      {
        final String s[] = FormatSupport.parseStringArray(attributes, data);
        if (s.length > 0) {
          final long alarmTime = PIMHandler.getInstance().parseDateTime(s[0]);
          event.addInt(100, 0, (int)(alarmTime / 1000L));
        }

        break;
      }
    }
  }

  private ToDoImpl decodeToDo(final LineReader in) throws IOException {
    final ToDoImpl todo = new ToDoImpl(null);
    do {
      String line;
      if ((line = in.readLine()) == null) {
        break;
      }
      final FormatSupport.DataElement element = FormatSupport.parseObjectLine(line);
      if (element.propertyName.equals("END")) { return todo; }
      if (element.propertyName.equals("VERSION")) {
        if (!element.data.equals("1.0")) { throw new UnsupportedPIMFormatException("Version " + element.data + " not supported"); }
      }
      else if (element.propertyName.equals("CATEGORIES")) {
        final String categories[] = FormatSupport.split(element.data, ',', 0);
        int j = 0;
        while (j < categories.length) {
          try {
            todo.addToCategory(categories[j]);
          }
          catch (final PIMException e) {
          }
          j++;
        }
      }
      else {
        importData(todo, element.propertyName, element.attributes, element.data);
      }
    }
    while (true);

    throw new IOException("Unterminated vToDo");
  }

  private void importData(final ToDo todo, final String propertyName, final String attributes[], final String data) {
    final int field = VToDoSupport.getFieldCode(propertyName);
    switch (field) {
      case 101: // 'e'
      default:
        break;
      case 104: // 'h'
      case 107: // 'k'
      case 108: // 'l'
      {
        final String sdata = FormatSupport.parseString(attributes, data);
        todo.addString(field, 0, sdata);

        break;
      }
      case 102: // 'f'
      {
        todo.addBoolean(101, 0, true);
        // fall through
      }
      case 103: // 'g'
      case 106: // 'j'
      {
        final long date = PIMHandler.getInstance().parseDateTime(data);
        todo.addDate(field, 0, date);

        break;
      }
      case 100: // 'd'
      {
        final String sdata = FormatSupport.parseString(attributes, data);
        final int c = VToDoSupport.getClassCode(sdata);
        todo.addInt(100, 0, c);

        break;
      }
      case 105: // 'i'
      {
        try {
          final int i = Integer.parseInt(data);
          todo.addInt(105, 0, i);
        }
        catch (final NumberFormatException e) {
        }

        break;
      }
    }
  }
}
