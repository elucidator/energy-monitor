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

package nl.elucidator.homeautomation.web.gson.converters;

import nl.elucidator.homeautomation.elastic.data.EnergyChartData;
import nl.elucidator.homeautomation.message_writers.gson.timebased.TimeBasedConverter;
import nl.elucidator.homeautomation.web.models.ChartSeries;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Class GsonProducerFactoryTest.
 */
public class GsonProducerFactoryTest {

    TimeBasedConverter factory = new TimeBasedConverter();

    @Test
    public void simple() {
        List<EnergyChartData> chartData = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            chartData.add(new EnergyChartData(DateTime.now(), i));

        }
        ChartSeries chartSeries = new ChartSeries("Wednesday", chartData);

        final String json = factory.toJson(chartSeries);
        System.out.println("json = " + json);

    }
}
