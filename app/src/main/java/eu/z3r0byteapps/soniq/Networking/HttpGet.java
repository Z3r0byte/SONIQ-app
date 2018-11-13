package eu.z3r0byteapps.soniq.Networking;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpGet {
    public static OkHttpClient client = new OkHttpClient();

    public static String get(String url) throws IOException, NullPointerException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
