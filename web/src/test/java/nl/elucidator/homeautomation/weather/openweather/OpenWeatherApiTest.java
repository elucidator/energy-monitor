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

import nl.elucidator.homeautomation.weather.openweather.model.Weather;
import org.junit.Ignore;
import org.junit.Test;

import javax.ejb.EJB;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test cases
 */
//@RunWith(Arquillian.class)
public class OpenWeatherApiTest {
    public String weatherLocation = "6544881";

//    @Deployment
//    public static WebArchive createDeployment() {
//        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies()
//                .resolve().withTransitivity().asFile();
//
//        WebArchive javaArchive = ShrinkWrap.create(WebArchive.class)
//                .addPackages(true, ConfigurationFactory.class.getPackage())
//                .addPackages(true, ElasticClientProducder.class.getPackage())
//                .addPackages(true, OpenWeatherService.class.getPackage())
//                .addAsResource("application.properties", "application.properties")
//                 .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
//                .addAsLibraries(libs);
//        System.out.println("javaArchive = " + javaArchive.toString(true));
//        return javaArchive;
//    }


    @EJB
    private OpenWeatherService openWeatherService;


    @Ignore
    @Test
    public void testGetWeather() throws Exception {
        assertThat(openWeatherService, notNullValue());
        System.out.println("openWeatherService.gsonService = " + openWeatherService.gsonService);
        System.out.println("openWeatherService = " + openWeatherService.baseUrl);
        assertThat(openWeatherService.appId, notNullValue());
        assertThat(openWeatherService.gsonService, notNullValue());

        Weather weather = openWeatherService.getWeather(weatherLocation);
        assertThat(weather, notNullValue());
    }
}
