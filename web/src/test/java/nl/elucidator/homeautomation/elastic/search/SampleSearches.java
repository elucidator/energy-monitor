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

package nl.elucidator.homeautomation.elastic.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacet;
import org.elasticsearch.search.facet.datehistogram.DateHistogramFacetBuilder;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases
 */
public class SampleSearches {
    private static final Logger LOGGER = LogManager.getLogger(SampleSearches.class);
    private TransportClient client;

    @Before
    public void before() {
        client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("192.168.1.142", 9300));
    }

    @Test
    public void dateRange() {
        LOGGER.info("Starting");
        DateTime now = DateTime.now();
        DateTime minusHalfHour = now.minusHours(1);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("smartmeter").setQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("timeStamp").from(minusHalfHour.toInstant().getMillis()).to("now"))
        ).setTypes("weather");


        System.out.println("searchRequestBuilder.toString() = " + searchRequestBuilder.toString());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        System.out.println("searchResponse = " + searchResponse);
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println("searchHit.type() = " + searchHit.type());
            System.out.println(searchHit.sourceAsString());
        }
    }


    @Test
    public void testenergyPerHour() throws InterruptedException {


        DateTime now = DateTime.now();
        DateTime minusHalfHour = now.minusDays(1);


        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet("999").keyField("timeStamp").valueField("electricityData.actualPower").interval("1h");


        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();

        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder("timeStamp").from(minusHalfHour.toInstant().getMillis()).to("now");
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder);

        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);

        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);


        facet.facetFilter(queryFilterBuilder);

        SearchRequestBuilder facetSearch = client.prepareSearch("smartmeter").addFacet(facet);

        System.out.println("facetSearch = " + facetSearch);

        SearchResponse searchResponse = facetSearch.execute().actionGet();
//        System.out.println("searchResponse = " + searchResponse);
        searchResponse.getFacets();
//        System.out.println("searchResponse.getFacets() = " + searchResponse.getFacets());


        Facets facets = searchResponse.getFacets();
        for (Facet facet1 : facets) {
            System.out.println("facet1 = " + facet1);
            DateHistogramFacet dateHistogramFacet = (DateHistogramFacet) facet1;
            System.out.println("dateHistogramFacet.getEntries().size() = " + dateHistogramFacet.getEntries().size());
            for (DateHistogramFacet.Entry entry : dateHistogramFacet) {
                System.out.println("new myEntryDecorator(entry) = " + new myEntryDecorator(entry));
            }
        }

    }

    @Test
    public void testenergyPerHour2() throws InterruptedException {


        DateTime now = DateTime.now();
        DateTime minusHalfHour = now.minusDays(1);


        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet("999").keyField("timeStamp").valueField("electricityData.actualPower").interval("1h");


        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();

        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder("timeStamp").from(minusHalfHour.toInstant().getMillis()).to("now");
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder);

        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);

        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);


        facet.facetFilter(queryFilterBuilder);

        SearchRequestBuilder facetSearch = client.prepareSearch("smartmeter").addFacet(facet);

        System.out.println("facetSearch = " + facetSearch);

        SearchResponse searchResponse = facetSearch.execute().actionGet();
        searchResponse.getFacets();

        DateHistogramFacet dateHistogramFacet = searchResponse.getFacets().facet("999");
        for (DateHistogramFacet.Entry entry : dateHistogramFacet) {
            System.out.println("new myEntryDecorator(entry) = " + new myEntryDecorator(entry));
        }

    }

    @Test
    public void testenergyPerHourMultipleRanges() throws InterruptedException {


        DateTime now = DateTime.now();
        DateTime previousDate = now.minusHours(3);


        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet("999").keyField("timeStamp").valueField("electricityData.actualPower").interval("1h");


        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder("timeStamp").from(previousDate.toInstant().getMillis()).to("now");
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();
        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();
        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);
        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);


        RangeFilterBuilder rangeFilterBuilder2 = new RangeFilterBuilder("timeStamp").from(previousDate.minusDays(2).getMillis()).to(previousDate.minusDays(2).plusHours(5).getMillis());

//        DateTime today = DateTime.now();
        //FilterBuilder orFilterBuilder = FilterBuilders.andFilter().add(rangeFilterBuilder).add(rangeFilterBuilder2);
//        List<RangeFilterBuilder> ranges =  new ArrayList<>();
//        for(int i=0;i<7;i++) {
//            DateTime start = today.minusDays(i);
//            RangeFilterBuilder currentRange = getRangeFilter(start, start.minusHours(2));
//            orFilterBuilder.add(currentRange);
//        }
        BoolFilterBuilder secondMust = FilterBuilders.boolFilter();
        secondMust.must(FilterBuilders.matchAllFilter(), rangeFilterBuilder2);

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder, secondMust);


        facet.facetFilter(queryFilterBuilder);

        SearchRequestBuilder facetSearch = client.prepareSearch("smartmeter").addFacet(facet);

        System.out.println("facetSearch = " + facetSearch);

        SearchResponse searchResponse = facetSearch.execute().actionGet();
        searchResponse.getFacets();

        DateHistogramFacet dateHistogramFacet = searchResponse.getFacets().facet("999");
        System.out.println("dateHistogramFacet.getEntries().size() = " + dateHistogramFacet.getEntries().size());
        for (DateHistogramFacet.Entry entry : dateHistogramFacet) {
            System.out.println("new myEntryDecorator(entry) = " + new myEntryDecorator(entry));
        }

    }

    private RangeFilterBuilder getRangeFilter(final DateTime start, final DateTime end) {
        System.out.println("Adding: start:" + start.getMillis() + " end:" + end.getMillis());
        return new RangeFilterBuilder("timeStamp").from(start.getMillis()).to(end.getMillis());
    }

    @Test
    public void testenergyPerHour3() throws InterruptedException {


        DateTime now = DateTime.now();
        DateTime minusHalfHour = now.minusDays(1);


        DateHistogramFacetBuilder facet = FacetBuilders.dateHistogramFacet("999").keyField("timeStamp").valueField("electricityData.actualPower").interval("1h");


        BoolFilterBuilder topLevelFilterBuilder = FilterBuilders.boolFilter();

        RangeFilterBuilder rangeFilterBuilder = new RangeFilterBuilder("timeStamp").from("now").to("now");
        FilterBuilder matchAllQueryBuilder = FilterBuilders.matchAllFilter();

        topLevelFilterBuilder.must(matchAllQueryBuilder, rangeFilterBuilder);

        FilteredQueryBuilder filteredQueryBuilder = new FilteredQueryBuilder(QueryBuilders.queryString("*"), topLevelFilterBuilder);

        QueryFilterBuilder queryFilterBuilder = new QueryFilterBuilder(filteredQueryBuilder);


        facet.facetFilter(queryFilterBuilder);

        SearchRequestBuilder facetSearch = client.prepareSearch("smartmeter").addFacet(facet);

        System.out.println("facetSearch = " + facetSearch);

        SearchResponse searchResponse = facetSearch.execute().actionGet();
//        System.out.println("searchResponse = " + searchResponse);
        searchResponse.getFacets();
//        System.out.println("searchResponse.getFacets() = " + searchResponse.getFacets());


        Facets facets = searchResponse.getFacets();
        for (Facet facet1 : facets) {
            System.out.println("facet1 = " + facet1);
            DateHistogramFacet dateHistogramFacet = (DateHistogramFacet) facet1;
            System.out.println("dateHistogramFacet.getEntries().size() = " + dateHistogramFacet.getEntries().size());
            for (DateHistogramFacet.Entry entry : dateHistogramFacet) {
                System.out.println("new myEntryDecorator(entry) = " + new myEntryDecorator(entry));
            }
        }

    }

    @Test
    public void specificRecordAtTime() {
        LOGGER.info("Starting");
        DateTime now = DateTime.now();
        DateTime minusHalfHour = now.minusHours(1);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("smartmeter").setQuery(
                QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("timeStamp").from(now.minusSeconds(11)).to("now"))
        ).setTypes("record");


        System.out.println("searchRequestBuilder.toString() = " + searchRequestBuilder.toString());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        System.out.println("searchResponse = " + searchResponse);
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println("searchHit.type() = " + searchHit.type());
            System.out.println(searchHit.sourceAsString());
        }
    }

    private class myEntryDecorator {
        private final DateHistogramFacet.Entry entry;


        private myEntryDecorator(DateHistogramFacet.Entry entry) {
            this.entry = entry;
        }

        @Override
        public String toString() {

            return new DateTime(entry.getTime()) + " count: " + entry.getCount() + " min: " + entry.getMin() + " max: " + entry.getMax() + " mean: " + entry.getMean();
        }
    }


}
