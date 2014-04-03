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

package nl.elucidator.homeautomation.energy.powermeter.processor;

import nl.elucidator.homeautomation.energy.powermeter.data.DMSRData;
import nl.elucidator.homeautomation.energy.powermeter.data.GasData;
import nl.elucidator.homeautomation.energy.powermeter.data.ValvePosition;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test cases
 */
public class GasDataTest {

    private static final String RAW_DATA_1 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01552.438*kWh), 1-0:1.8.2(01849.327*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0001.04*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140115170000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3), (01107.089), 0-2:24.4.0(1), !";
    private static final DMSRDataProcessor PROCESSOR = new DMSRDataProcessor();
    private List<GasData> gasDataList;

    @Before
    public void before() {
        DMSRData data = PROCESSOR.process(Arrays.asList(RAW_DATA_1.split(",")));
        gasDataList = data.getGasData();
    }

    @Test
    public void numberOfMeters() {
        assertThat(gasDataList.size(), is(2));
    }

    @Test
    public void reading1() {
        assertThat(gasDataList.get(0).getReading(), is(new BigDecimal("00733.261")));
    }

    @Test
    public void reading2() {
        assertThat(gasDataList.get(1).getReading(), is(new BigDecimal("01107.089")));
    }

    @Test
    public void timestamp() {
        assertThat(gasDataList.get(0).getTimeStamp(), is(new DateTime(14, 1, 15, 17, 0, 0)));
        assertThat(gasDataList.get(1).getTimeStamp(), is(new DateTime(14, 1, 15, 17, 0, 0)));
    }

    @Test
    public void valve() {
        assertThat(gasDataList.get(0).getValve(), is(ValvePosition.OPEN));
        assertThat(gasDataList.get(1).getValve(), is(ValvePosition.OPEN));
    }

    @Test
    public void equipmentId() {
        assertThat(gasDataList.get(0).getEquipmentId(), is("3238303131303038323036313435383132"));
        ;
        assertThat(gasDataList.get(1).getEquipmentId(), is("3238303131303038333036343239303133"));
        ;
    }

    @Test
    public void type() {
        assertThat(gasDataList.get(0).getType(), is("3"));
        ;
        assertThat(gasDataList.get(1).getType(), is("3"));
        ;
    }
}
