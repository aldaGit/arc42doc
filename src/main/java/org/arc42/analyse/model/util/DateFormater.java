package org.arc42.analyse.model.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

  public static final String LONG_FORMAT_DE = "dd.MM.yyyy HH:mm:ss";

  public static String format(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT_DE);
    return dateFormat.format(date);
  }

  public static Date parse(String formattedDate) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_FORMAT_DE);
    return dateFormat.parse(formattedDate);
  }
}
