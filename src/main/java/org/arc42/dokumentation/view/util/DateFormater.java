package org.arc42.dokumentation.view.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

  // --Commented out by Inspection START (17.04.22, 17:49):
  // public static String format(Date date, String format) {
  // SimpleDateFormat dateFormat = new SimpleDateFormat(format);
  // return dateFormat.format(date);
  // }
  // --Commented out by Inspection STOP (17.04.22, 17:49)

  public static String format(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.LONG_FORMAT_DE);
    return dateFormat.format(date);
  }

  public static Date parse(String formattedDate) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat.LONG_FORMAT_DE);
    return dateFormat.parse(formattedDate);
  }
}