package br.com.higa.bot.connection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;

import java.io.IOException;

public class OkHttpConnection {
    public static final Logger logger = Logger.getLogger(OkHttpConnection.class);

    public ResponseBody makeGetRequest(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        return client.newCall(request).execute().body();
    }

}
