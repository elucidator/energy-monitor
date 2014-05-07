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

package nl.elucidator.homeautomation.web.gson.converters.timebased;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.elucidator.homeautomation.elastic.data.EnergyChartData;
import nl.elucidator.homeautomation.web.controller.client.ChartSeries;
import nl.elucidator.homeautomation.web.gson.converters.ChartSeriesConverter;

/**
 * Class GsonProducerFactory.
 */
public class TimeBasedConverter {
    private final Gson gson;

    public TimeBasedConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChartSeries.class, new ChartSeriesConverter());
        gsonBuilder.registerTypeAdapter(EnergyChartData.class, new EnergyChartDataTimeXAxisConverter());
        gson = gsonBuilder.create();
    }

    public String toJson(final Object object) {
        return gson.toJson(object);
    }
}
