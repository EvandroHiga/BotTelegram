package br.com.higa.bot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;

import static br.com.higa.bot.utils.Constants.*;

public class ViaCep {
	public JsonObject getEnderecoByCep(String cep) {
		try{
			StringBuilder url = new StringBuilder()
					.append(URL_VIA_CEP)
					.append(cep)
					.append(JSON_RESPONSE_VIA_CEP);

			OkHttpClient client = new OkHttpClient();

			Request request =
					new Request.Builder()
							.url(url.toString())
							.build();

			ResponseBody responseBody = client.newCall(request).execute().body();
			return JsonParser.parseString(responseBody.string()).getAsJsonObject();

			//
			// TODO: Tratar adequadamente IOException
			//

		} catch (IOException ioException) {
			System.out.println(ioException + " : " + MSG_ERRO_VIA_CEP_REQUEST);
			return null;
		}
	}

	public String parseViaCepJson(JsonObject jsonObject){
		return new StringBuilder()
				.append("CEP: ").append(jsonObject.get("cep")).append(System.getProperty("line.separator"))
				.append("Logradouro: ").append(jsonObject.get("logradouro")).append(System.getProperty("line.separator"))
				.append("Complemento: ").append(jsonObject.get("complemento")).append(System.getProperty("line.separator"))
				.append("Bairro: ").append(jsonObject.get("bairro")).append(System.getProperty("line.separator"))
				.append("Cidade: ").append(jsonObject.get("localidade")).append(System.getProperty("line.separator"))
				.append("UF: ").append(jsonObject.get("uf")).append(System.getProperty("line.separator"))
				.append("DDD: ").append(jsonObject.get("ddd")).append(System.getProperty("line.separator"))
				.toString();
	}

}
