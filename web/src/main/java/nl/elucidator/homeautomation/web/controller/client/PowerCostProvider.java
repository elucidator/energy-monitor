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

import nl.elucidator.ee7.utilities.configuration.NamedProperty;
import nl.elucidator.homeautomation.elastic.data.EnergyInformation;
import nl.elucidator.homeautomation.elastic.data.SimpleValue;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Class CostProvider.
 */
@Path("/client/power/cost")
public class PowerCostProvider {
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_A_YEAR = 365;
    private static final int KILO_UNITS = 1000;

    @Inject
    private EnergyInformation energyInformation;

    //TODO Not loading??
    @NamedProperty(key = "kwh-price", defaultValue = "23")
    private double kwhPrice = 0.23;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{level}/{period}")
    public SimpleValue<Double> getCost(@PathParam("level") String condition, @PathParam("period") int periodDays) {
        switch (condition) {
            case "average":
                return getPrice(energyInformation.average(), periodDays);
            case "low":
                return getPrice(energyInformation.lowPower(), periodDays);
            case "high":
                return getPrice(energyInformation.maxPower(), periodDays);
            default:
                throw new IllegalArgumentException(condition + " is not a valid selection");
        }
    }

    private SimpleValue<Double> getPrice(final double i, final int periodDays) {
        return new SimpleValue<>((i * HOURS_IN_DAY * periodDays / KILO_UNITS) * (kwhPrice));
    }
}
