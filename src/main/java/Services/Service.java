package Services;

import chatbot.*;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

public class Service {

    public Service() {
    }

    public JsonObject getServicioHoroscopo(Horoscopo horoscopo)
            throws ClientProtocolException, IOException {
        String url = "" + "https://servicioshoroscopebot.herokuapp.com/myApp/rest/horoscopo/" + horoscopo.getSunsign(); //step 2: Create a HTTP client
        HttpClient httpclient = HttpClientBuilder.create().build();

        //step 3: Create a HTTPGet object and execute the url
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpGet);

        //step 4: Process the result
        JsonObject json = null;
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String response_string = EntityUtils.toString(response.getEntity());
            json = (new JsonParser()).parse(response_string)
                    .getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(json);
            System.out.println(prettyJson);
        }
        return json;
    }
}
