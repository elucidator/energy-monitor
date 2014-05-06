/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.elucidator.homeautomation.elastic.data;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Class EnergyChartData.
 */
public class EnergyChartData {
    DateTime timeStamp;
    double value;
    String day;

    public EnergyChartData(final DateTime timeStamp, final double value) {
        this.timeStamp = timeStamp;
        this.value = value;
        this.day = (new LocalDate(timeStamp)).dayOfWeek().getAsString();
    }

    public String toJavaScriptEntry() {
        return "[" + toJavaScriptDateUTC(timeStamp) + "," + value + "]";
    }

    public String getDay() {
        return day;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String toJavaScriptDateUTC(final DateTime dateTime) {
        DateTime currentDate = DateTime.now();
        return "Date.UTC(" +
                currentDate.getYear() + "," +
                (currentDate.getMonthOfYear() - 1) + "," +
                currentDate.getDayOfMonth() + "," +
                dateTime.getHourOfDay() + "," +
                dateTime.getMinuteOfHour() + "," +
                dateTime.getSecondOfMinute() + ")";
    }

    public double getValue() {
        return value;
    }
}
