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

/**
 * P1 Identifiers
 */
public class DMSRIdentifiers {
    /* Electricity Data **/

    /**
     * Electrical Equipment ID *
     */
    public static final String EQUIMPENT_ID = "0-0:96.1.1";
    /**
     * Meter Reading electricity delivered to client (low tariff) in 0,001 kWh *
     */
    public static final String LOW_TARRIF_TO_READING = "1-0:1.8.1";
    /**
     * Meter Reading electricity delivered to client (normal tariff) in 0,001 kWh *
     */
    public static final String NORMAL_TARRIF_TO_READING = "1-0:1.8.2";
    /**
     * Meter Reading electricity delivered by client (low tariff) in 0,001 kWh *
     */
    public static final String LOW_TARRIF_BY_READING = "1-0:2.8.1";
    /**
     * Meter Reading electricity delivered by client (normal tariff) in 0,001 kWh *
     */
    public static final String NORMAL_TARRIF_BY_READING = "1-0:2.8.2";
    /**
     * Tariff indicator electricity. The tariff indicator can be used to switch tariff dependent loads e.g boilers.
     * This is responsibility of the P1 user *
     */
    public static final String TARRIF_INDICATOR = "0-0:96.14.0";
    /**
     * Actual electricity power delivered (+P) in 1 Watt resolution *
     */
    public static final String ACTUAL_POWER = "1-0:1.7.0";
    /**
     * Actual electricity power received (-P) in 1 Watt resolution *
     */
    public static final String ACTUAL_POWER_RECEIVED = "1-0:2.7.0";
    /**
     * The actual threshold Electricity in kW *
     */
    public static final String ACTUAL_TRASHOLD = "0-0:17.0.0";
    /**
     * Switch position Electricity (in/out/enabled). *
     */
    public static final String SWITCH_POSITION = "0-0:96.3.10";
    /**
     * Number of power failures in any phases *
     */
    public static final String POWER_FAILURES = "0-0:96.7.21";
    /**
     * Number of long power failures in any phases *
     */
    public static final String LONG_POWER_FAILURES = "0-0:96.7.9";
    /**
     * Power failure event log *
     */
    public static final String POWER_FAILURE_EVENT_LOG = "1-0:99:97.0";

    /* Gas data **/
    /**
     * Device-Type *
     */
    public static final String GAS_DEVICE_TYPE = "0-n:24.1.0";
    /**
     * Equipment identifier *
     */
    public static final String GAS_EQUIPMENT_IDENTIFIER = "0-n:96.1.0";
    /**
     * Last hourly value (temperature converted), gas delivered to client in m3, including decimal values and capture time *
     */
    public static final String LAST_HOURLY_VALUE = "0-n:24.2.1";
    /**
     * Valve position gas (on/off/released). (see note 1)
     */
    public static final String GAS_VALVE_POSITION = "0-n:24.4.0";
    /**
     * NOT IN SPEC (maybe old version??)
     */
    public static final String GAS_VALUE_TIMESTAMP = "0-n:24.3.0";
}
