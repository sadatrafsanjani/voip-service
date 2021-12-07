package com.jotno.voip.utility;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Formatter {

    public static String formatInstant(Instant instant){

        Date date = Date.from(instant);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");

        return formatter.format(date);
    }
}
