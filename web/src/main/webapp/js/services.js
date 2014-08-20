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

var energyServices = angular.module('EnergyServices', ['ngResource']);

energyServices.factory("PowerToday", function ($resource) {
    return $resource(
        "rest/client/power/today/:method",
        {},
        {
            "average": { method: "GET", params: {method: 'average'}, isArray: false},
            "low": { method: 'GET', params: {method: 'low'}, isArray: false },
            "high": { method: 'GET', params: {method: 'high'}, isArray: false },
            "usage": { method: 'GET', params: {method: 'avarage'}, isArray: false }
        }
    )
});

energyServices.factory("PowerCost", function ($resource) {
    return $resource(
        "rest/client/power/cost/:level/:period",
        {},
        {
            "average": { method: "GET", params: {level: 'average', period: '365', isArray: false}},
            "low": { method: "GET", params: {level: 'low', period: '365', isArray: false}},
            "high": { method: "GET", params: {level: 'high', period: '365', isArray: false}}

        }
    )
});

energyServices.factory("ElectricityData", function ($resource) {
    return $resource(
        "rest/client/electricity/:method/:what",
        {},
        {
            "today": { method: "GET", params: { method: "today", what: "stats"}, isArray: false},
            "costs": { method: "GET", params: { method: "today", what: "cost", period: "365"}, isArray: false}
        }
    )
});



