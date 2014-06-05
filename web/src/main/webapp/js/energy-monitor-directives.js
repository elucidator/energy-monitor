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

var directives = angular.module('directives', []);

function formatPower(usage) {
    return parseFloat(usage).toFixed(1) + ' W';
}

directives.directive('emDigitalClock', function ($interval) {
    return {
        restrict: 'E',
        scope: {},
        template: '<div ng-bind="now | date:\'HH:mm:ss\'"></div>',
        link: function (scope) {
            console.log("Link Activated");
            scope.now = new Date();
            var clockTimer = $interval(function () {
                scope.now = new Date();
            }, 1000);

            scope.$on('$destroy', function () {
                console.log("Destroy Called");
                $interval.cancel(clockTimer);
            });
        }
    };
});


directives.directive('emAveragePowerToday', function ($interval, $http) {
    return {
        restrict: 'E',
        scope: {},
        template: '<div ng-bind="averagepowertoday"/>',
        link: function (scope) {
            $http.get('rest/client/power/average/today')
                .success(function (usage) {
                    scope.averagepowertoday = formatPower(usage);
                });

            var clockTimer = $interval(function () {
                $http.get('rest/client/power/average/today')
                    .success(function (usage) {
                        scope.averagepowertoday = parseFloat(usage).toFixed(1);
                    })
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }
});

directives.directive('emMinimalPowerToday', function ($interval, $http) {
    return {
        restrict: 'E',
        scope: {},
        template: '<div ng-bind="minimalpowertoday"/>',
        link: function (scope) {
            $http.get('rest/client/power/lowpower/today')
                .success(function (usage) {
                    scope.minimalpowertoday = formatPower(usage);
                });

            var clockTimer = $interval(function () {
                $http.get('rest/client/power/lowpower/today')
                    .success(function (usage) {
                        scope.minimalpowertoday = parseFloat(usage).toFixed(1);
                    })
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }
});