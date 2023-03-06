package br.com.higa.bot.service;

import br.com.higa.bot.connection.OkHttpConnection;
import br.com.higa.bot.enums.OpcoesBot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.ResponseBody;
import org.apache.log4j.Logger;

import java.io.IOException;

import static br.com.higa.bot.utils.Constants.*;

public class ClimaTempo {
    public static final Logger logger = Logger.getLogger(ClimaTempo.class);

    public static String executarAcaoPrevisaoTempo(String msgRecebidaTxt) {
        String cidade =
                new StringBuilder(msgRecebidaTxt)
                        .delete(0, OpcoesBot.TEMPO.getNome().length())
                        .toString().trim();

        JsonObject jsonObjectResponse = getForecastByCityName(cidade);

        if(jsonObjectResponse.has("error")){
            return jsonObjectResponse.get("detail").getAsString();
        } else {
            return parseClimaTempoJson(jsonObjectResponse);
        }
    }

    private static JsonObject getForecastByCityName(String cidade){
        try{
            JsonObject jsonObject = getCityInfoByCityName(cidade);

            if(jsonObject.has("error")){
                return jsonObject;
            } else {
                String url = new StringBuilder()
                        .append(URL_CLIMATEMPO)
                        .append("/forecast/locale/")
                        .append(jsonObject.get("id").getAsString())
                        .append("/days/15?token=")
                        .append(CLIMATEMPO_USER_TOKEN).toString();

                ResponseBody responseBody = new OkHttpConnection().makeGetRequest(url);
                return JsonParser.parseString(responseBody.string()).getAsJsonObject();
            }
        } catch (IOException | IllegalStateException exception) {
            logger.error(MSG_ERRO_REQUEST_CLIMATEMPO, exception);
            JsonObject jsonObjectException = new JsonObject();
            jsonObjectException.addProperty("error", true);
            jsonObjectException.addProperty("detail", MSG_ERRO_REQUEST_CLIMATEMPO);
            return jsonObjectException;
        }
    }

    private static JsonObject getCityInfoByCityName(String cidade){
        JsonObject jsonObjectReturn = new JsonObject();

        try{
            String url = new StringBuilder()
                    .append(URL_CLIMATEMPO)
                    .append("/locale/city?name=")
                    .append(cidade)
                    .append("&token=")
                    .append(CLIMATEMPO_USER_TOKEN).toString();

            ResponseBody responseBody = new OkHttpConnection().makeGetRequest(url);
            JsonArray jsonArray = JsonParser.parseString(responseBody.string()).getAsJsonArray();

            if(jsonArray.isEmpty()){
                jsonObjectReturn.addProperty("error", true);
                jsonObjectReturn.addProperty("detail", MSG_ERRO_CIDADE_NAO_ENCONTRADA);

            } else {
                for(JsonElement jsonElement : jsonArray){
                    if(cidade.equalsIgnoreCase(jsonElement.getAsJsonObject().get("name").getAsString())){
                        jsonObjectReturn = jsonElement.getAsJsonObject();
                        break;

                        // TODO
                        // Tratar quando houver mais de uma cidade com o mesmo nome

                    }
                }
            }
        } catch (IOException | IllegalStateException exception) {
            logger.error(MSG_ERRO_REQUEST_CLIMATEMPO, exception);
            jsonObjectReturn.addProperty("error", true);
            jsonObjectReturn.addProperty("detail", MSG_ERRO_REQUEST_CLIMATEMPO);

        } finally {
            return jsonObjectReturn;
        }
    }

    private static String parseClimaTempoJson(JsonObject jsonObjectResponse) {
        JsonObject jsonObject;
        String strRetorno = "";

        for(int i=0; i < FORECAST_QTDE_DIAS; i++){
            jsonObject = jsonObjectResponse.get("data").getAsJsonArray().get(i).getAsJsonObject();
            strRetorno += new StringBuilder()
                    .append("Data: ")
                    .append(jsonObject.get("date_br").getAsString())
                    .append(System.getProperty("line.separator"))
                    .append("Temperatura: ")
                    .append(jsonObject.get("temperature").getAsJsonObject().get("min").getAsString()).append("ºC")
                    .append(" - ")
                    .append(jsonObject.get("temperature").getAsJsonObject().get("max").getAsString()).append("ºC")
                    .append(System.getProperty("line.separator"))
                    .append("Chuva: ")
                    .append(jsonObject.get("rain").getAsJsonObject().get("precipitation").getAsString()).append("mm")
                    .append(" - ")
                    .append(jsonObject.get("rain").getAsJsonObject().get("probability").getAsString()).append("%")
                    .append(System.getProperty("line.separator"))
                    .append("Umidade: ")
                    .append(jsonObject.get("humidity").getAsJsonObject().get("min").getAsString()).append("%")
                    .append(" - ")
                    .append(jsonObject.get("humidity").getAsJsonObject().get("max").getAsString()).append("%")
                    .append(System.getProperty("line.separator"))
                    .append("'")
                    .append(jsonObject.get("text_icon").getAsJsonObject().get("text").getAsJsonObject().get("pt").getAsString())
                    .append("'")
                    .append(System.getProperty("line.separator"))
                    .append(System.getProperty("line.separator"))
                    .toString();
        }

        return strRetorno;
    }

}
