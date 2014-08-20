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

package nl.elucidator.homeautomation.web.models;

/**
 * Class TodaysPower.
 */
public class Statistics implements SimpleResonse {
    private final double average;
    private final double low;
    private final double maximum;
    private final int usage;

    public Statistics(final double average, final double low, final double maximum, final int usage) {
        this.average = average;
        this.low = low;
        this.maximum = maximum;
        this.usage = usage;
    }

    public double getAverage() {
        return average;
    }

    public double getLow() {
        return low;
    }

    public double getMaximum() {
        return maximum;
    }

    public int getUsage() {
        return usage;
    }
}
