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

package nl.elucidator.homeautomation.energy.powermeter.collector;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Collector for data recieved by the Rest interface.
 */
@ApplicationScoped
public class DataCollector {
    private List<String> dataRows = new Stack<>();

    /**
     * Add data to the collection.
     *
     * @param dataRow Data
     * @return true when input equals "!"
     */
    public boolean add(String dataRow) {
        dataRows.add(dataRow);
        return dataRow.equals("!");
    }

    public List<String> getData() {
        List<String> result = new ArrayList<>(dataRows);
        dataRows.clear();
        return result;
    }
}
