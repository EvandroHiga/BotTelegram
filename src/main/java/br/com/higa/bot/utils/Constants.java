package br.com.higa.bot.utils;

public class Constants {
    public static final String TELEGRAM_BOT_TOKEN = "<TELEGRAM_BOT_TOKEN>";
    public static final String CLIMATEMPO_USER_TOKEN = "<CLIMATEMPO_USER_TOKEN>";

    public static final String URL_VIA_CEP = "https://viacep.com.br/ws/";
    public static final String URL_CLIMATEMPO = "http://apiadvisor.climatempo.com.br/api/v1";

    public static final int FORECAST_QTDE_DIAS = 5;

    public static final String MSG_ERRO_OPCAO_INVALIDA = "Nao entendi, temos apenas as opcoes abaixo. Tente uma delas.";
    public static final String MSG_ERRO_CEP_INVALIDO = "CEP invalido. Envie com ou sem traco (hifen).";
    public static final String MSG_ERRO_CEP_NAO_ENCONTRADO = "CEP nao encontrado.";
    public static final String MSG_ERRO_CIDADE_NAO_ENCONTRADA = "Cidade nao encontrada.";
    public static final String MSG_ERRO_REQUEST_VIA_CEP = "Erro no servico ViaCep. Tente novamente mais tarde.";
    public static final String MSG_ERRO_REQUEST_CLIMATEMPO = "Erro no servico Climatempo. Tente novamente mais tarde.";
}
