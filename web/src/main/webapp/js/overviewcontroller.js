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

var overviewControllers = angular.module('overViewControllers', []);

overviewControllers.controller('mainController', function ($scope, $http, $timeout) {

        $scope.dmsrData = null;
        $scope.totalEnergyToday = 0;
        $scope.totalEnergyYesterday = 0;
        $scope.percentageYesterday = 0;
        $scope.totalEnergyLastWeek = 0;
        $scope.percentageLastWeek = 0;
        $scope.lowPower = 0;
        $scope.maxPower = 0;
        $scope.averageToday = 0;

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


        // Function to replicate setInterval using $timeout service.
        $scope.intervalFunction = function () {
            $timeout(function () {
                $scope.getLatestRecord();
                $scope.getTotalUsedToday();
                $scope.getUsageWeekAgo();
                $scope.percentages();
                $scope.intervalFunction();
            }, 10000)
        };


        //Initialize
        $scope.getLatestRecord();
        $scope.getTotalUsedToday();
        $scope.getUsageYesterday();
        $scope.getUsageWeekAgo();
        $scope.percentages();

        // Kick off the interval
        $scope.intervalFunction();
    }
);
