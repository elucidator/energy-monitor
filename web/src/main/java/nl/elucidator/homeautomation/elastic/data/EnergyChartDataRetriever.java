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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.mvel2.util.StringAppender;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.joda.time.DateTime;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.swing.text.html.parser.DTD;
import java.util.ArrayList;
import java.util.List;

/**
 * Class EnergyChartData.
 */
@Singleton
public class EnergyChartDataRetriever {

    public static final String FACET_NAME = "EnergyDateHistogramFacet";
    //private static final String VALUE_SCRIPT = "doc['electricityData.tariffIndicator'].value == 'LOW' ? doc['electricityData.lowTarrifToReading'].value : doc['electricityData.normalTarrifToReading'].value";
    private static final String VALUE_SCRIPT = "doc['electricityData.lowTarrifToReading'].value + doc['electricityData.normalTarrifToReading'].value";
    //private static final String VALUE_SCRIPT = "doc['record.electricityData.tariffIndicator'] == 'HIGH' ? 0 : 1";
    private static final String DEFAULT_INDEX = "smartmeter";
    private static final String DEFAULT_INTERVAL = "10m";
    private static final Logger LOGGER = LogManager.getLogger(EnergyChartDataRetriever.class);
    @Inject
    Client client;

    private DateHistogramFacetBuilder buildFacet(final DateTime fromTime, final DateTime untilTime, final String interval) {
        return buildFacet(fromTime, untilTime, VALUE_SCRIPT, interval);
    }

    private DateHistogramFacetBuilder buildFacet(final DateTime fromTime, final DateTime untilTime, final String valueScript, final String interval) {

        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet(FACET_NAME).keyField(DataConstants.TIME_STAMP).valueScript(valueScript).interval(interval);

        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();

        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder(DataConstants.TIME_STAMP).from(fromTime.getMillis()).to(untilTime.getMillis());
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder);

        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);

        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);

        facet.facetFilter(queryFilterBuilder);


        return facet;
    }

    private java.util.List<? extends DateHistogramFacet.Entry> executeQuery(final String index, final FacetBuilder builder) {
        SearchRequestBuilder facetSearch = client.prepareSearch(index).addFacet(builder).setSize(0);

        SearchResponse searchResponse = facetSearch.execute().actionGet();

        Facets facets = searchResponse.getFacets();

        DateHistogramFacet resultFacet = facets.facet(FACET_NAME);
        return resultFacet.getEntries();
    }

    public ArrayList<EnergyChartData> getChartForToday(final boolean summing, final String interval) {
        DateTime untilTime = DateTime.now();
        DateTime fromTime = new DateTime(untilTime.getYear(), untilTime.getMonthOfYear(), untilTime.getDayOfMonth(), 0, 0, 0);
        return getChartData(fromTime, untilTime, interval, summing);
    }

    public ArrayList<EnergyChartData> getHistoryDay(final int daysAgo, final boolean summing, final String interval) {
        DateTime startDayFull = DateTime.now().minusDays(daysAgo);
        DateTime fromTime = new DateTime(startDayFull.getYear(), startDayFull.getMonthOfYear(), startDayFull.getDayOfMonth(), 0, 0, 0);
        DateTime untilTime = new DateTime(startDayFull.getYear(), startDayFull.getMonthOfYear(), startDayFull.getDayOfMonth(), 23, 59, 59);
        return getChartData(fromTime, untilTime, interval, summing);
    }

    private ArrayList<EnergyChartData> getChartData(final DateTime fromTime, final DateTime untilTime, final String interval, final boolean summing) {
        final DateHistogramFacetBuilder dateHistogramFacetBuilder = buildFacet(fromTime, untilTime, interval);
        final List<? extends DateHistogramFacet.Entry> entries = executeQuery(DEFAULT_INDEX, dateHistogramFacetBuilder);
        ArrayList<EnergyChartData> result = new ArrayList<EnergyChartData>(entries.size());
        DateHistogramFacet.Entry previous = null;
        for (DateHistogramFacet.Entry entry : entries) {
            if (previous == null) {
                previous = entry;
                continue;
            }
            DateTime timeStamp = new DateTime(entry.getTime());
            double value = entry.getMax() - previous.getMax();
            EnergyChartData data = new EnergyChartData(timeStamp, value);
            if (!summing) {
                previous = entry;
            }
            result.add(data);
        }
        return result;
    }
}


