package eu.z3r0byteapps.soniq.Networking;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpGet {
    //Via deze class kan een GET request verstuurd worden. Het is niet nodig om hier een instance van aan te maken
    public static OkHttpClient client = new OkHttpClient();

    //GET-request versturen naar de opgegeven URL, returnt het antwoord van de server
    public static String get(String url) throws IOException, NullPointerException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
