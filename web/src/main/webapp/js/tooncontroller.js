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

/*
 See http://jsfiddle.net/EVRpj/ for chart
 */
var toonViewControllers = angular.module('toonViewControllers', ['directives', 'highcharts-ng']);

toonViewControllers.controller('mainToonController', function ($scope, $http, $timeout, ElectricityData) {

        $scope.lowPower = 0;
        $scope.maxPower = 0;
        $scope.averageToday = 0;
        $scope.costLow = 0;
        $scope.costAverage = 0;
        $scope.costMax = 0;
        $scope.chartSeries = [];

        $scope.updateTodayData = function () {
            var statsToday = ElectricityData.today(function () {
                $scope.lowPower = statsToday.low;
                $scope.maxPower = statsToday.maximum;
                $scope.averageToday = parseFloat(statsToday.average).toFixed(1);
            })

            var costToday = ElectricityData.costs(function () {
                $scope.costAverage = parseFloat(costToday.average).toFixed(2);
                $scope.costLow = parseFloat(costToday.low).toFixed(2);
                $scope.costMax = parseFloat(costToday.maximum).toFixed(2);
            })
        }

        var timeoutPromise;
        $scope.intervalFunction = function () {
            timeoutPromise = $timeout(function () {
                $scope.updateTodayData();
                $scope.intervalFunction();
            }, 1000);
        };

        $scope.$on('$destroy', function () {
            $timeout.cancel(timeoutPromise);
        });


        $scope.addSeries = function (serie) {
            $scope.chartSeries.push(serie);
        };

        $scope.loadChartData = function () {
            $http.get('rest/provider/chart/histogram/today?interval=1h')
                .success(function (series) {
                    $scope.addSeries(series);
                });
        };


        //Initialize


        // Kick off the interval
        $scope.intervalFunction();

        $scope.loadChartData();


        $scope.columnChart = {
            options: {
                chart: {
                    type: 'column'
                },
                title: {
                    text: 'Energy consumption per hour'
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: 'W',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    plotBands: {
                        color: '#87FA73',
                        from: '150', // Start of the plot band
                        to: '350' // End of the plot band
                    }
                },

                credits: {
                    enabled: false
                },
                exporting: { enabled: false },
                legend: {
                    enabled: false
                }
            },
            series: $scope.chartSeries
        };
    }
);
