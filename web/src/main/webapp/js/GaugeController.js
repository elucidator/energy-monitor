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


//Update the needle
$scope.chartConfig.series[0].data[0] = dmsr.electricityData.actualPower;

//Configuration of the chart/gauge
$scope.chartConfig = {
    options: {
        chart: {
            type: 'gauge',
            borderWidth: 1
        },
        pane: {
            startAngle: -150,
            endAngle: 150
        },
        credits: {
            enabled: false
        },
        exporting: {
            enabled: false
        }
    },
    yAxis: {
        min: 0,
        max: 4000,

        minorTickInterval: 'auto',
        minorTickWidth: 1,
        minorTickLength: 10,
        minorTickPosition: 'inside',
        minorTickColor: '#666',

        tickPixelInterval: 30,
        tickWidth: 2,
        tickPosition: 'inside',
        tickLength: 10,
        tickColor: '#666',
        labels: {
            step: 2,
            rotation: 'auto'
        },
        title: {
            text: 'W/h'
        },
        plotBands: [
            {
                from: 0,
                to: 2000,
                color: '#55BF3B' // green
            },
            {
                from: 2000,
                to: 3000,
                color: '#DDDF0D' // yellow
            },
            {
                from: 3000,
                to: 4000,
                color: '#DF5353' // red
            }
        ]
    },
    series: [
        {
            name: 'Watt/h',
            data: [0],
            dataLabels: {
                formatter: function () {
                    return '<span style="color:#950">' + this.y + ' W/h</span><br/>';

                },
                backgroundColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, '#DDD'],
                        [1, '#FFF']
                    ]
                }
            },
            tooltip: {
                valueSuffix: ' W/h'
            }
        }
    ],
    title: {
        text: 'Current'
    },

    loading: false
}