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

package nl.elucidator.homeautomation.client.javafx.boundry;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Test cases
 */
public class EnergyUsage {

    //TODO Make configurable
    private static final String TODAY_URL = "http://localhost:8080/homeautomation/rest/client/power/usage/today";
    private static final String DAY_URL = "http://localhost:8080/homeautomation/rest/client/power/usage/day/";
    private static final int DAYS_IN_WEEK = 7;

    //TODO Make this parallel i.e. CompletableFuture
    public List<XYChart.Series> getWeekSeries() {
        List<XYChart.Series> result = new ArrayList<>();
        XYChart.Series series = getCurrentWeek();
        result.add(series);

        result.addAll(getWeekSeries(4));
        return result;
    }

    private List<XYChart.Series> getWeekSeries(final int weeksAgo) {
        List<XYChart.Series> result = new ArrayList<>(weeksAgo);
        int startDay;
        for (int i = 1; i < weeksAgo; i++) {
            startDay = i * DAYS_IN_WEEK;

            XYChart.Series series = new XYChart.Series();
            series.setName("Week " + getWeekNumber(LocalDate.now().minusDays(startDay)));
            final ObservableList seriesData = series.getData();
            final List<Usage> usageList = getUsage(7 + startDay, startDay);
            seriesData.addAll(usageList.stream().map(usage -> new XYChart.Data(usage.day, usage.power)).collect(Collectors.toList()));
            result.add(series);
        }

        return result;
    }

    private XYChart.Series getCurrentWeek() {
        XYChart.Series series = new XYChart.Series();
        series.setName("Week " + getWeekNumber(LocalDate.now()) + " (Current)");
        final ObservableList seriesData = series.getData();
        final List<Usage> usageList = getUsage(6, 0);
        LocalDate localDate = LocalDate.now();
        usageList.add(new Usage(localDayOfWeek(localDate), getToPowerCurrentDay()));
        seriesData.addAll(usageList.stream().map(usage -> new XYChart.Data(usage.day, usage.power)).collect(Collectors.toList()));
        return series;
    }

    private Integer getWeekNumber(final LocalDate date) {
        return date.query(temporal -> temporal.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
    }

    private List<Usage> getUsage(final int start, final int end) {
        List<Usage> result = new ArrayList<>(start);

        LocalDate localDate = LocalDate.now();

        for (int i = start; i > end; i--) {
            final int toPower = getToPower(i);
            final String dayOfWeek = localDayOfWeek(localDate.minusDays(i));
            result.add(new Usage(dayOfWeek, toPower));
        }

        return result;
    }

    private String localDayOfWeek(final LocalDate localDate) {
        return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    private int getToPower(final int day) {
        return execute(DAY_URL + day);
    }

    private int getToPowerCurrentDay() {
        return execute(TODAY_URL);
    }

    private int execute(final String uri) {
        final Client clientBuilder = ClientBuilder.newBuilder().build();
        final WebTarget target = clientBuilder.target(uri);
        final Response response = target.request(MediaType.TEXT_PLAIN).get();
        final String responseData = response.readEntity(String.class);
        response.close();
        return Integer.parseInt(responseData);
    }

    private class Usage {
        String day;
        int power;

        private Usage(final String day, final int power) {
            this.day = day;
            this.power = power;
        }

        @Override
        public String toString() {
            return "Usage{" +
                    "day='" + day + '\'' +
                    ", power=" + power +
                    '}';
        }
    }
}
