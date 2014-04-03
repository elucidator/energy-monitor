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
import nl.elucidator.homeautomation.energy.powermeter.data.ElectricityData;
import nl.elucidator.homeautomation.energy.powermeter.data.GasData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static nl.elucidator.homeautomation.energy.powermeter.processor.DMSRIdentifiers.*;

/**
 * DMSR Data processor
 */
@ApplicationScoped
public class DMSRDataProcessor {

    private static final Logger DATA_LOGGER = LogManager.getLogger("DataLogger");
    private static final Logger LOGGER = LogManager.getLogger(DMSRDataProcessor.class);
    private DMSRData previousDmsrData;

    public DMSRData process(final List<String> data) {
        if (DATA_LOGGER.isTraceEnabled()) {
            DATA_LOGGER.trace(data);
        }
        DMSRData dmsrData = new DMSRData();
        dmsrData.setTimeStamp(DateTime.now());

        dmsrData.setHeader(data.get(0));

        dmsrData.setElectricityData(parseElectrical(data));

        dmsrData.setGasData(parseGasData(data));

        dmsrData.setGasUsage(calculateGasUsage(dmsrData));
        this.previousDmsrData = dmsrData;

        return dmsrData;
    }

    private BigDecimal calculateGasUsage(DMSRData currentGasData) {
        if (previousDmsrData == null) {
            return new BigDecimal(0);
        }
        boolean updated = isGasDataUpdated(currentGasData, previousDmsrData);

        BigDecimal updatedValue = getGasUsage(currentGasData);
        if (updated && LOGGER.isInfoEnabled()) {
            LOGGER.info("New Gas Usage value = " + updatedValue);
        }
        return updated ? updatedValue : previousDmsrData.getGasUsage();
    }

    private BigDecimal getGasUsage(DMSRData currentGasData) {
        BigDecimal usage = new BigDecimal(0);
        for (GasData current : currentGasData.getGasData()) {
            for (GasData previous : previousDmsrData.getGasData()) {
                if (current.getEquipmentId().equals(previous.getEquipmentId()) && current.getTimeStamp().isAfter(previous.getTimeStamp())) {
                    BigDecimal difference = current.getReading().subtract(previous.getReading());
                    usage = usage.add(difference);
                }
            }
        }
        return usage;
    }

    private boolean isGasDataUpdated(DMSRData currentGasData, DMSRData previousGasData) {
        boolean updated = false;
        for (GasData current : currentGasData.getGasData()) {
            for (GasData previous : previousGasData.getGasData()) {
                if (current.getEquipmentId().equals(previous.getEquipmentId()) && current.getTimeStamp().isAfter(previous.getTimeStamp())) {
                    updated = true;
                    break;
                }
            }
        }
        if (updated) {
            LOGGER.info("Gas data readings are updated:");
        }
        return updated;
    }

    private List<GasData> parseGasData(List<String> data) {

        List<String> gasMeterPrefixes = findGasMeterPrefixes(data);

        ArrayList<GasData> gasDataList = new ArrayList<>();
        for (String prefix : gasMeterPrefixes) {
            gasDataList.add(parseGasDataMeter(data, prefix));
        }

        return gasDataList;
    }

    private GasData parseGasDataMeter(List<String> data, String prefix) {

        String type = GAS_DEVICE_TYPE.replace("n", prefix);
        String identifier = GAS_EQUIPMENT_IDENTIFIER.replace("n", prefix);
        String value = LAST_HOURLY_VALUE.replace("n", prefix);
        String position = GAS_VALVE_POSITION.replace("n", prefix);
        String timeStamp = GAS_VALUE_TIMESTAMP.replace("n", prefix);
        boolean valueInNext = false;

        GasData result = new GasData();

        for (String s : data) {
            if (valueInNext) {
                result.setReading(extractData(s));
                valueInNext = false;
                continue;
            }

            if (s.trim().startsWith(type)) {
                result.setType(extractData(s));
                continue;
            }

            if (s.trim().startsWith(identifier)) {
                result.setEquipmentId(extractData(s));
            }

            if (s.trim().startsWith(value)) {
                throw new IllegalArgumentException("Unexpexted in processing: " + s);
            }

            if (s.trim().startsWith(position)) {
                result.setValve(extractData(s));
            }

            if (s.trim().startsWith(timeStamp)) {
                //Input line is: "0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3)"
                //Next line will be "(01107.089)" and that is what were after
                result.setTimeStamp(extractTimeStamp(s));
                valueInNext = true;
            }
        }

        return result;
    }

    /**
     * Input has this form: "0-2:24.3.0(140115170000)(00)(60)(1)(0-2:24.2.1)(m3)"
     * stamp is in: "140115170000"
     * format: yymmddhh????
     *
     * @param s formatted string
     * @return Date time
     */
    private DateTime extractTimeStamp(String s) {
        String stamp = extractData(s);
        int year = Integer.valueOf(stamp.substring(0, 2));
        int month = Integer.valueOf(stamp.substring(2, 4));
        int day = Integer.valueOf(stamp.substring(4, 6));
        int hour = Integer.valueOf(stamp.substring(6, 8));
        return new DateTime(year, month, day, hour, 0, 0, 0);
    }

    private List<String> findGasMeterPrefixes(List<String> data) {
        List<String> devices = new ArrayList<>();
        boolean foundDevice = true;
        int foundDevices = 0;

        while (foundDevice) {
            foundDevice = false;
            String tmp = GAS_DEVICE_TYPE.replace("n", Integer.toString(++foundDevices));
            for (String s : data) {
                if (s.trim().startsWith(tmp)) {
                    devices.add(Integer.toString(foundDevices));
                    foundDevice = true;
                    break;
                }
            }
        }

        return devices;
    }

    private ElectricityData parseElectrical(final List<String> p1Data) {
        ElectricityData data = new ElectricityData();
        for (String s : p1Data) {


            if (s.trim().startsWith(EQUIMPENT_ID)) {
                data.setEquipmentId(extractData(s));
                continue;
            }

            if (s.trim().startsWith(DMSRIdentifiers.LOW_TARRIF_TO_READING)) {
                data.setLowTarrifToReading(extractData(s));
                continue;
            }

            if (s.trim().startsWith(DMSRIdentifiers.NORMAL_TARRIF_TO_READING)) {
                data.setNormalTarrifToReading(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.LOW_TARRIF_BY_READING)) {
                data.setLowTarrifByReading(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.NORMAL_TARRIF_BY_READING)) {
                data.setNormalTarrifByReading(extractData(s));
                continue;
            }

            if (s.trim().startsWith(DMSRIdentifiers.TARRIF_INDICATOR)) {
                data.setTariffIndicator(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.ACTUAL_POWER)) {
                data.setActualPower(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.ACTUAL_POWER_RECEIVED)) {
                data.setActualPowerRecieved(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.ACTUAL_TRASHOLD)) {
                data.setActualTreshHold(extractData(s));
                continue;
            }
            if (s.trim().startsWith(DMSRIdentifiers.SWITCH_POSITION)) {
                data.setSwitchPosition(extractData(s));
                continue;
            }
        }

        return data;
    }

    private String extractData(final String s) {
        int startIndex = s.indexOf("(");
        int endIndex = s.indexOf(")");
        return s.substring(startIndex + 1, endIndex);
    }

}
