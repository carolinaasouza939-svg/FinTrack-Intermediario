package br.com.capacita.fintrack.exceptions;

public class RepositorioException extends RuntimeException {

    public RepositorioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public String detalhe() {
        return getCause() != null && getCause().getMessage() != null ? getCause().getMessage() : "";
    }
}
