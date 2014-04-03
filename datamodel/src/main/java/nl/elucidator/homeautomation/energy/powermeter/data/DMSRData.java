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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.elucidator.homeautomation.energy.powermeter.data.gson.DateTimeConverter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

/**
 * Collection of data conform specs DMSR 4.0
 */
public class DMSRData {

    private static final Gson gson;
    private ElectricityData electricityData;
    private String header;
    private List<GasData> gasData;
    private DateTime timeStamp;
    private BigDecimal gasUsage;
    private int gasUsage2;

    public DMSRData() {
        gasUsage = new BigDecimal(0);

    }

    public static DMSRData build(final String input) {
        return gson.fromJson(input, DMSRData.class);
    }

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeConverter());
        gson = gsonBuilder.create();
    }

    public ElectricityData getElectricityData() {
        return electricityData;
    }

    public void setElectricityData(ElectricityData electricityData) {
        this.electricityData = electricityData;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<GasData> getGasData() {
        return gasData;
    }

    public void setGasData(List<GasData> gasData) {
        this.gasData = gasData;
    }

    public String gson() {
        return gson.toJson(this);
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getGasUsage() {
        return gasUsage;
    }

    public void setGasUsage(BigDecimal gasUsage) {
        this.gasUsage = gasUsage;
        gasUsage2 = (gasUsage.multiply(new BigDecimal("1000"))).intValue();
    }

}
