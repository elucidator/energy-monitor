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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * JAX-WS endpoints
 */
@Path("/client")
public class PowerUsageProvider {

    @Inject
    private EnergyInformation energyInformation;

    /**
     * Latest record
     *
     * @return record
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/record/latest")
    public String getLatestDMSRData() {
        return energyInformation.getLatestRecored();
    }

    /**
     * Get avarage for specified period
     *
     * @param period
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("power/average/{periodInMinutes}")
    public double energyAveragePeriod(final @PathParam("periodInMinutes") int period) {
        return energyInformation.getLastEnergyAverage(period);

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/power/usage/history/day/{daysAgo}")
    public int getPeriodDaysInHistory(@PathParam("daysAgo") final int daysAgo) {
        return energyInformation.getUsageHistorySamePeriod(daysAgo);
    }

    /**
     * Energy used in day (midnight to midnight)
     *
     * @param dayInHistory days ago
     * @return Wh
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/power/usage/day/{dayInHistory}")
    public int getToday(final @PathParam("dayInHistory") int dayInHistory) {
        return energyInformation.getDayUsage(dayInHistory);
    }
}
