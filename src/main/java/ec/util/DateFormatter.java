package ec.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateFormatter {
    public static String formatDateToString(Date date, String format,
                                            String timeZone) {
        // null check
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (timeZone == null || "".equalsIgnoreCase(timeZone.trim())) {
            timeZone = Calendar.getInstance().getTimeZone().getID();
        }
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));

        return sdf.format(date);
    }

    public static Date formatStringToDateOnly(String date, String format) throws ParseException {
        if (date == null) return null;
        String dateString = date.substring(0, 10);
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        return inputFormat.parse(dateString);
    }

    public static Date formarStringToDate(String dateStr) {
        if ( "null".equals(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            try {
                return formatStringToDateOnly(dateStr, "yyyy-MM-dd");
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        return date;
    }


}
