package br.com.higa.bot.service;

import br.com.caelum.stella.boleto.*;
import br.com.caelum.stella.boleto.bancos.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.higa.bot.utils.Constants.*;

public class GeradorDeBoletos {
    private static final Integer MOEDA = 9;

    private String nomeBanco;
	private LocalDate vencimentoBoleto;
	
	public LocalDate getVencimentoBoleto() {
		return vencimentoBoleto;
	}

    public GeradorDeBoletos(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public Boleto getBoleto() {

        LocalDate vencimentoBoleto = LocalDate.now().plusDays(15);
        LocalDate dataDocumentoBoleto = LocalDate.now();
        
        this.vencimentoBoleto = vencimentoBoleto;

        Datas datas = getDatas(vencimentoBoleto, dataDocumentoBoleto);

        Endereco enderecoBeneficiario = Endereco.novoEndereco()
                .comLogradouro("Rua dos devs")
                .comBairro("Jardim Java")
                .comCep("07112-000")
                .comCidade("Sao Paulo")
                .comUf("SP");
        
        Beneficiario beneficiario = getBeneficiario(enderecoBeneficiario);

        Endereco enderecoPagador = Endereco.novoEndereco()
                .comLogradouro("Rua primavera")
                .comBairro("Liberdade")
                .comCep("07112-001")
                .comCidade("Rio de Janeiro")
                .comUf("RJ");

        Pagador pagador = Pagador.novoPagador()
                .comNome("Alex Oliveira")
                .comDocumento("378.689.699-88")
                .comEndereco(enderecoPagador);

        Banco banco = getBanco();

        return Boleto.novoBoleto()
                .comBanco(banco)
                .comDatas(datas)
                .comBeneficiario(beneficiario)
                .comPagador(pagador)
                .comCodigoEspecieMoeda(MOEDA)
                .comAceite(false)
                .comQuantidadeMoeda(BigDecimal.ZERO)
                .comValorBoleto(10559)
                .comNumeroDoDocumento("123")
                .comInstrucoes(BOLETO_INSTRUCOES)
                .comLocaisDePagamento(BOLETO_LOCAIS_PAG)
                .comEspecieDocumento("DM");
    }

    private Datas getDatas(LocalDate vencimentoBoleto, LocalDate dataDocumentoBoleto) {
        Datas datas = Datas.novasDatas()
                .comDocumento(dataDocumentoBoleto.getDayOfMonth(), dataDocumentoBoleto.getMonthValue(), dataDocumentoBoleto.getYear())
                .comProcessamento(LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(), LocalDate.now().getYear())
                .comVencimento(vencimentoBoleto.getDayOfMonth(), vencimentoBoleto.getMonthValue(), vencimentoBoleto.getYear());
        return datas;
    }

    private Banco getBanco() {
        if (nomeBanco.equalsIgnoreCase(ITAU)) {
            return new Itau();

        } else if (nomeBanco.equalsIgnoreCase(BB)) {
            return new BancoDoBrasil();

        } else if (nomeBanco.equalsIgnoreCase(SANTANDER)) {
            return new Santander();

        } else if (nomeBanco.equalsIgnoreCase(BRADESCO)) {
            return new Bradesco();

        } else if (nomeBanco.equalsIgnoreCase(CAIXA)) {
            return new Caixa();
        }

        throw new IllegalArgumentException("Banco " + nomeBanco + " nao cadastrado.");
    }

    private Beneficiario getBeneficiario(Endereco enderecoBeneficiario) {
        if (nomeBanco.equalsIgnoreCase(ITAU)) {
            return Beneficiario.novoBeneficiario()
                    .comNomeBeneficiario("Devs SA")
                    .comAgencia("3217")
                    .comCodigoBeneficiario("22673")
                    .comDigitoCodigoBeneficiario("1")
                    .comCarteira("112")
                    .comEndereco(enderecoBeneficiario)
                    .comNossoNumero("123456")
                    .comDigitoNossoNumero("4")
                    .comDocumento("15");
        } else {
            return Beneficiario.novoBeneficiario()
                    .comNomeBeneficiario("Devs SA")
                    .comAgencia("1635")
                    .comCodigoBeneficiario("09387495")
                    .comDigitoCodigoBeneficiario("4")
                    .comCarteira("104")
                    .comEndereco(enderecoBeneficiario)
                    .comNossoNumero("123456")
                    .comDigitoNossoNumero("4")
                    .comDocumento("15");
        }
    }
}
