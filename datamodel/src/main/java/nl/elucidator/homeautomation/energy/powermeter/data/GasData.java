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

import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * P1 Gas data
 */
public class GasData {

    private String type;
    private String equipmentId;
    private ValvePosition valve;
    private DateTime timeStamp;
    private BigDecimal reading;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public ValvePosition getValve() {
        return valve;
    }

    public void setValve(String valve) {
        this.valve = "1".equals(valve) ? ValvePosition.OPEN : ValvePosition.CLOSED;
    }

    public DateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public BigDecimal getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = new BigDecimal(reading);
    }
}
