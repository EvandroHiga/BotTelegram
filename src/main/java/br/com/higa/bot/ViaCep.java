package br.com.higa.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Logger;

import static br.com.higa.bot.Constants.URL_VIA_CEP;

public class ViaCep {
	static Logger log = Logger.getLogger(ViaCep.class.getName());

	private static final String ERRO = "erro";
	private static final String AS_JSON = "/json/";

	public static String consultarCep(String msgRecebidaTxt){
		String cep =
				new StringBuilder(msgRecebidaTxt)
						.delete(0, OpcoesBot.CEP.getNomeServico().length()).toString().trim();

		if(cep.matches("[0-9]{5}-[0-9]{3}") || cep.matches("[0-9]{8}")){
            JsonObject jsonObject;
            try {
                jsonObject = getEnderecoByCep(cep);
            } catch (IOException | IllegalStateException exception) {
				String msgErro = "Erro no servico ViaCep. Tente novamente mais tarde.";
				log.info(msgErro);
				return msgErro;
            }
			return parseViaCepJson(jsonObject);
		} else {
			return "CEP invalido.";
		}
	}

	public static String consultarLogradouro(String msgRecebidaTxt) {
		try{
		String logradouroCompleto =
				new StringBuilder(msgRecebidaTxt)
						.delete(0, OpcoesBot.RUA.getNomeServico().length()).toString().toLowerCase().trim();

		String uf = logradouroCompleto.substring(0, logradouroCompleto.indexOf(",")).trim();
		String cidade = logradouroCompleto.substring(logradouroCompleto.indexOf(",")+1, logradouroCompleto.lastIndexOf(",")).trim();
		String logradouro = logradouroCompleto.substring(logradouroCompleto.lastIndexOf(",")+1, logradouroCompleto.length()).trim();


			JsonArray jsonArray = getEnderecoByLogradouro(uf, cidade, logradouro);
			return parseViaCepLogradouroJson(jsonArray);
		} catch(StringIndexOutOfBoundsException e){
			return "ERRO - " + e.getMessage();
		}
		catch (IOException | IllegalStateException exception) {
			String msgErro = "Erro no servico ViaCep. Tente novamente mais tarde.";
			log.info(msgErro);
			return msgErro;
		}

	}

	private static JsonObject getEnderecoByCep(String cep) throws IOException, IllegalStateException {
		final String url = URL_VIA_CEP + cep + AS_JSON;
		try(Response response = new OkHttpConnection().makeGetRequest(url)){
			return JsonParser.parseString(response.body().string()).getAsJsonObject();
		}
	}

	private static JsonArray getEnderecoByLogradouro(String uf, String cidade, String logradouro) throws IOException, IllegalStateException {
		final String url = URL_VIA_CEP + uf + "/" + cidade + "/" + logradouro + AS_JSON;

		try(Response response = new OkHttpConnection().makeGetRequest(url)){
			return JsonParser.parseString(response.body().string()).getAsJsonArray();
		}
	}

	private static String parseViaCepJson(JsonObject jsonObject){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder
			.append("Logradouro: ").append(jsonObject.get("logradouro").getAsString())
			.append(System.lineSeparator())
			.append("Complemento: ").append(jsonObject.get("complemento").getAsString())
			.append(System.lineSeparator())
			.append("Bairro: ").append(jsonObject.get("bairro").getAsString())
			.append(System.lineSeparator())
			.append(jsonObject.get("localidade").getAsString()).append(" - ").append(jsonObject.get("uf").getAsString())
			.append(System.lineSeparator())
			.append(jsonObject.get("cep").getAsString())
			.append(System.lineSeparator())
			.append("DDD: ").append(jsonObject.get("ddd").getAsString());
		return strBuilder.toString();
	}

	private static String parseViaCepLogradouroJson(JsonArray jsonArray) {
		return parseViaCepJson(jsonArray.get(0).getAsJsonObject());
	}

}
