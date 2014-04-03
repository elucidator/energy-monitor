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

package nl.elucidator.homeautomation.weather.openweather;

import nl.elucidator.ee7.utilities.configuration.NamedProperty;
import nl.elucidator.homeautomation.weather.openweather.model.Weather;
import nl.elucidator.homeautomation.weather.openweather.model.gson.OpenWeatherGsonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Stateless interface for retrieving Weather data.
 */
@Stateless
public class OpenWeatherService {

    private static final String PARAM_LOCATION_ID = "id";
    private static final String PARAM_APP_ID = "APPID";
    private static final String PARAM_UNITS = "units";
    private static final Logger LOGGER = LogManager.getLogger(OpenWeatherService.class);
    @Inject
    @NamedProperty(key = "openweather.url", defaultValue = "http://api.openweathermap.org/data/2.5/weather")
    String baseUrl;
    @Inject
    @NamedProperty(key = "openweather.applicationId", mandatory = true)
    String appId;
    @Inject
    @NamedProperty(key = "openweather.units", defaultValue = "metric")
    String units;
    @Inject
    OpenWeatherGsonService gsonService;

    public Weather getWeather(final String locationId) {
        try {
            LOGGER.info("Retrieving weather for location: " + locationId);
            Client client = ClientBuilder.newClient();
            WebTarget base = client.target(baseUrl).queryParam(PARAM_LOCATION_ID, locationId).queryParam(PARAM_UNITS, units).queryParam(PARAM_APP_ID, appId);
            String response = base.request(MediaType.APPLICATION_JSON_TYPE).get(String.class);
            Weather weather = gsonService.fromJson(response, Weather.class);
            LOGGER.trace("Retrieved Weather, temperature: " + weather.getMain().getTemp());
            return weather;
        } catch (ResponseProcessingException e) {
            LOGGER.error(e);
        } catch (ServerErrorException e) {
            Response.StatusType statusType = e.getResponse().getStatusInfo();
            LOGGER.error("Call resulted in error: Family: " + statusType.getFamily() + " Code: " + statusType.getStatusCode() + " Phrase: " + statusType.getReasonPhrase());
            for (Link link : e.getResponse().getLinks()) {
                LOGGER.error("Link: " + link.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Unknown error: " + e.getCause() + " \"" + e.getClass().getSimpleName() + "\"", e);
        }

        return null;
    }

}
