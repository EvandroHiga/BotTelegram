package br.com.higa.bot;

import lombok.Getter;

@Getter
public enum OpcoesBot {
    CEP("/cep", "'/cep <cep_desejado>': Retorna informacoes sobre o CEP consultado. Ex.: /cep 00000-000 ou /cep 00000000");

    private final String nomeServico;
    private final String descricaoServico;

    OpcoesBot(String nomeServico, String descricaoServico){
        this.nomeServico = nomeServico;
        this.descricaoServico = descricaoServico;
    }
}
