package br.com.higa.bot;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class OkHttpConnection {
    public ResponseBody makeGetRequest(String url) throws IOException, IllegalStateException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try(Response response = okHttpClient.newCall(request).execute()){
            return response.body();
        }
    }
}
