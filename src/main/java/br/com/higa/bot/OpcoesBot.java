package br.com.higa.bot;

import lombok.Getter;

@Getter
public enum OpcoesBot {
    CEP("/cep", "'/cep <cep_desejado>': Retorna informacoes sobre o CEP consultado." + System.lineSeparator() + "Ex.: /cep 00000-000 ou /cep 00000000"),
    RUA("/rua", "'/rua <uf>,<cidade>,<logradouro>': Retorna informacoes sobre o logradouro consultado." + System.lineSeparator() + "Ex.: /rua sp,bora,primavera");

    private final String nomeServico;
    private final String descricaoServico;

    OpcoesBot(String nomeServico, String descricaoServico){
        this.nomeServico = nomeServico;
        this.descricaoServico = descricaoServico;
    }
}
