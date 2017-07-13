package com.example.rafaelanastacioalves.desafioandroid.pulllisting;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafaelanastacioalves on 05/07/17.
 */

@SuppressWarnings("DefaultFileTemplate")
class Utils {

    public static long getTimeDeltaUntilNowFrom(Date mLastUpdate) {
        if (mLastUpdate != null) {
            long timeNow = Calendar.getInstance().getTimeInMillis();
            long timeLastUpdate = mLastUpdate.getTime();
            return timeNow - timeLastUpdate;
        } else {
            return 0;
        }
    }
}
