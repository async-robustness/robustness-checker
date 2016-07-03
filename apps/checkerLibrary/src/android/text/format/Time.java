/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Stub/model code simplified/modified from the Android library
 */

package android.text.format;

import java.util.TimeZone;

/**
 * An alternative to the {@link java.util.Calendar} and
 * {@link java.util.GregorianCalendar} classes. An instance of the Time class represents
 * a moment in time, specified with second precision. It is modelled after
 * struct tm, and in fact, uses struct tm to implement most of the
 * functionality.
 */
public class Time {
    private static final String Y_M_D_T_H_M_S_000 = "%Y-%m-%dT%H:%M:%S.000";
    private static final String Y_M_D_T_H_M_S_000_Z = "%Y-%m-%dT%H:%M:%S.000Z";
    private static final String Y_M_D = "%Y-%m-%d";

    public static final String TIMEZONE_UTC = "UTC";

    /**
     * The Julian day of the epoch, that is, January 1, 1970 on the Gregorian
     * calendar.
     */
    public static final int EPOCH_JULIAN_DAY = 2440588;

    public boolean allDay;

    public int second;

    public int minute;

    public int hour;

    public int monthDay;

    public int month;

    public int year;

    public int weekDay;

    public int yearDay;

    public int isDst;

    public long gmtoff;

    public String timezone;

    public Time(String timezone) {
        if (timezone == null) {
            throw new NullPointerException("timezone is null!");
        }
        this.timezone = timezone;
        this.year = 1970;
        this.monthDay = 1;
        // Set the daylight-saving indicator to the unknown value -1 so that
        // it will be recomputed.
        this.isDst = -1;
    }

    /**
     * Construct a Time object in the default timezone. The time is initialized to
     * Jan 1, 1970.
     */
    public Time() {
        this(TimeZone.getDefault().getID());
    }

    /**
     * Clears all values, setting the timezone to the given timezone. Sets isDst
     * to a negative value to mean "unknown".
     *
     * @param timezone the timezone to use.
     */
    public void clear(String timezone) {
        if (timezone == null) {
            throw new NullPointerException("timezone is null!");
        }
        this.timezone = timezone;
        this.allDay = false;
        this.second = 0;
        this.minute = 0;
        this.hour = 0;
        this.monthDay = 0;
        this.month = 0;
        this.year = 0;
        this.weekDay = 0;
        this.yearDay = 0;
        this.gmtoff = 0;
        this.isDst = -1;
    }

    public void setToNow() {
        clear(TimeZone.getDefault().getID());
    }

    public void parse(String s) {

    }

    public void normalize(boolean b) {

    }
}
