package br.com.higa.bot;

import br.com.higa.bot.enums.OpcoesBot;
import br.com.higa.bot.service.ClimaTempo;
import br.com.higa.bot.service.ViaCep;
import br.com.higa.bot.utils.Constants;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

import static br.com.higa.bot.utils.Constants.TELEGRAM_BOT_TOKEN;

public class Principal {
	// Subida do bot
	static TelegramBot bot = new TelegramBot(TELEGRAM_BOT_TOKEN);
	// Recebimento de mensagens
	static GetUpdatesResponse updatesResponse;
	// Gerenciamento de acoes do chat
	static BaseResponse baseResponse;
	// Envio de mensagens
	static SendResponse sendResponse;
	// contador de off-set (paginacao)
	static int offSet = 0;
	// Definicao do limite do tamanho do off-set (paginacao)
	static final int OFF_SET_LIMIT = 50;
	// ID da mensagem recebida
	static Object msgRecebidaId = null;
	// Texto da mensagem recebida
	static String msgRecebidaTxt = "";

	public static void main(String[] args) {
		System.out.println("### BOT INICIADO ###");

		while (true){
			updatesResponse =  bot.execute(new GetUpdates().limit(OFF_SET_LIMIT).offset(offSet));
			List<Update> mensagensRecebidas = updatesResponse.updates();

			for (Update mensagemRecebida : mensagensRecebidas) {
				offSet = mensagemRecebida.updateId() + 1;
				msgRecebidaId = mensagemRecebida.message().chat().id();
				msgRecebidaTxt = mensagemRecebida.message().text();

				if(msgRecebidaTxt.startsWith(OpcoesBot.START.getNome())){
					baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, getTodasOpcoesBot()));

				} else if(msgRecebidaTxt.startsWith(OpcoesBot.CEP.getNome())){
					baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
					String resposta = ViaCep.consultarCep(msgRecebidaTxt);
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, resposta));

				} else if(msgRecebidaTxt.startsWith(OpcoesBot.TEMPO.getNome())){
					baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
					String resposta = ClimaTempo.executarAcaoPrevisaoTempo(msgRecebidaTxt);
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, resposta));

				} else {
					baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, Constants.MSG_ERRO_OPCAO_INVALIDA));
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, getTodasOpcoesBot()));
				}
			}
		}
	}

	static String getTodasOpcoesBot(){
		StringBuilder stringBuilder = new StringBuilder();

		for(OpcoesBot opcoes : OpcoesBot.values()){
			stringBuilder
					.append(opcoes.getDescServico())
					.append(System.getProperty("line.separator"))
					.append(System.getProperty("line.separator"));
		}

		return stringBuilder.toString();
	}

	static SendChatAction criarAcao(Object msgRecebidaId, String acao){
		return new SendChatAction(msgRecebidaId, acao);
	}

	static SendMessage criaMsgDeResposta(Object msgRecebidaId, String resposta){ return new SendMessage(msgRecebidaId, resposta); }

}
