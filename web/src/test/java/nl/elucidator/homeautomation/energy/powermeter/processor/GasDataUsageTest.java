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

import nl.elucidator.homeautomation.energy.powermeter.collector.DataCollector;
import nl.elucidator.homeautomation.energy.powermeter.data.DMSRData;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Test cases
 */
public class GasDataUsageTest {

    private static final String RAW_DATA_LOG = "/raw-data.log";
    private static final DataCollector COLLECTOR = new DataCollector();
    private static final DMSRDataProcessor PROCESSOR = new DMSRDataProcessor();
    private final List<DMSRData> dmsrDataList = new ArrayList<>();


    @Before
    public void before() throws FileNotFoundException {
        InputStream dataStream = this.getClass().getResourceAsStream(RAW_DATA_LOG);
        if (dataStream == null) {
            throw new FileNotFoundException(RAW_DATA_LOG);
        }
        Scanner sc = new Scanner(dataStream);
        sc.useDelimiter("(\\s|,)"); // this means whitespace or comma
        while (sc.hasNext()) {
            String next = sc.next();
            if (next.startsWith("[")) {
                next = next.substring(1);
            }
            if (next.endsWith("]")) {
                next = next.substring(0, next.length() - 1);
            }
            if (next.length() > 0) {
                if (COLLECTOR.add(next)) {
                    DMSRData dmsrData = PROCESSOR.process(COLLECTOR.getData());
                    dmsrDataList.add(dmsrData);
                }
            }

        }
    }

    @Test
    public void listGasUsage() {
        for (DMSRData dmsrData : dmsrDataList) {

        }

    }

}
