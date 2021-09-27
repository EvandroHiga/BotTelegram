package br.com.higa.bot.enums;

public enum OpcoesBot {
    CEP("/cep", "Envie '/cep <cep_desejado>' para receber as informacoes sobre o local consultado."),
    BOLETOS_EM_ABERTO("/boletos_em_aberto", "Retorna boletos em aberto");

    private String nome;
    private String descServico;

    OpcoesBot(String nome, String descServico){
        this.nome = nome;
        this.descServico = descServico;
    }

    public String getNome(){
        return this.nome;
    }

    public String getDescServico(){return this.descServico;}
}
