package model;

import exceptions.EntradaInvalidaException;

import java.time.LocalDate;

public class Transacao {

    public static final int TAMANHO_MAXIMO_DESCRICAO = 100; 

    private int id; 
    private String descricao;
    private double valor;
    private boolean ehReceita;
    private LocalDate data;

    public Transacao(String descricao, double valor, boolean ehReceita, LocalDate data) {
        this(0, descricao, valor, ehReceita, data);
    }

    public Transacao(int id, String descricao, double valor, boolean ehReceita, LocalDate data) {
        this.id = id;
        setDescricao(descricao);
        setValor(valor);
        this.ehReceita = ehReceita;
        setData(data);
    }

    public double getValorComSinal() {
        return ehReceita ? valor : -valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new EntradaInvalidaException("Informe a descrição da transação.");
        }
        if (descricao.trim().length() > TAMANHO_MAXIMO_DESCRICAO) {
            throw new EntradaInvalidaException(
                    "A descrição pode ter no máximo " + TAMANHO_MAXIMO_DESCRICAO + " caracteres.");
        }
        this.descricao = descricao.trim();
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        if (Double.isNaN(valor) || Double.isInfinite(valor) || valor <= 0) {
            throw new EntradaInvalidaException("O valor deve ser maior que zero.");
        }
        this.valor = valor;
    }

    public boolean isEhReceita() {
        return ehReceita;
    }

    public void setEhReceita(boolean ehReceita) {
        this.ehReceita = ehReceita;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new EntradaInvalidaException("Informe a data da transação.");
        }
        this.data = data;
    }

    @Override
    public String toString() {
        return data + " | " + descricao + " | " + (ehReceita ? "Receita" : "Despesa") + " | " + valor;
    }
}
