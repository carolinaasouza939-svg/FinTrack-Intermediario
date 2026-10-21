package br.com.capacita.fintrack.app;

import br.com.capacita.fintrack.dao.ConexaoPostgres;
import br.com.capacita.fintrack.dao.TransacaoDAO;
import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.repository.Repositorio;
import br.com.capacita.fintrack.repository.RepositorioGenerico;

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
