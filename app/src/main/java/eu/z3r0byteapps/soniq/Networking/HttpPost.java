package eu.z3r0byteapps.soniq.Networking;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import eu.z3r0byteapps.soniq.Containers.Search;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost {
    //Via deze class kan een POST request verstuurd worden. Het is niet nodig om hier een instance van aan te maken


    //Definïeren Mediatype header
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    //Aanmaken client met aangepaste timeouts
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();


    // POST-request versturen naar URL met data JSON en search_id uit SEARCH
    public static String post(String url, String json, Search search) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("search_id", search.getSearchId())
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else return null;
    }
}
