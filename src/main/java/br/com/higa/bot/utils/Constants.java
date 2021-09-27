package br.com.higa.bot.utils;

public class Constants {
    public static final String BOT_TOKEN = "<TELEGRAM_BOT_TOKEN_AQUI>";
    public static final String BOT_START = "/start";

    public static final String ITAU = "Itau";
    public static final String BRADESCO = "Bradesco";
    public static final String BB = "Banco do Brasil";
    public static final String SANTANDER = "Santander";
    public static final String CAIXA = "Caixa Economica Fed";

    public static final String BOLETO_INSTRUCOES = "Instrucoes (Todas as informacoes sao de plena responsabilidade do cliente).";
    public static final String BOLETO_LOCAIS_PAG = "Pagavel em qualquer banco ate o vencimento.";

    public static final String URL_VIA_CEP = "https://viacep.com.br/ws/";
    public static final String JSON_RESPONSE_VIA_CEP = "/json/";

    public static final String MSG_ERRO_OPCAO_INVALIDA = "Nao entendi, temos apenas as opcoes abaixo. Tente uma delas.";
    public static final String MSG_ERRO_CEP_INVALIDO = "CEP invalido. Envie com ou sem traco.";
    public static final String MSG_ERRO_CEP_NAO_ENCONTRADO = "CEP nao encontrado.";
    public static final String MSG_ERRO_VIA_CEP_REQUEST = "Erro no servico ViaCep. Tente novamente mais tarde.";
}
