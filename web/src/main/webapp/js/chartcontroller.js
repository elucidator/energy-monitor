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

var chartControllers = angular.module('chartControllers', ["highcharts-ng"]);

chartControllers.controller('chartController', function ($scope, $http, $timeout) {

    $scope.chartSeries = [];

    $scope.loadData = function (summing) {
        $scope.areaChart.loading = true;
        $http.get('rest/provider/chart/histogram/today')
            .success(function (series) {
                $scope.addSeries(series);
            })

        for(var i=1;i<6;i++) {
            $http.get('rest/provider/chart/histogram/history/' + i)
                .success(function (series) {
                    $scope.addSeries(series);
                })
        }

        $scope.areaChart.loading = false;
    }

    $scope.addPoints = function () {
        var seriesArray = $scope.chartConfig.series;
        var rndIdx = Math.floor(Math.random() * seriesArray.length);
        seriesArray[rndIdx].data = seriesArray[rndIdx].data.concat([1, 10, 20])
    };

    $scope.addSeries = function (serie) {
        $scope.chartSeries.push(serie);
    }

    $scope.areaChart = {
        options: {
            plotOptions: {
                areaspline: {
                    marker: {
                        enabled: false
                    }
                },
                series: {
                    "stacking": ""
                }
            },

            zoomType: "x",

            legend : {
                enabled : true
            },
            navigator : {
                enabled : true
            },
            scrollbar : {
                enabled : false
            }
        },
        series: $scope.chartSeries,
        title: {
            text: "Energy consumption"
        },
        credits: {
            enabled: false
        },
        loading: true,
        size: {},
        useHighStocks: true
    }

    $scope.reflow = function () {
        $scope.$broadcast('highchartsng.reflow');
    };

    $scope.loadData(false);

});