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
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test cases
 */
public class EnergyInformationTest {
    EnergyInformation energyInformation;

    @Before
    public void before() {

        energyInformation = new EnergyInformation();
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("node.name", "elasticsearch")
                .build();
        energyInformation.client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("elastic.elucidator.nl", 81));
    }

    @Ignore
    @Test
    public void startFrom() {
        DateTime start = DateTime.now().minusDays(1);
        List<? extends DateHistogramFacet.Entry> powerHistogram = energyInformation.getActualPowerHistogram(start);
        assertThat(powerHistogram.size(), is(either(is(23)).or(is(24))));
    }

    @Test
    public void currentEnergy() {
        final String latestRecored = energyInformation.getLatestRecored();
        assertThat(latestRecored, notNullValue());
        System.out.println("latestRecored = " + latestRecored);
    }

    @Test
    public void getLastEnergyAverage() {
        final double lastEnergyAverage = energyInformation.getLastEnergyAverage(10);
        System.out.println("lastEnergyAverage = " + lastEnergyAverage);
    }

    @Test
    public void minPower() {
        final DateTime untilTime = DateTime.now();
        DateTime startTime = untilTime.minusDays(1);
        final DateTime fromTime = new DateTime(startTime.getYear(), startTime.getMonthOfYear(), startTime.getDayOfMonth(), 0, 0, 0);
        final double lowPower = energyInformation.lowPowerPeriod(fromTime, untilTime);
        System.out.println("lowPowerPeriod = " + lowPower);
    }

    @Test
    public void maxPower() {
        final DateTime untilTime = DateTime.now();
        DateTime startTime = untilTime.minusDays(1);
        final DateTime fromTime = new DateTime(startTime.getYear(), startTime.getMonthOfYear(), startTime.getDayOfMonth(), 0, 0, 0);
        final double maxPower = energyInformation.maxPowerPeriod(fromTime, untilTime);
        System.out.println("maxPowerPeriod = " + maxPower);
    }

    @Test
    public void averageToday() {
        final double average = energyInformation.getAverage();
        System.out.println("averagePeriod = " + average);
    }
}
