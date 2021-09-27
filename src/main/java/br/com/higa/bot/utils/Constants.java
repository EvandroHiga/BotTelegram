package br.com.higa.bot.utils;

public class Constants {
    public static final String BOT_TOKEN = "<SEU_TELEGRAM_TOKEN_AQUI>";
    public static final String BOT_START = "/start";

    public static final String URL_VIA_CEP = "https://viacep.com.br/ws/";
    public static final String JSON_RESPONSE_VIA_CEP = "/json/";

    public static final String MSG_ERRO_OPCAO_INVALIDA = "Nao entendi, temos as opcoes abaixo. Tente uma delas.ss ";
    public static final String MSG_ERRO_CEP_INVALIDO = "CEP invalido. Envie com ou sem traco.";
    public static final String MSG_ERRO_CEP_NAO_ENCONTRADO = "CEP nao encontrado.";
    public static final String MSG_ERRO_VIA_CEP_REQUEST = "Erro no servico ViaCep. Tente novamente mais tarde.";
}
