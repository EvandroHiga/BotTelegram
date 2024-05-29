package br.com.higa.bot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpConnection {
    public Response makeGetRequest(String url) throws IOException, IllegalStateException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request).execute();
    }
}
