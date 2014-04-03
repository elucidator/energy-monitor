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
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test cases
 */

public class DMSRDataProcessorTest {

    private static final String RAW_DATA_1 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01552.438*kWh), 1-0:1.8.2(01849.327*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0001.04*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140115170000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3), (01107.089), 0-2:24.4.0(1), !";
    private static final String RAW_DATA_2 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01552.438*kWh), 1-0:1.8.2(01849.330*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0001.03*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140115170000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3), (01107.089), 0-2:24.4.0(1), !";
    private static final String RAW_DATA_3 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01612.137*kWh), 1-0:1.8.2(01912.867*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0000.33*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140122140000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140122140000)(00)(60)(1)(0-2:24.2.1)(m3), (01153.897), 0-2:24.4.0(1), !";
    private static final DMSRDataProcessor PROCESSOR = new DMSRDataProcessor();
    private DMSRData data1;
    private DMSRData data2;
    private DMSRData data3;


    @Before
    public void before() {
        data1 = PROCESSOR.process(parseRaw(RAW_DATA_1));
        data2 = PROCESSOR.process(parseRaw(RAW_DATA_2));
        data3 = PROCESSOR.process(parseRaw(RAW_DATA_3));
    }

    private List<String> parseRaw(final String rawData) {
        return Arrays.asList(rawData.split(","));
    }

    @Test
    public void header() {
        assertThat(data1.getHeader(), is("ISk5-2MT382-1003"));
    }


    @Test
    public void ElectricalequipmentId() {
        assertThat(data1.getElectricityData().getEquipmentId(), is("5A424556303035313036383434393132"));
    }

    @Test
    public void gasUsage() {
        DMSRDataProcessor processor = new DMSRDataProcessor();
        DMSRData current = processor.process(parseRaw(RAW_DATA_1));
        assertThat(current.getGasUsage(), is(new BigDecimal(0)));
        current = processor.process(parseRaw(RAW_DATA_2));
        assertThat(current.getGasUsage(), is(new BigDecimal(0)));
        current = processor.process(parseRaw(RAW_DATA_3));
        assertThat(current.getGasUsage(), is(new BigDecimal("46.808")));
        current = processor.process(parseRaw(RAW_DATA_3));
        current = processor.process(parseRaw(RAW_DATA_3));
        assertThat(current.getGasUsage(), is(new BigDecimal("46.808")));
        current = processor.process(parseRaw(RAW_DATA_3));
        assertThat(current.getGasUsage(), is(new BigDecimal("46.808")));
        current = processor.process(parseRaw(RAW_DATA_3));
        assertThat(current.getGasUsage(), is(new BigDecimal("46.808")));
        current = processor.process(parseRaw(RAW_DATA_2));
        assertThat(current.getGasUsage(), is(new BigDecimal("46.808")));
    }

    @Test
    public void incorrectOrderGasData() {
        DMSRDataProcessor processor = new DMSRDataProcessor();
        DMSRData current = processor.process(parseRaw(RAW_DATA_1));
        System.out.println("current.getGasUsage() = " + current.getGasUsage());
        current = processor.process(parseRaw(RAW_DATA_3));
        System.out.println("current.getGasUsage() = " + current.getGasUsage());
        current = processor.process(parseRaw(RAW_DATA_2));
        System.out.println("current.getGasUsage() = " + current.getGasUsage());
    }


}
