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

package nl.elucidator.homeautomation.web.providers;

import nl.elucidator.homeautomation.web.controller.client.TimeBasedChartSeries;
import nl.elucidator.homeautomation.web.gson.converters.timebased.TimeBasedConverter;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Class TimeBasedXAxisChartResponse.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class TimeBasedXAxisChartResponse implements MessageBodyWriter<TimeBasedChartSeries> {

    private TimeBasedConverter gsonProducer = new TimeBasedConverter();

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return TimeBasedChartSeries.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(final TimeBasedChartSeries chartSeries, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(final TimeBasedChartSeries chartSeries, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        entityStream.write(gsonProducer.toJson(chartSeries).getBytes());
    }
}
