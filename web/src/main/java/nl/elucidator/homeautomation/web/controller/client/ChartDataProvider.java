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
import nl.elucidator.homeautomation.elastic.data.EnergyChartDataRetriever;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Class ChartDataProvider.
 */
@Path("/provider/chart")
public class ChartDataProvider {
    private static final String TIME_BASED = "time";

    @Inject
    EnergyChartDataRetriever retriever;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/histogram/today")
    public ChartSeries getToday(final @DefaultValue("false") @QueryParam("summing") boolean summing, final @DefaultValue("10m") @QueryParam("interval") String interval, final @DefaultValue("time") @QueryParam("base") String base) {
        final ArrayList<EnergyChartData> chartForToday = retriever.getChartForToday(summing, interval);
        final String chartIdentifierName = getChartIdentifierName(chartForToday);
        if (TIME_BASED.equals(base)) {
            return new TimeBasedChartSeries(chartIdentifierName, chartForToday);
        }
        return new ValueBasedChartSeries(chartIdentifierName, chartForToday);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/histogram/history/{days}")
    public ChartSeries getDaysInHistory(@PathParam("days") final int days, @DefaultValue("false") @QueryParam("summing") boolean summing, final @DefaultValue("10m") @QueryParam("interval") String interval, final @DefaultValue("time") @QueryParam("base") String base) {
        final ArrayList<EnergyChartData> chartForToday = retriever.getHistoryDay(days, summing, interval);
        final String chartIdentifierName = getChartIdentifierName(chartForToday);

        ChartSeries energyChartDataChartSeries;
        if (TIME_BASED.equals(base)) {
            energyChartDataChartSeries = new TimeBasedChartSeries(chartIdentifierName, chartForToday);
        } else {
            energyChartDataChartSeries = new ValueBasedChartSeries(chartIdentifierName, chartForToday);
        }
        energyChartDataChartSeries.normalize();
        return energyChartDataChartSeries;
    }

    private String getChartIdentifierName(final ArrayList<EnergyChartData> chartForToday) {
        return chartForToday.get(0).getTimeStamp().dayOfWeek().getAsText();
    }
}
