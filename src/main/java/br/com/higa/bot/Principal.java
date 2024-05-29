package br.com.higa.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;
import java.util.logging.Logger;

import static br.com.higa.bot.Constants.TELEGRAM_BOT_TOKEN;

public class Principal {
	static Logger log = Logger.getLogger(Principal.class.getName());

	// Subida do bot
	static TelegramBot bot = new TelegramBot(TELEGRAM_BOT_TOKEN);
	// Gerenciamento de acoes do chat
	static BaseResponse baseResponse;
	// Envio de mensagens
	static SendResponse sendResponse;
	// ID da mensagem recebida
	static Object msgRecebidaId = null;
	// Texto da mensagem recebida
	static String msgRecebidaTxt = "";
	// Definicao do limite de tamanho do off-set (paginacao)
	static final int OFF_SET_LIMIT = 50;
	// contador de off-set (paginacao)
	static int offSet = 0;

	public static void main(String[] args) {
		log.info("### BOT INICIADO :-) ###");
		sendResponse = bot.execute(new SendMessage(msgRecebidaId, getDescricaoTodosServicos()));

		while (true){
			List<Update> mensagensRecebidas =
					bot.execute(new GetUpdates().limit(OFF_SET_LIMIT).offset(offSet)).updates();

			for (Update mensagemRecebida : mensagensRecebidas) {
				offSet = mensagemRecebida.updateId() + 1;
				msgRecebidaId = mensagemRecebida.message().chat().id();
				msgRecebidaTxt = mensagemRecebida.message().text();

				if(msgRecebidaTxt.startsWith(OpcoesBot.CEP.getNomeServico().toLowerCase())){
					baseResponse = bot.execute(new SendChatAction(msgRecebidaId, ChatAction.typing.name()));
					final String responseViaCep = ViaCep.consultarCep(msgRecebidaTxt);
					sendResponse = bot.execute(new SendMessage(msgRecebidaId, responseViaCep));
				}



				 else  {
					baseResponse = bot.execute(new SendChatAction(msgRecebidaId, ChatAction.typing.name()));
					sendResponse = bot.execute(new SendMessage(msgRecebidaId, Constants.MSG_ERRO_OPCAO_INVALIDA));
					sendResponse = bot.execute(new SendMessage(msgRecebidaId, getDescricaoTodosServicos()));
				}
			}
		}
	}

	static String getDescricaoTodosServicos(){
		StringBuilder s = new StringBuilder();
		for(OpcoesBot opcoesBot : OpcoesBot.values()){
			s.append(opcoesBot.getDescricaoServico()).append(System.lineSeparator()).append(System.lineSeparator());
		}
		return s.toString();
	}
}
