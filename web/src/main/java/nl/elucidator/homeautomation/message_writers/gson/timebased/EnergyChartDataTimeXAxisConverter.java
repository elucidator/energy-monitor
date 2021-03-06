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

package nl.elucidator.homeautomation.message_writers.gson.timebased;

import com.google.gson.*;
import nl.elucidator.homeautomation.elastic.data.EnergyChartData;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

import java.lang.reflect.Type;

/**
 * Class EnergyChartDataConverter.
 */
public class EnergyChartDataTimeXAxisConverter implements JsonSerializer<EnergyChartData> {
    @Override
    public JsonElement serialize(final EnergyChartData src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonArray jsonArray = new JsonArray();
        final int rawOffset = src.getTimeStamp().getZone().getOffset(Instant.now());
        jsonArray.add(new JsonPrimitive(src.getTimeStamp().toDateTime(DateTimeZone.UTC).getMillis() + rawOffset));
        jsonArray.add(new JsonPrimitive(src.getValue()));
        return jsonArray;
    }
}
