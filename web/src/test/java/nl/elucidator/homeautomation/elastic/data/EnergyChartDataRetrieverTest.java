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

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Class EnergyChartDataRetrieverTest.
 */
public class EnergyChartDataRetrieverTest {
    EnergyChartDataRetriever retriever;

    @Before
    public void before() {

        retriever = new EnergyChartDataRetriever();
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("node.name", "elasticsearch")
                .build();
        retriever.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("elastic.elucidator.nl", 81));
    }

    @After
    public void after() {
        retriever.client.close();
    }

    @Test
    public void simple() {
        DateTime untilTime = DateTime.now();
        DateTime fromTime = untilTime.minusDays(1);
        ArrayList<EnergyChartData> chartData = retriever.getChartForToday(false, "1h");
        final StringBuilder todayBuilder = new StringBuilder();
        chartData.stream().forEach(e -> todayBuilder.append(e.toJavaScriptEntry() + ","));

        chartData = retriever.getHistoryDay(1, true, "30m");
        final StringBuilder historyBuilder = new StringBuilder();
        chartData.stream().forEach(e -> historyBuilder.append(e.toJavaScriptEntry() + ","));
    }
}
