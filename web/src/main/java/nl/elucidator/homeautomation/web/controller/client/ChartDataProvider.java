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
import nl.elucidator.homeautomation.web.gson.producers.GsonProducerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

/**
 * Class ChartDataProvider.
 */
@Path("/provider/chart")
public class ChartDataProvider {

    @Inject
    EnergyChartDataRetriever retriever;
    @Inject
    GsonProducerFactory gsonProducer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/histogram/today")
    public String getToday(final @DefaultValue("false")@QueryParam("summing") boolean summing, final @DefaultValue("10m") @QueryParam("interval") String interval) {
        final ArrayList<EnergyChartData> chartForToday = retriever.getChartForToday(summing, interval);
        final String chartIdentifierName = getChartIdentifierName(chartForToday);
        return gsonProducer.toJson(new ChartSeries(chartIdentifierName, chartForToday));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/histogram/history/{days}")
    public String getDaysInHistory(@PathParam("days") final int days, @DefaultValue("false") @QueryParam("summing") boolean summing, final @DefaultValue("10m") @QueryParam("interval") String interval) {
        final ArrayList<EnergyChartData> chartForToday = retriever.getHistoryDay(days, summing, interval);
        final String chartIdentifierName = getChartIdentifierName(chartForToday);

        final ChartSeries energyChartDataChartSeries = new ChartSeries(chartIdentifierName, chartForToday);
        energyChartDataChartSeries.normalize();
        return gsonProducer.toJson(energyChartDataChartSeries);
    }

    private String getChartIdentifierName(final ArrayList<EnergyChartData> chartForToday) {
        return chartForToday.get(0).getTimeStamp().dayOfWeek().getAsText();
    }
}
