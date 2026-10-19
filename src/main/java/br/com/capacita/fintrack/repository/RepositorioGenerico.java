package br.com.capacita.fintrack.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class RepositorioGenerico<T> implements Repositorio<T> {

    private final List<T> itens = new ArrayList<>();

    @Override
    public void adicionar(T item) {
        itens.add(Objects.requireNonNull(item, "O item não pode ser nulo."));
    }

    @Override
    public boolean atualizar(T item) {
        return itens.contains(item);
    }

    @Override
    public boolean remover(T item) {
        return itens.remove(item);
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(itens);
    }

    public void adicionarTodos(Collection<? extends T> novos) {
        itens.addAll(novos);
    }

    public void copiarPara(Collection<? super T> destino) {
        destino.addAll(itens);
    }

    public List<T> buscar(Predicate<? super T> filtro) {
        List<T> encontrados = new ArrayList<>();
        for (T item : itens) {
            if (filtro.test(item)) {
                encontrados.add(item);
            }
        }
        return encontrados;
    }

    public int tamanho() {
        return itens.size();
    }
}
