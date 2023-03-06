package br.com.higa.bot.enums;

public enum OpcoesBot {
    START("/start", "'/start': Inicia o bot."),
    CEP("/cep", "'/cep <cep_desejado>': Informacoes sobre o CEP consultado providas por viacep.com.br."),
    TEMPO("/tempo", "'/tempo <cidade_desejada>': Previsao do tempo de hoje e dos proximos dias. Servico provido por www.climatempo.com.br.");

    private String nome;
    private String descServico;

    OpcoesBot(String nome, String descServico){
        this.nome = nome;
        this.descServico = descServico;
    }

    public String getNome(){
        return this.nome;
    }
    public String getDescServico(){ return this.descServico; }
}
