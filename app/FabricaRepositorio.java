package app;

import dao.ConexaoPostgres;
import dao.TransacaoDAO;
import exceptions.RepositorioException;
import model.Transacao;
import repository.Repositorio;
import repository.RepositorioGenerico;

import java.util.function.Consumer;

public final class FabricaRepositorio {

    private FabricaRepositorio() {
    }

    public static Repositorio<Transacao> criar(Consumer<String> avisar) {
        TransacaoDAO dao = new TransacaoDAO(ConexaoPostgres::conectar);

        try {
            dao.listar();
            return dao;
        } catch (RepositorioException e) {
            avisar.accept("Sem acesso ao banco de dados: " + e.detalhe()
                    + "\n\nO FinTrack vai rodar em memória. Nada será gravado.");
            return new RepositorioGenerico<>();
        }
    }
}
