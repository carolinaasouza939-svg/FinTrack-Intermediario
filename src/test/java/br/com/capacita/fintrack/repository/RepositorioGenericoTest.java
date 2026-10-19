package br.com.capacita.fintrack.repository;

import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.model.TransacaoMensal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositorioGenericoTest {

    private RepositorioGenerico<Transacao> repositorio;
    private Transacao mercado;

    @BeforeEach
    void prepararRepositorio() {
        repositorio = new RepositorioGenerico<>();
        mercado = new Transacao("Mercado", 348.75, false, LocalDate.of(2026, 10, 8));
    }

    @Test
    void adicionarAumentaOTamanho() {
        repositorio.adicionar(mercado);

        assertEquals(1, repositorio.tamanho());
    }

    @Test
    void adicionarItemNuloLancaExcecao() {
        assertThrows(NullPointerException.class, () -> repositorio.adicionar(null));
    }

    @Test
    void removerItemExistenteDevolveTrue() {
        repositorio.adicionar(mercado);

        assertTrue(repositorio.remover(mercado));
        assertEquals(0, repositorio.tamanho());
    }

    @Test
    void removerItemInexistenteDevolveFalse() {
        assertFalse(repositorio.remover(mercado));
    }

    @Test
    void atualizarItemExistenteDevolveTrueEInexistenteFalse() {
        repositorio.adicionar(mercado);

        assertTrue(repositorio.atualizar(mercado));
        assertFalse(repositorio.atualizar(new Transacao("Outra", 1, false, LocalDate.of(2026, 10, 8))));
    }

    @Test
    void listarDevolveUmaCopiaEMudarACopiaNaoAlteraORepositorio() {
        repositorio.adicionar(mercado);

        List<Transacao> copia = repositorio.listar();
        copia.clear();

        assertEquals(1, repositorio.tamanho());
    }

    @Test
    void adicionarTodosAceitaListaDeSubclasse() { // ? extends T
        List<TransacaoMensal> fixas = new ArrayList<>();
        fixas.add(new TransacaoMensal("Internet", 99.90, false, LocalDate.of(2026, 10, 10), 10));

        repositorio.adicionarTodos(fixas);

        assertEquals(1, repositorio.tamanho());
    }

    @Test
    void copiarParaAceitaListaDeSuperclasse() { // ? super T
        repositorio.adicionar(mercado);
        List<Object> destino = new ArrayList<>();

        repositorio.copiarPara(destino);

        assertEquals(1, destino.size());
    }

    @Test
    void buscarFiltraPorPredicado() {
        repositorio.adicionar(mercado);
        repositorio.adicionar(new Transacao("Salário", 3200, true, LocalDate.of(2026, 10, 5)));

        List<Transacao> receitas = repositorio.buscar(Transacao::isEhReceita);

        assertEquals(1, receitas.size());
        assertEquals("Salário", receitas.get(0).getDescricao());
    }

    @Test
    void repositorioFuncionaComQualquerTipo() {
        RepositorioGenerico<String> textos = new RepositorioGenerico<>();
        textos.adicionar("a");
        textos.adicionar("b");

        assertEquals(List.of("a", "b"), textos.listar());
    }
}
