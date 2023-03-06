package br.com.higa.bot.service;

import br.com.higa.bot.connection.OkHttpConnection;
import br.com.higa.bot.enums.OpcoesBot;
import br.com.higa.bot.utils.Constants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;

import java.io.IOException;

import static br.com.higa.bot.utils.Constants.MSG_ERRO_REQUEST_VIA_CEP;
import static br.com.higa.bot.utils.Constants.URL_VIA_CEP;

public class ViaCep {
	public static final Logger logger = Logger.getLogger(ViaCep.class);

	public static String consultarCep(String msgRecebidaTxt){
		String cep =
				new StringBuilder(msgRecebidaTxt)
						.delete(0, OpcoesBot.CEP.getNome().length())
						.toString().trim();

		if(cep.matches("[0-9]{5}-[0-9]{3}") || cep.matches("[0-9]{8}")){
			JsonObject jsonObject = getEnderecoByCep(cep);

			if(jsonObject.has("erro")){
				if(jsonObject.get("erro").getAsBoolean()){
					return Constants.MSG_ERRO_CEP_NAO_ENCONTRADO;
				} else {
					return jsonObject.get("erro").getAsString();
				}
			}
			return parseViaCepJson(jsonObject);
		} else {
			return Constants.MSG_ERRO_CEP_INVALIDO;
		}
	}

	private static JsonObject getEnderecoByCep(String cep) {
		try {
			String url = new StringBuilder()
					.append(URL_VIA_CEP)
					.append(cep)
					.append("/json/").toString();

			ResponseBody responseBody = new OkHttpConnection().makeGetRequest(url);
			return JsonParser.parseString(responseBody.string()).getAsJsonObject();

		} catch (IOException | IllegalStateException exception) {
			logger.error(MSG_ERRO_REQUEST_VIA_CEP, exception);
			JsonObject jsonObjectException = new JsonObject();
			jsonObjectException.addProperty("erro", MSG_ERRO_REQUEST_VIA_CEP);
			return jsonObjectException;
		}
	}

	private static String parseViaCepJson(JsonObject jsonObject){
		return new StringBuilder()
				.append("Logradouro: ").append(jsonObject.get("logradouro").getAsString())
				.append(System.getProperty("line.separator"))
				.append("Complemento: ").append(jsonObject.get("complemento").getAsString())
				.append(System.getProperty("line.separator"))
				.append(jsonObject.get("bairro").getAsString())
				.append(" - ")
				.append(jsonObject.get("localidade").getAsString())
				.append(" - ")
				.append(jsonObject.get("uf").getAsString())
				.append(System.getProperty("line.separator"))
				.append(jsonObject.get("cep").getAsString())
				.append(System.getProperty("line.separator"))
				.toString();
	}

}
