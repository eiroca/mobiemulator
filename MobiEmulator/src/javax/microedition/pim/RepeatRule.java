package javax.microedition.pim;

//~--- JDK imports ------------------------------------------------------------

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

//Referenced classes of package javax.microedition.pim:
//           FieldEmptyException
public class RepeatRule {
    public static final int   APRIL            = 0x100000;
    public static final int   AUGUST           = 0x1000000;
    public static final int   COUNT            = 32;
    public static final int   DAILY            = 16;
    private static final int  DAYS[]           = {
        0x10000, 32768, 16384, 8192, 4096, 2048
    };
    private static final long DAY_INCREMENT    = 0x5265c00L;
    public static final int   DAY_IN_MONTH     = 1;
    public static final int   DAY_IN_WEEK      = 2;
    private static final long DAY_IN_WEEK_MASK = 0x1fc00L;
    public static final int   DAY_IN_YEAR      = 4;
    private static final long DAY_LENGTH       = 0x5265c00L;
    public static final int   DECEMBER         = 0x10000000;
    public static final int   END              = 64;
    public static final int   FEBRUARY         = 0x40000;
    public static final int   FIFTH            = 16;
    public static final int   FIFTHLAST        = 512;
    public static final int   FIRST            = 1;
    public static final int   FOURTH           = 8;
    public static final int   FOURTHLAST       = 256;
    public static final int   FREQUENCY        = 0;
    public static final int   FRIDAY           = 2048;
    public static final int   INTERVAL         = 128;
    public static final int   JANUARY          = 0x20000;
    public static final int   JULY             = 0x800000;
    public static final int   JUNE             = 0x400000;
    public static final int   LAST             = 32;
    public static final int   MARCH            = 0x80000;
    public static final int   MAY              = 0x200000;
    public static final int   MONDAY           = 32768;
    public static final int   MONTHLY          = 18;
    private static final int  MONTHS[]         = {
        0x20000, 0x40000, 0x80000, 0x100000, 0x200000, 0x400000, 0x800000, 0x1000000, 0x2000000, 0x4000000, 0x8000000,
        0x10000000
    };
    public static final int     MONTH_IN_YEAR      = 8;
    private static final long   MONTH_IN_YEAR_MASK = 0x1ffe0000L;
    public static final int     NOVEMBER           = 0x8000000;
    private static final Object NO_DEFAULT         = "";
    public static final int     OCTOBER            = 0x4000000;
    public static final int     SATURDAY           = 1024;
    public static final int     SECOND             = 2;
    public static final int     SECONDLAST         = 64;
    public static final int     SEPTEMBER          = 0x2000000;
    public static final int     SUNDAY             = 0x10000;
    public static final int     THIRD              = 4;
    public static final int     THIRDLAST          = 128;
    public static final int     THURSDAY           = 4096;
    public static final int     TUESDAY            = 16384;
    public static final int     WEDNESDAY          = 8192;
    public static final int     WEEKLY             = 17;
    public static final int     WEEK_IN_MONTH      = 16;
    private static final long   WEEK_IN_MONTH_MASK = 1023L;
    public static final int     YEARLY             = 19;
    private Vector              exceptions;
    private Hashtable           fields;
    public RepeatRule() {
        fields     = new Hashtable();
        exceptions = new Vector();
    }

    public Enumeration dates(long startDate, long subsetBeginning, long subsetEnding) {
        if (subsetBeginning > subsetEnding) {
            throw new IllegalArgumentException("Bad range: " + subsetBeginning + "("
                                               + PIMHandler.getInstance().composeDateTime(subsetBeginning) + ") to "
                                               + subsetEnding + "("
                                               + PIMHandler.getInstance().composeDateTime(subsetEnding));
        }
        Calendar calendar = Calendar.getInstance();
        Date     dateObj  = new Date(startDate);
        calendar.setTime(dateObj);
        Vector  dates       = new Vector();
        long    date        = startDate;
        Integer frequency   = (Integer) getField(0, null);
        int     interval    = ((Integer) getField(128, new Integer(1))).intValue();
        int     count       = ((Integer) getField(32, new Integer(0x7fffffff))).intValue();
        long    end         = ((Long) getField(64, new Long(0x7fffffffffffffffL))).longValue();
        Integer dayInWeek   = (Integer) getField(2, null);
        Integer dayInMonth  = (Integer) getField(1, null);
        Integer dayInYear   = (Integer) getField(4, null);
        Integer weekInMonth = (Integer) getField(16, null);
        Integer monthInYear = (Integer) getField(8, null);
        if ((dayInMonth == null) && (weekInMonth == null)) {
            dayInMonth = new Integer(calendar.get(5));
        }
        if (dayInWeek == null) {
            switch (calendar.get(7)) {
            case 1 :    // '\001'
                dayInWeek = new Integer(0x10000);

                break;
            case 2 :    // '\002'
                dayInWeek = new Integer(32768);

                break;
            case 3 :    // '\003'
                dayInWeek = new Integer(16384);

                break;
            case 4 :    // '\004'
                dayInWeek = new Integer(8192);

                break;
            case 5 :    // '\005'
                dayInWeek = new Integer(4096);

                break;
            case 6 :    // '\006'
                dayInWeek = new Integer(2048);

                break;
            case 7 :    // '\007'
                dayInWeek = new Integer(1024);

                break;
            }
        }
        long rangeStart = Math.max(subsetBeginning, startDate);
        long rangeEnd   = Math.min(subsetEnding, end);
        for (int i = 0;(date <= subsetEnding) && (date <= end) && (i < count);i++) {
            if (frequency == null) {
                storeDate(dates, startDate, rangeStart, rangeEnd);

                break;
            }
            switch (frequency.intValue()) {
            case 16 :    // '\020'
                storeDate(dates, date, rangeStart, rangeEnd);
                date += 0x5265c00L * (long) interval;
                dateObj.setTime(date);
                calendar.setTime(dateObj);

                break;
            case 17 :    // '\021'
                if (dayInWeek == null) {
                    storeDate(dates, date, rangeStart, rangeEnd);
                } else {
                    date -= 0x5265c00L * (long) (calendar.get(7) - 1);
                    dateObj.setTime(date);
                    calendar.setTime(dateObj);
                    storeDays(dates, date, rangeStart, rangeEnd, dayInWeek.intValue());
                }
                date += 0x240c8400L;
                dateObj.setTime(date);
                calendar.setTime(dateObj);

                break;
            case 18 :    // '\022'
                storeDaysByMonth(dates, date, rangeStart, rangeEnd, dayInWeek, dayInMonth, weekInMonth);
                int currentMonth = calendar.get(2);
                if (currentMonth == 11) {
                    int currentYear = calendar.get(1);
                    calendar.set(1, currentYear + 1);
                    calendar.set(2, 0);
                } else {
                    calendar.set(2, currentMonth + 1);
                }
                dateObj = calendar.getTime();
                date    = dateObj.getTime();

                break;
            case 19 :    // '\023'
                if ((monthInYear == null) && (dayInYear == null)) {
                    storeDate(dates, date, rangeStart, rangeEnd);
                } else {
                    calendar.set(2, 0);
                    dateObj = calendar.getTime();
                    date    = dateObj.getTime();
                    if (monthInYear != null) {
                        int months = monthInYear.intValue();
                        for (int m = 0;m < MONTHS.length;m++) {
                            if ((months & MONTHS[m]) != 0) {
                                calendar.set(2, m);
                                storeDaysByMonth(dates, calendar.getTime().getTime(), rangeStart, rangeEnd, dayInWeek,
                                                 dayInMonth, weekInMonth);
                            }
                        }
                    } else {
                        calendar.set(5, 1);
                        dateObj = calendar.getTime();
                        date    = dateObj.getTime();
                        storeDate(dates, date + (long) (dayInYear.intValue() - 1) * 0x5265c00L, rangeStart, rangeEnd);
                    }
                }
                calendar.set(1, calendar.get(1) + 1);
                dateObj = calendar.getTime();
                date    = dateObj.getTime();

                break;
            default :
                throw new IllegalArgumentException("Unrecognized value for frequency: " + frequency);
            }
        }

        return dates.elements();
    }
    private void storeDate(Vector dates, long date, long rangeStart, long rangeEnd) {
        if ((date >= rangeStart) && (date <= rangeEnd)) {
            Date dateObj = new Date(date);
            if (!exceptions.contains(dateObj)) {
                dates.addElement(new Date(date));
            }
        }
    }
    private void storeDays(Vector dates, long date, long rangeStart, long rangeEnd, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date));
        int dayShift = cal.get(7) - 1;
        date -= (long) dayShift * 0x5265c00L;
        long dateNextWeek = (long) dayShift + 0x240c8400L;
        for (int i = 0;i < DAYS.length;i++) {
            if ((days & DAYS[i]) != 0) {
                long targetDate = (dayShift <= i) ? date
                                                  : dateNextWeek;
                storeDate(dates, targetDate + 0x5265c00L * (long) i, rangeStart, rangeEnd);
            }
        }
    }
    private void storeDaysByMonth(Vector dates, long date, long rangeStart, long rangeEnd, Integer dayInWeek,
                                  Integer dayInMonth, Integer weekInMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(date));
        date -= 0x5265c00L * (long) (calendar.get(5) - 1);
        if (dayInMonth != null) {
            storeDate(dates, date + 0x5265c00L * (long) (dayInMonth.intValue() - 1), rangeStart, rangeEnd);
        } else if (weekInMonth != null) {
            long monthRangeStart = Math.max(rangeStart, date);
            long monthEnd        = date + 0x9fa52400L;
            calendar.setTime(new Date(monthEnd));
            for (;calendar.get(5) > 1;calendar.setTime(new Date(monthEnd))) {
                monthEnd -= 0x5265c00L;
            }
            monthEnd -= 0x5265c00L;
            long monthRangeEnd = Math.min(rangeEnd, monthEnd);
            int  weeks         = weekInMonth.intValue();
            if ((weeks & 1) != 0) {
                storeDays(dates, date, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 2) != 0) {
                storeDays(dates, date + 0x240c8400L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 4) != 0) {
                storeDays(dates, date + 0x48190800L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 8) != 0) {
                storeDays(dates, date + 0x6c258c00L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x10) != 0) {
                storeDays(dates, date + 0x90321000L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x20) != 0) {
                storeDays(dates, monthEnd - 0x1ee62800L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x20) != 0) {
                storeDays(dates, monthEnd - 0x1ee62800L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x40) != 0) {
                storeDays(dates, monthEnd - 0x42f2ac00L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x80) != 0) {
                storeDays(dates, monthEnd - 0x66ff3000L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x100) != 0) {
                storeDays(dates, monthEnd - 0x8b0bb400L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
            if ((weeks & 0x200) != 0) {
                storeDays(dates, monthEnd - 0xaf183800L, monthRangeStart, monthRangeEnd, dayInWeek.intValue());
            }
        }
    }
    public void addExceptDate(long date) {
        exceptions.addElement(new Date(date));
    }
    public void removeExceptDate(long date) {
        exceptions.removeElement(new Date(date));
    }
    public Enumeration getExceptDates() {
        Vector results = new Vector();
        Date   date;
        for (Enumeration e = exceptions.elements();e.hasMoreElements();results.addElement(new Date(date.getTime()))) {
            date = (Date) e.nextElement();
        }

        return results.elements();
    }
    public int getInt(int field) {
        validateDataType(field, 3);

        return ((Integer) getField(field, NO_DEFAULT)).intValue();
    }
    private Object getField(int field, Object defaultValue) {
        Integer fieldKey   = new Integer(field);
        Object  fieldValue = fields.get(fieldKey);
        if (fieldValue == null) {
            if (defaultValue == NO_DEFAULT) {
                throw new FieldEmptyException();
            } else {
                return defaultValue;
            }
        } else {
            return fieldValue;
        }
    }
    public void setInt(int field, int value) {
        validateDataType(field, 3);
        boolean isValid;
        switch (field) {
        case 32 :        // ' '
            isValid = value >= 1;

            break;
        case 1 :         // '\001'
            isValid = (value >= 1) && (value <= 31);

            break;
        case 2 :         // '\002'
            isValid = ((long) value & 0xfffffffffffe03ffL) == 0L;

            break;
        case 0 :         // '\0'
            switch (value) {
            case 16 :    // '\020'
            case 17 :    // '\021'
            case 18 :    // '\022'
            case 19 :    // '\023'
                isValid = true;

                break;
            default :
                isValid = false;

                break;
            }

            break;
        case 128 :
            isValid = value >= 1;

            break;
        case 8 :         // '\b'
            isValid = ((long) value & 0xffffffffe001ffffL) == 0L;

            break;
        case 16 :        // '\020'
            isValid = ((long) value & -1024L) == 0L;

            break;
        case 4 :         // '\004'
            isValid = (value >= 1) && (value <= 366);

            break;
        default :
            isValid = false;

            break;
        }
        if (!isValid) {
            throw new IllegalArgumentException("Field value is invalid");
        } else {
            Integer fieldKey = new Integer(field);
            fields.put(fieldKey, new Integer(value));

            return;
        }
    }
    public long getDate(int field) {
        validateDataType(field, 2);

        return ((Long) getField(field, NO_DEFAULT)).longValue();
    }
    public void setDate(int field, long value) {
        validateDataType(field, 2);
        Integer fieldKey = new Integer(field);
        fields.put(fieldKey, new Long(value));
    }
    public int[] getFields() {
        int result[] = new int[fields.size()];
        int i        = 0;
        for (Enumeration e = fields.keys();e.hasMoreElements();) {
            Integer fieldKey = (Integer) e.nextElement();
            result[i++] = fieldKey.intValue();
        }

        return result;
    }
    public boolean equals(Object obj) {
        if ((obj == null) ||!(obj instanceof RepeatRule)) {
            return false;
        }
        RepeatRule rule         = (RepeatRule) obj;
        Calendar   cal          = Calendar.getInstance();
        int        ruleFields[] = rule.getFields();
        for (int i = 0;i < ruleFields.length;i++) {
            int    field = ruleFields[i];
            Object value = fields.get(new Integer(field));
            if (value == null) {
                return false;
            }
            switch (getDataType(field)) {
            case 3 :    // '\003'
                int iValue = ((Integer) value).intValue();
                if (rule.getInt(field) != iValue) {
                    return false;
                }

                break;
            case 2 :    // '\002'
                long thisDate = ((Long) value).longValue();
                long ruleDate = rule.getDate(field);
                if (thisDate == ruleDate) {
                    return true;
                }
                if (Math.abs(thisDate - ruleDate) >= 0x5265c00L) {
                    return false;
                }
                cal.setTime(new Date(thisDate));
                int day = cal.get(5);
                cal.setTime(new Date(ruleDate));
                if (day != cal.get(5)) {
                    return false;
                }

                break;
            default :
                return false;
            }
        }
        for (Enumeration e = fields.keys();e.hasMoreElements();) {
            Integer fieldKey = (Integer) e.nextElement();
            int     field    = fieldKey.intValue();
            boolean match    = false;
            for (int i = 0;(i < ruleFields.length) &&!match;i++) {
                if (ruleFields[i] == field) {
                    match = true;
                }
            }
            if (!match) {
                return false;
            }
        }
        int exceptionDates[] = new int[exceptions.size()];
        for (int i = 0;i < exceptionDates.length;i++) {
            Date date = (Date) exceptions.elementAt(i);
            cal.setTime(date);
            exceptionDates[i] = cal.get(5) + 100 * cal.get(2) + 10000 * cal.get(1);
        }
        boolean matchedExceptionDates[] = new boolean[exceptionDates.length];
        for (Enumeration e = rule.getExceptDates();e.hasMoreElements();) {
            Date date = (Date) e.nextElement();
            cal.setTime(date);
            int     day   = cal.get(5) + 100 * cal.get(2) + 10000 * cal.get(1);
            boolean match = false;
            for (int i = 0;(i < exceptionDates.length) &&!match;i++) {
                if (exceptionDates[i] == day) {
                    match                    = true;
                    matchedExceptionDates[i] = true;
                }
            }
            if (!match) {
                return false;
            }
        }
        for (int i = 0;i < matchedExceptionDates.length;i++) {
            if (matchedExceptionDates[i]) {
                continue;
            }
            boolean duplicate = false;
            for (int j = 0;(j < i) &&!duplicate;j++) {
                duplicate = exceptionDates[j] == exceptionDates[i];
            }
            if (!duplicate) {
                return false;
            }
        }

        return true;
    }
    private void validateDataType(int field, int dataType) {
        int correctDataType = getDataType(field);
        if (dataType != correctDataType) {
            throw new IllegalArgumentException("Invalid field type");
        } else {
            return;
        }
    }
    private int getDataType(int field) {
        switch (field) {
        case 0 :     // '\0'
        case 1 :     // '\001'
        case 2 :     // '\002'
        case 4 :     // '\004'
        case 8 :     // '\b'
        case 16 :    // '\020'
        case 32 :    // ' '
        case 128 :
            return 3;
        case 64 :    // '@'
            return 2;
        }

        throw new IllegalArgumentException("Unrecognized field: " + field);
    }
}
