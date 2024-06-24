package br.com.higa.bot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import static br.com.higa.bot.Constants.MAX_QTY_ADDRESSES_BY_LOGRADOURO;
import static br.com.higa.bot.Constants.URL_VIA_CEP;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ViaCep {
	static Logger log = Logger.getLogger(ViaCep.class.getName());

	private static final String AS_JSON = "/json/";

	public static String consultarCep(String msgRecebidaTxt){
		log.info("CepBot : Consulta por CEP iniciada.");

		String cep =
				new StringBuilder(msgRecebidaTxt)
						.delete(0, OpcoesBot.CEP.getNomeServico().length()).toString().trim();

		if(cep.matches("[0-9]{5}-[0-9]{3}") || cep.matches("[0-9]{8}")){
            JsonObject jsonObject;
            try {
                jsonObject = getEnderecoByCep(cep);
            } catch (IOException | IllegalStateException exception) {
				String msgErro = "Erro no servico ViaCep. Tente novamente mais tarde.";
				log.info("CepBot : " + msgErro);
				return msgErro;
            } catch (Exception exception){
				String msgErro = "Erro inesperado. Tente novamente mais tarde.";
				log.info("CepBot : " + msgErro);
				return msgErro;
			}
			return parseViaCepJsonObj(jsonObject);
		} else {
			String msg = "CEP invalido.";
			log.info("CepBot : " + msg);
			return msg;
		}
	}

	public static String consultarLogradouro(String msgRecebidaTxt) {
		log.info("CepBot : Consulta por LOGRADOURO iniciada");

		try{
			String logradouroCompleto =
					new StringBuilder(msgRecebidaTxt)
							.delete(0, OpcoesBot.RUA.getNomeServico().length()).toString().toLowerCase().trim();

			String uf = logradouroCompleto.substring(0, logradouroCompleto.indexOf(",")).trim();
			String cidade = logradouroCompleto.substring(logradouroCompleto.indexOf(",") + 1, logradouroCompleto.lastIndexOf(",")).trim();
			String logradouro = logradouroCompleto.substring(logradouroCompleto.lastIndexOf(",") + 1).trim();

			JsonArray jsonArray = getEnderecoByLogradouro(uf, cidade, logradouro);

			return parseViaCepJsonArray(jsonArray);
		} catch(StringIndexOutOfBoundsException e){
			String msgErro = "Erro - Formate a mensagem conforme descricao do servico.";
			log.info("CepBot : " + msgErro);
			return msgErro +  System.lineSeparator() + System.lineSeparator() + OpcoesBot.RUA.getDescricaoServico();
		} catch (IOException | IllegalStateException exception) {
			String msgErro = "Erro no servico ViaCep. Tente novamente mais tarde.";
			log.info("CepBot : " + msgErro);
			return msgErro;
		} catch (Exception exception) {
			String msgErro = "Erro inesperado. Tente novamente mais tarde.";
			log.info("CepBot : " + msgErro);
			return msgErro;
		}
	}

	private static JsonObject getEnderecoByCep(String cep) throws IOException, IllegalStateException {
		final String url = URL_VIA_CEP + cep + AS_JSON;

		log.info("CepBot : URL a ser consultada" + url);

		try(Response response = new OkHttpConnection().makeGetRequest(url)){
			if(response.code() == 200){
				final String responseBodyStr = response.body().string();
				log.info("CepBot : Sucesso. Response Body: " + responseBodyStr);
				return JsonParser.parseString(responseBodyStr).getAsJsonObject();
			} else {
				throw new IOException();
			}
		}
	}

	private static JsonArray getEnderecoByLogradouro(String uf, String cidade, String logradouro) throws IOException, IllegalStateException {
		final String url =
				URL_VIA_CEP + URLEncoder.encode(uf, UTF_8) + "/" + URLEncoder.encode(cidade, UTF_8) + "/" + URLEncoder.encode(logradouro, UTF_8) + AS_JSON;
		final String finalUrl = url.replace("+", "%20");

		log.info("CepBot : URL a ser consultada" + finalUrl);

		try(Response response = new OkHttpConnection().makeGetRequest(finalUrl)){
			if(response.code() == 200){
				String responseBodyStr = response.body().string();
				log.info("CepBot : Sucesso. Response Body: " + responseBodyStr);
				return JsonParser.parseString(responseBodyStr).getAsJsonArray();
			} else {
				if(response.code() >= 400 && response.code() < 500) {
					throw new StringIndexOutOfBoundsException();
				} else {
					throw new IOException();
				}
			}

		}
	}

	private static String parseViaCepJsonArray(JsonArray jsonArray) {
		StringBuilder strBuilder = new StringBuilder();
		int qtdeEnderecos = Math.min(jsonArray.size(), MAX_QTY_ADDRESSES_BY_LOGRADOURO);
        for(int i=0; i < qtdeEnderecos; i++){
			strBuilder
					.append(parseViaCepJsonObj(jsonArray.get(i).getAsJsonObject()))
					.append(System.lineSeparator())
					.append(System.lineSeparator());
		}
		return strBuilder.toString().trim();
	}

	private static String parseViaCepJsonObj(JsonObject jsonObject){
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

}
