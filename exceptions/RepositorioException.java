package br.com.capacita.fintrack.exceptions;

/** Lançada quando a persistência falha (por exemplo, erro de SQL ou banco fora do ar). */
public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    /** Mensagem técnica da causa (ex.: a do driver JDBC), ou string vazia se não houver. */
    public String detalhe() {
        return getCause() != null && getCause().getMessage() != null ? getCause().getMessage() : "";
    }
}
