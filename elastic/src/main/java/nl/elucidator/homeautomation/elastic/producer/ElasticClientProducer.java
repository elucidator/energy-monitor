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

package nl.elucidator.homeautomation.elastic.producer;

import nl.elucidator.ee7.utilities.configuration.NamedProperty;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Test cases
 */
public class ElasticClientProducer {

    @Inject
    @NamedProperty(key = "elastic.host")
    private String host;
    @Inject
    @NamedProperty(key = "elastic.port")
    private int port;


    @Produces
    public Client produceClient() {
        return new TransportClient().addTransportAddress(new InetSocketTransportAddress(host, port));
    }

}
