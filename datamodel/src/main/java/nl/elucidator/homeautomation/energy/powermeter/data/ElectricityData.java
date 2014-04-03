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

/**
 * P! electricity data
 */
public class ElectricityData {
    private static final int KWH = 1000;
    private String equipmentId;
    private int lowTarrifToReading;
    private int normalTarrifToReading;
    private int lowTarrifByReading;
    private int normalTarrifByReading;
    private Tariff tariffIndicator;
    private int actualPower;
    private int actualPowerRecieved;
    private int actualTreshHold;
    private SwitchPosition switchPosition;

    public ElectricityData() {
        super();
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getLowTarrifToReading() {
        return lowTarrifToReading;
    }

    public void setLowTarrifToReading(String lowTarrifToReading) {
        this.lowTarrifToReading = toWattHour(lowTarrifToReading);
    }

    public int getNormalTarrifToReading() {
        return normalTarrifToReading;
    }

    public void setNormalTarrifToReading(String normalTarrifToReading) {
        this.normalTarrifToReading = toWattHour(normalTarrifToReading);
    }

    public int getLowTarrifByReading() {
        return lowTarrifByReading;
    }

    public void setLowTarrifByReading(String lowTarrifByReading) {
        this.lowTarrifByReading = toWattHour(lowTarrifByReading);
    }

    public int getNormalTarrifByReading() {
        return normalTarrifByReading;
    }

    public void setNormalTarrifByReading(String normalTarrifByReading) {
        this.normalTarrifByReading = toWattHour(normalTarrifByReading);
    }

    public Tariff getTariffIndicator() {
        return tariffIndicator;
    }

    public void setTariffIndicator(String tariffIndicator) {
        this.tariffIndicator = "0002".equals(tariffIndicator) ? Tariff.HIGH : Tariff.LOW;
    }

    public int getActualPower() {
        return actualPower;
    }

    public void setActualPower(String actualPower) {
        this.actualPower = toWattHour(actualPower);
    }

    public int getActualPowerRecieved() {
        return actualPowerRecieved;
    }

    public void setActualPowerRecieved(String actualPowerRecieved) {
        this.actualPowerRecieved = toWattHour(actualPowerRecieved);
    }

    public int getActualTreshHold() {
        return actualTreshHold;
    }

    public void setActualTreshHold(String actualTreshHold) {
        this.actualTreshHold = toWattHour(actualTreshHold);
    }

    public SwitchPosition getSwitchPosition() {
        return switchPosition;
    }

    public void setSwitchPosition(String switchPosition) {
        this.switchPosition = "1".equals(switchPosition) ? SwitchPosition.CLOSED : SwitchPosition.OPEN;
    }

    private int toWattHour(final String kwh) {
        String value = kwh.substring(0, kwh.indexOf("*"));
        Double valKwh = new Double(value);

        return (int) (valKwh * (double) KWH);
    }
}
