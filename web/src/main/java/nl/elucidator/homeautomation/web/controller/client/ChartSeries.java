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

package nl.elucidator.homeautomation.web.controller.client;

import nl.elucidator.homeautomation.elastic.data.EnergyChartData;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Class ChartSeries.
 */
public class ChartSeries {
    private final String name;
    private final boolean connectNulls;
    private List<EnergyChartData> data;

    public ChartSeries(final String name, final List<EnergyChartData> seriesData) {
        this.connectNulls = true;
        this.name = name;
        this.data = seriesData;
    }


    public final void normalize() {
        final DateTime now = DateTime.now();
        final int year = now.getYear();
        final int month = now.getMonthOfYear();
        final int day = now.getDayOfMonth();

        for (EnergyChartData energyChartData : data) {
            energyChartData.setTimeStamp(normalize(energyChartData.getTimeStamp(), year, month, day));
        }

    }

    private final DateTime normalize(final DateTime dateTime, final int year, final int month, final int day) {
        return new DateTime(year, month, day, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute());
    }

    public String getName() {
        return name;
    }

    public boolean isConnectNulls() {
        return connectNulls;
    }

    public List<EnergyChartData> getData() {
        return data;
    }
}
