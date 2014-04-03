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

package nl.elucidator.homeautomation.weather.openweather.model.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;

import javax.ejb.Singleton;

/**
 * GSon conversion service for the OpenWeather data.
 */
@Singleton
public class OpenWeatherGsonService {
    private final Gson gson;


    public OpenWeatherGsonService() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new OpenWeatherDateTimeConverter());
        gson = gsonBuilder.create();
    }

    public <T> T fromJson(final String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public String toJsonTimeStamped(final Object object, final String src, final String destination) {
        String json = gson.toJson(object);
        return json.replace(src, destination);
    }
}
