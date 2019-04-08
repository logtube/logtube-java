package net.landzero.xlog.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dates {

    public static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String yesterday_yyyyMMdd() {
        return new SimpleDateFormat("yyyyMMdd").format(yesterday());
    }

}
