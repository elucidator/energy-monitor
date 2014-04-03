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

import nl.elucidator.homeautomation.elastic.DataConstants;
import nl.elucidator.homeautomation.energy.powermeter.data.DMSRData;
import nl.elucidator.homeautomation.energy.powermeter.data.ElectricityData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.joda.time.DateTime;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Energy Info
 */
@Singleton
public class EnergyInformation {

    public static final String FACET_NAME = "EnergyDateHistogramFacet";
    private static final Logger LOGGER = LogManager.getLogger(EnergyInformation.class);
    @Inject
    Client client;

    public List<? extends DateHistogramFacet.Entry> getActualPowerHistogram(DateTime start) {
        return this.getActualPowerHistogram(start, DateTime.now());
    }


    public List<? extends DateHistogramFacet.Entry> getActualPowerHistogram(DateTime start, DateTime end) {
        return getHistogramEntries(DataConstants.INDEX_SMARTMETER, start, end, DataConstants.ELECTRICITY_DATA_ACTUAL_POWER, DataConstants.INTERVAL_ONE_HOUR);
    }

    public double getLastEnergyAverage(final int periodInMinutes) {
        DateTime start = DateTime.now().minusMinutes(periodInMinutes);
        System.out.println("start = " + start);
        DateTime end = DateTime.now();
        System.out.println("end = " + end);
        final List<? extends DateHistogramFacet.Entry> histogramEntries = getHistogramEntries(DataConstants.INDEX_SMARTMETER, start, end, DataConstants.ELECTRICITY_DATA_ACTUAL_POWER, periodInMinutes + "m");
        return histogramEntries.get(0).getMean();
    }


    public String getLatestRecored() {

        LOGGER.trace("Retrieving latest Energy data.");
        final DateTime lowerBound = DateTime.now().minusSeconds(15);
        final SearchRequestBuilder searchRequestBuilder = client.prepareSearch("smartmeter").setQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("timeStamp").from(lowerBound.getMillis()).to("now"))).setTypes("record");
        final SearchResponse response = searchRequestBuilder.execute().actionGet();
        final SearchHits searchHits = response.getHits();
        if (searchHits.getTotalHits() == 0) {
            LOGGER.warn("Query resulted in 0 results.");
            return null;
        }

        final SearchHit hit = searchHits.getAt((int) (searchHits.getTotalHits() - 1L));

        return hit.sourceAsString();
    }

    private List<? extends DateHistogramFacet.Entry> getHistogramEntries(final String index, final DateTime start, final DateTime end, final String valueField, final String interval) {

        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet(FACET_NAME).keyField(DataConstants.TIME_STAMP).valueField(valueField).interval(interval);

        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();

        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder(DataConstants.TIME_STAMP).from(start.getMillis()).to(end.getMillis());
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder);

        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);

        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);

        facet.facetFilter(queryFilterBuilder);

        SearchRequestBuilder facetSearch = client.prepareSearch(index).addFacet(facet);

        SearchResponse searchResponse = facetSearch.execute().actionGet();

        Facets facets = searchResponse.getFacets();

        DateHistogramFacet resultFacet = facets.facet(FACET_NAME);

        return resultFacet.getEntries();
    }

    public int getTodaysUsage() {
        DateTime queryTime = DateTime.now();
        Optional<ElectricityData> latestRecordOptional = recordAtTime(queryTime);
        DateTime midnight = new DateTime(queryTime.getYear(), queryTime.getMonthOfYear(), queryTime.getDayOfMonth(), 0, 0, 0);
        Optional<ElectricityData> midnightRecord = recordAtTime(midnight);

        return getDayUsage(midnight, queryTime);
    }

    public int getDayUsage(final int dayInHistory) {
        DateTime startDayFull = DateTime.now().minusDays(dayInHistory);
        DateTime startDay = new DateTime(startDayFull.getYear(), startDayFull.getMonthOfYear(), startDayFull.getDayOfMonth(), 0, 0, 0);
        DateTime endDay = new DateTime(startDayFull.getYear(), startDayFull.getMonthOfYear(), startDayFull.getDayOfMonth(), 23, 59, 59);

        return getDayUsage(startDay, endDay);
    }

    private int getDayUsage(final DateTime from, final DateTime until) {
        final Optional<ElectricityData> fromRecord = recordAtTime(from);
        final Optional<ElectricityData> untilRecord = recordAtTime(until);

        if (fromRecord.isPresent() && untilRecord.isPresent()) {

            ElectricityData electricityDataLatest = untilRecord.orElse(new ElectricityData());
            ElectricityData electricityDataMidnight = fromRecord.orElse(new ElectricityData());
            int lowMeterUsage = electricityDataLatest.getLowTarrifToReading() - electricityDataMidnight.getLowTarrifToReading();
            int normalMeterUsage = electricityDataLatest.getNormalTarrifToReading() - electricityDataMidnight.getNormalTarrifToReading();

            return lowMeterUsage + normalMeterUsage;
        }

        return 0;
    }

    private Optional<ElectricityData> recordAtTime(final DateTime atTime) {

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("smartmeter").setQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("timeStamp").from(atTime.minusSeconds(11)).to(atTime))
        ).setTypes("record");


        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        SearchHits searchHits = searchResponse.getHits();
        LOGGER.trace("Found {} records for {}", searchHits.totalHits(), atTime);
        if (searchHits.totalHits() >= 1) {
            final DMSRData dmsrData = DMSRData.build(searchHits.getAt(0).sourceAsString());
            return Optional.of(dmsrData.getElectricityData());
        }

        return Optional.empty();
    }
}
