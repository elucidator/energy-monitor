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
import nl.elucidator.homeautomation.web.models.Costs;
import nl.elucidator.homeautomation.web.models.Statistics;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Class ElectricityDataProvider.
 */
@Path("/client/electricity")
public class ElectricityDataProvider {
    @Inject
    private EnergyInformation energyInformation;
    @Inject
    private CostCalculator costCalculator;

    //TODO Not loading??
    @NamedProperty(key = "kwh-price", defaultValue = "23")
    private double kwhPrice = 0.23;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/today/stats")
    public Statistics getToday() {
        return new Statistics(energyInformation.getAverage(),
                energyInformation.getLow(),
                energyInformation.getMax(),
                energyInformation.getUsage());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/today/cost")
    public Costs getCosts(@QueryParam("period") @DefaultValue("1") int period) {
        return new Costs(costCalculator.getPrice(energyInformation.getAverage(), period),
                costCalculator.getPrice(energyInformation.getLow(), period),
                costCalculator.getPrice(energyInformation.getMax(), period));
    }

}
