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

package nl.elucidator.homeautomation.web.controller.client;

import nl.elucidator.homeautomation.elastic.data.EnergyInformation;
import nl.elucidator.homeautomation.elastic.data.SimpleValue;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Class PowerToday.
 */
@Path("/client/power/today")
public class PowerToday {
    @Inject
    private EnergyInformation energyInformation;

    /**
     * Get Average power consumption today
     *
     * @return W
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("average")
    public SimpleValue<Double> getAverageTodayPower() {
        return new SimpleValue<>(energyInformation.getAverage());
    }

    /**
     * Get Minimal minimal power consumption today
     *
     * @return W
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("low")
    public SimpleValue<Double> getMinPower() {
        return new SimpleValue<>(energyInformation.getLow());
    }

    /**
     * Get Max power consumption for today
     *
     * @return W
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("high")
    public SimpleValue<Double> getMaxPower() {
        return new SimpleValue<>(energyInformation.getMax());
    }

    /**
     * Usage for today, midnight until now
     *
     * @return usage in Wh
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usage")
    public SimpleValue<Integer> getToday() {
        return new SimpleValue<>(energyInformation.getUsage());
    }
}
