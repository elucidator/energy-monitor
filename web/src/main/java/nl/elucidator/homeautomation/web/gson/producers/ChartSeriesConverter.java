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

package nl.elucidator.homeautomation.web.gson.producers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import nl.elucidator.homeautomation.web.controller.client.ChartSeries;


import java.lang.reflect.Type;

/**
 * Class ChartSeriesConverter.
 * {"name" : "Sunday", "connectNulls" :true, "data" : [[Date.UTC(2014,3,27,0,10,0),47.0],[Date.UTC(2014,3,27,0,20,0),112.0],[Date.UTC(2014,3,27,0,30,0),155.0],[Date.UTC(2014,3,27,0,40,0),197.0],[Date.UTC(2014,3,27,0,50,0),254.0],[Date.UTC(2014,3,27,1,0,0),296.0],[Date.UTC(2014,3,27,1,10,0),339.0],[Date.UTC(2014,3,27,1,20,0),381.0],[Date.UTC(2014,3,27,1,30,0),431.0],[Date.UTC(2014,3,27,1,40,0),474.0],[Date.UTC(2014,3,27,1,50,0),524.0],[Date.UTC(2014,3,27,2,0,0),584.0],[Date.UTC(2014,3,27,2,10,0),633.0],[Date.UTC(2014,3,27,2,20,0),677.0],[Date.UTC(2014,3,27,2,30,0),720.0],[Date.UTC(2014,3,27,2,40,0),770.0],[Date.UTC(2014,3,27,2,50,0),819.0],[Date.UTC(2014,3,27,3,0,0),864.0],[Date.UTC(2014,3,27,3,10,0),907.0],[Date.UTC(2014,3,27,3,20,0),950.0],[Date.UTC(2014,3,27,3,30,0),1011.0],[Date.UTC(2014,3,27,3,40,0),1065.0],[Date.UTC(2014,3,27,3,50,0),1107.0],[Date.UTC(2014,3,27,4,0,0),1151.0],[Date.UTC(2014,3,27,4,10,0),1196.0],[Date.UTC(2014,3,27,4,20,0),1243.0],[Date.UTC(2014,3,27,4,30,0),1285.0],[Date.UTC(2014,3,27,4,40,0),1353.0],[Date.UTC(2014,3,27,4,50,0),1396.0],[Date.UTC(2014,3,27,5,0,0),1445.0],[Date.UTC(2014,3,27,5,10,0),1487.0],[Date.UTC(2014,3,27,5,20,0),1530.0],[Date.UTC(2014,3,27,5,30,0),1576.0],[Date.UTC(2014,3,27,5,40,0),1630.0],[Date.UTC(2014,3,27,5,50,0),1672.0],[Date.UTC(2014,3,27,6,0,0),1716.0],[Date.UTC(2014,3,27,6,10,0),1759.0],[Date.UTC(2014,3,27,6,20,0),1821.0],[Date.UTC(2014,3,27,6,30,0),1871.0],[Date.UTC(2014,3,27,6,40,0),1914.0],[Date.UTC(2014,3,27,6,50,0),1957.0],[Date.UTC(2014,3,27,7,0,0),2007.0],[Date.UTC(2014,3,27,7,10,0),2084.0],[Date.UTC(2014,3,27,7,20,0),2175.0],[Date.UTC(2014,3,27,7,30,0),2258.0],[Date.UTC(2014,3,27,7,40,0),2341.0],[Date.UTC(2014,3,27,7,50,0),2421.0],[Date.UTC(2014,3,27,8,0,0),2498.0],[Date.UTC(2014,3,27,8,10,0),2574.0],[Date.UTC(2014,3,27,8,20,0),2652.0],[Date.UTC(2014,3,27,8,30,0),2757.0],[Date.UTC(2014,3,27,8,40,0),2811.0],[Date.UTC(2014,3,27,8,50,0),2861.0],[Date.UTC(2014,3,27,9,0,0),2912.0],[Date.UTC(2014,3,27,9,10,0),2968.0],[Date.UTC(2014,3,27,9,20,0),3018.0],[Date.UTC(2014,3,27,9,30,0),3076.0],[Date.UTC(2014,3,27,9,40,0),3159.0],[Date.UTC(2014,3,27,9,50,0),3215.0],[Date.UTC(2014,3,27,10,0,0),3265.0],[Date.UTC(2014,3,27,10,10,0),3315.0],[Date.UTC(2014,3,27,10,20,0),3370.0],[Date.UTC(2014,3,27,10,30,0),3451.0],[Date.UTC(2014,3,27,10,40,0),3533.0],[Date.UTC(2014,3,27,10,50,0),3664.0],[Date.UTC(2014,3,27,11,0,0),3777.0],[Date.UTC(2014,3,27,11,10,0),4012.0],[Date.UTC(2014,3,27,11,20,0),4527.0],[Date.UTC(2014,3,27,11,30,0),4817.0],[Date.UTC(2014,3,27,11,40,0),4913.0],[Date.UTC(2014,3,27,11,50,0),5046.0],[Date.UTC(2014,3,27,12,0,0),5132.0],[Date.UTC(2014,3,27,12,10,0),5242.0],[Date.UTC(2014,3,27,12,20,0),5351.0],[Date.UTC(2014,3,27,12,30,0),5460.0],[Date.UTC(2014,3,27,12,40,0),5704.0],[Date.UTC(2014,3,27,12,50,0),5852.0],[Date.UTC(2014,3,27,13,0,0),6016.0],[Date.UTC(2014,3,27,13,10,0),6392.0],[Date.UTC(2014,3,27,13,20,0),6766.0],[Date.UTC(2014,3,27,13,30,0),6871.0],[Date.UTC(2014,3,27,13,40,0),6926.0],[Date.UTC(2014,3,27,13,50,0),7240.0],[Date.UTC(2014,3,27,14,0,0),7640.0],[Date.UTC(2014,3,27,14,10,0),7853.0],[Date.UTC(2014,3,27,14,20,0),7952.0],[Date.UTC(2014,3,27,14,30,0),8010.0],[Date.UTC(2014,3,27,14,40,0),8155.0],[Date.UTC(2014,3,27,14,50,0),8570.0],[Date.UTC(2014,3,27,15,0,0),8714.0],[Date.UTC(2014,3,27,15,10,0),8764.0],[Date.UTC(2014,3,27,15,20,0),8820.0],[Date.UTC(2014,3,27,15,30,0),8860.0],[Date.UTC(2014,3,27,15,40,0),8904.0],[Date.UTC(2014,3,27,15,50,0),8964.0],[Date.UTC(2014,3,27,16,0,0),9003.0],[Date.UTC(2014,3,27,16,10,0),9086.0],[Date.UTC(2014,3,27,16,20,0),9124.0],[Date.UTC(2014,3,27,16,30,0),9168.0],[Date.UTC(2014,3,27,16,40,0),9205.0],[Date.UTC(2014,3,27,16,50,0),9273.0],[Date.UTC(2014,3,27,17,0,0),9310.0],[Date.UTC(2014,3,27,17,10,0),9353.0],[Date.UTC(2014,3,27,17,20,0),9392.0],[Date.UTC(2014,3,27,17,30,0),9429.0],[Date.UTC(2014,3,27,17,40,0),9470.0],[Date.UTC(2014,3,27,17,50,0),9516.0],[Date.UTC(2014,3,27,18,0,0),9555.0],[Date.UTC(2014,3,27,18,10,0),9592.0],[Date.UTC(2014,3,27,18,20,0),9649.0],[Date.UTC(2014,3,27,18,30,0),9693.0],[Date.UTC(2014,3,27,18,40,0),9739.0],[Date.UTC(2014,3,27,18,50,0),9789.0],[Date.UTC(2014,3,27,19,0,0),9863.0],[Date.UTC(2014,3,27,19,10,0),10019.0],[Date.UTC(2014,3,27,19,20,0),10167.0],[Date.UTC(2014,3,27,19,30,0),10239.0],[Date.UTC(2014,3,27,19,40,0),10286.0],[Date.UTC(2014,3,27,19,50,0),10353.0],[Date.UTC(2014,3,27,20,0,0),10407.0],[Date.UTC(2014,3,27,20,10,0),10484.0],[Date.UTC(2014,3,27,20,20,0),10539.0],[Date.UTC(2014,3,27,20,30,0),10592.0],[Date.UTC(2014,3,27,20,40,0),10651.0],[Date.UTC(2014,3,27,20,50,0),10730.0],[Date.UTC(2014,3,27,21,0,0),10810.0],[Date.UTC(2014,3,27,21,10,0),10912.0],[Date.UTC(2014,3,27,21,20,0),11001.0],[Date.UTC(2014,3,27,21,30,0),11080.0],[Date.UTC(2014,3,27,21,40,0),11136.0]]},
 */
public class ChartSeriesConverter implements JsonSerializer<ChartSeries> {
    private static final String NAME = "name";
    private static final String CONNECT_NULLS = "connectNulls";
    private static final String DATA = "data";
    @Override
    public JsonElement serialize(final ChartSeries src, final Type typeOfSrc, final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(NAME, src.getName());
        jsonObject.addProperty(CONNECT_NULLS, src.isConnectNulls());
        final JsonElement data = context.serialize(src.getData());
        jsonObject.add(DATA, data);

        return jsonObject;
    }
}
