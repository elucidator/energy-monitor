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

var directives = angular.module('directives', ['EnergyServices']);

function formatPower(usage) {
    return parseFloat(usage).toFixed(1) + " W";
}

directives.directive('emDigitalClock', function ($interval) {
    return {
        restrict: 'E',
        scope: {},
        template: '<div ng-bind="now | date:\'HH:mm:ss\'"></div>',
        link: function (scope) {
            scope.now = new Date();
            var clockTimer = $interval(function () {
                scope.now = new Date();
            }, 1000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    };
});


directives.directive('emPowerTodayAverage', function ($interval, $http, PowerToday) {
    return {
        restrict: 'AE',
        scope: {},
        template: '<div ng-bind="powertodayaverage"/>',
        link: function (scope) {
            var promise = PowerToday.average(function () {
                scope.powertodayaverage = formatPower(promise.value);
            });

            var clockTimer = $interval(function () {
                var promise = PowerToday.average(function () {
                    scope.powertodayaverage = formatPower(promise.value);
                });
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }
});

directives.directive('emPowerTodayLow', function ($interval, $http, PowerToday) {
    return {
        restrict: 'AE',
        scope: {},
        template: '<div ng-bind="powertodaylow"/>',
        link: function (scope) {
            var promise = PowerToday.low(function () {
                scope.powertodaylow = formatPower(promise.value);
            });

            var clockTimer = $interval(function () {
                var promise = PowerToday.low(function () {
                    scope.powertodaylow = formatPower(promise.value);
                });
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }
});

directives.directive('emPowerTodayHigh', function ($interval, $http, PowerToday) {
    return {
        restrict: 'AE',
        scope: {},
        template: '<div ng-bind="powertodayhigh"/>',
        link: function (scope) {
            var promise = PowerToday.high(function () {
                scope.powertodayhigh = formatPower(promise.value);
            });

            var clockTimer = $interval(function () {
                var promise = PowerToday.high(function () {
                    scope.powertodayhigh = formatPower(promise.value);
                });
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }
});

function formatAmount(value) {
    return parseFloat(value).toFixed(2);
}

directives.directive('emPowerCostLow', function ($interval, $http, PowerCost) {
    return {
        restrict: 'AE',
        scope: { period: '=period'},
        template: '<div class="powerNoWrap" ng-bind="powercostlow"/>',
        link: function (scope) {
            var promise = PowerCost.low(function () {
                scope.powercostlow = formatAmount(promise.value);
            });
            var clockTimer = $interval(function () {
                var promise = PowerCost.low(function () {
                    scope.powercostlow = formatAmount(promise.value);
                });
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }

});

directives.directive('emPowerCostAverage', function ($interval, $http, PowerCost) {
    return {
        restrict: 'AE',
        scope: { period: '=period'},
        template: '<div class="powerNoWrap" ng-bind="powercostaverage"/>',
        link: function (scope) {
            var promise = PowerCost.average(function () {
                scope.powercostaverage = formatAmount(promise.value);
            });
            var clockTimer = $interval(function () {
                var promise = PowerCost.average(function () {
                    scope.powercostaverage = formatAmount(promise.value);
                });
            }, 10000);

            scope.$on('$destroy', function () {
                $interval.cancel(clockTimer);
            });
        }
    }

});