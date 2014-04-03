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

package nl.elucidator.homeautomation.web.controller.powermeter;

import nl.elucidator.homeautomation.elastic.client.PowerMeterElasticClient;
import nl.elucidator.homeautomation.energy.powermeter.collector.DataCollector;
import nl.elucidator.homeautomation.energy.powermeter.data.DMSRData;
import nl.elucidator.homeautomation.energy.powermeter.processor.DMSRDataProcessor;
import nl.elucidator.homeautomation.weather.openweather.timed.TimedWeatherInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by pieter on 1/8/14.
 */
@Path("/powermeter")
public class PowerMeter {

    private static final Logger LOGGER = LogManager.getLogger(PowerMeter.class);
    private static final Logger GSON_LOGGER = LogManager.getLogger("GsonLogger");

    @Inject
    private DataCollector collector;
    @Inject
    private PowerMeterElasticClient powerMeterElasticClient;
    @Inject
    private DMSRDataProcessor processor = new DMSRDataProcessor();
    @Inject
    private TimedWeatherInfo timedWeatherInfo;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/event/{data}")
    public String partialData(@Context HttpServletRequest request, @PathParam("data") @Encoded String data) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received data: " + data);
        }

        byte[] encodedData = DatatypeConverter.parseBase64Binary(data);
        String encodedString = new String(encodedData);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Encoded data: " + encodedString);
        }
        if (collector.add(encodedString)) {
            DMSRData dmsrData = processor.process(collector.getData());
            String gsonData = dmsrData.gson();
            powerMeterElasticClient.add(gsonData);
            GSON_LOGGER.info(gsonData);

        }
        return data;
    }
}
