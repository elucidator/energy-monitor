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

toonViewControllers.controller('mainToonController', function ($scope, $http, $timeout) {

        $scope.dmsrData = null;
        $scope.totalEnergyToday = 0;
        $scope.totalEnergyYesterday = 0;
        $scope.percentageYesterday = 0;
        $scope.totalEnergyLastWeek = 0;
        $scope.percentageLastWeek = 0;
        $scope.lowPower = 0;
        $scope.maxPower = 0;
        $scope.averageToday = 0;
        $scope.costLow = 0;
        $scope.costAverage = 0;
        var kwPrice = 0.23;
        $scope.chartSeries = [];

        $scope.getLatestRecord = function () {
            $http.get('rest/client/record/latest')
                .success(function (dmsr, status, headers, config) {
                    $scope.dmsrData = dmsr;
                    return dmsr;
                })
        };

        $scope.getTotalUsedToday = function () {
            $http.get('rest/client/power/usage/today')
                .success(function (usage) {
                    $scope.totalEnergyToday = usage;
                })
        };

        $scope.getUsageYesterday = function () {
            $http.get('rest/client/power/usage/day/1')
                .success(function (usage) {
                    $scope.totalEnergyYesterday = usage;
                })
        };

        $scope.getUsageWeekAgo = function () {
            $http.get('rest/client/power/usage/history/day/7')
                .success(function (usage) {
                    $scope.totalEnergyLastWeek = usage;
                })
        };

        $scope.percentages = function () {
            $scope.percentageLastWeek = $scope.percentage($scope.totalEnergyToday, $scope.totalEnergyLastWeek);
            $scope.percentageYesterday = $scope.percentage($scope.totalEnergyToday, $scope.totalEnergyYesterday);
        };

        $scope.percentage = function (current, previous) {
            var result;
            var x = ((current / previous)) - 1;
            result = parseFloat(x * 100).toFixed(2);

            if (isNaN(result)) {
                return '--';
            }
            return result;
        };

        $scope.getLowPower = function () {
            $http.get('rest/client/power/lowpower/today')
                .success(function (usage) {
                    $scope.lowPower = usage;
                    $scope.costLow = parseFloat(((usage * 24 * 365) / 1000) * kwPrice).toFixed(2);
                })
        };

        $scope.getMaxPower = function () {
            $http.get('rest/client/power/maxpower/today')
                .success(function (usage) {
                    $scope.maxPower = usage;
                })
        };

        $scope.getAverageToday = function () {
            $http.get('rest/client/power/average/today')
                .success(function (usage) {
                    $scope.averageToday = parseFloat(usage).toFixed(1);
                    $scope.costAverage = parseFloat(((usage * 24 * 365) / 1000) * kwPrice).toFixed(2);
                })
        };


        // Function to replicate setInterval using $timeout service.
        $scope.intervalFunction = function () {
            $timeout(function () {
                $scope.getLatestRecord();
                $scope.getTotalUsedToday();
                $scope.getUsageWeekAgo();
                $scope.percentages();
                $scope.getLowPower();
                $scope.getMaxPower();
                $scope.getAverageToday();
                $scope.intervalFunction();
            }, 10000)
        };


        $scope.addSeries = function (serie) {
            $scope.chartSeries.push(serie);
        };

        $scope.loadData = function () {
//            $scope.columnChart.loading = true;
            $http.get('rest/provider/chart/histogram/today?interval=1h')
                .success(function (series) {
                    $scope.addSeries(series);
                });

//            $scope.columnChart.loading = false;
        };


        //Initialize
        $scope.getLatestRecord();
        $scope.getTotalUsedToday();
        $scope.getUsageYesterday();
        $scope.getUsageWeekAgo();
        $scope.percentages();
        $scope.getLowPower();
        $scope.getMaxPower();
        $scope.getAverageToday();
        // Kick off the interval
        $scope.intervalFunction();
        $scope.loadData();


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
                        text: 'W/h',
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
