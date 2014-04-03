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

package nl.elucidator.homeautomation.energy.powermeter.data;

import nl.elucidator.homeautomation.energy.powermeter.processor.DMSRDataProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases
 */
public class TestGsonFormat {
    private static final String RAW_DATA_1 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01552.438*kWh), 1-0:1.8.2(01849.327*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0001.04*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140115170000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3), (01107.089), 0-2:24.4.0(1), !";
    //    private static final String RAW_DATA_2 = "ISk5-2MT382-1003, 0-0:96.1.1(5A424556303035313036383434393132), 1-0:1.8.1(01552.438*kWh), 1-0:1.8.2(01849.330*kWh), 1-0:2.8.1(00000.000*kWh), 1-0:2.8.2(00000.005*kWh), 0-0:96.14.0(0002), 1-0:1.7.0(0001.03*kW), 1-0:2.7.0(0000.00*kW), 0-0:17.0.0(0999.00*kW), 0-0:96.3.10(1), 0-0:96.13.1(), 0-0:96.13.0(), 0-1:24.1.0(3), 0-1:96.1.0(3238303131303038323036313435383132), 0-1:24.3.0(140115170000)(00)(60)(1)(0-1:24.2.1)(m3), (00733.261), 0-1:24.4.0(1), 0-2:24.1.0(3), 0-2:96.1.0(3238303131303038333036343239303133), 0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3), (01107.089), 0-2:24.4.0(1), !";
    private static final DMSRDataProcessor PROCESSOR = new DMSRDataProcessor();
    private DMSRData data1;


    @Before
    public void before() {
        data1 = PROCESSOR.process(Arrays.asList(RAW_DATA_1.split(",")));
    }

    @Test
    public void simple() {
        System.out.println("data1 = " + data1.gson());
    }

}
