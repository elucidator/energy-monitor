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

package nl.elucidator.homeautomation.client.javafx.boundry;

import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

/**
 * Test cases
 */
public class EnergyUsageTest {

    @Test
    public void dayOfWeek() {

        final LocalDate now = LocalDate.now();
        final Object query = now.query(temporal -> temporal.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        System.out.println("query = " + query);

    }

}
