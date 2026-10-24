package br.com.capacita.fintrack.app;

import br.com.capacita.fintrack.controller.FinTracker;
import br.com.capacita.fintrack.exceptions.EntradaInvalidaException;
import br.com.capacita.fintrack.exceptions.RepositorioException;
import br.com.capacita.fintrack.model.Transacao;
import br.com.capacita.fintrack.utils.Formatador;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/** Versão console (a do módulo anterior), agora usando o mesmo banco da versão com tela. */
public class Main {

    private static final Scanner TECLADO = new Scanner(System.in);

    public static void main(String[] args) {
        FinTracker finTracker = new FinTracker(FabricaRepositorio.criar(System.out::println));
        int opcao;

        do {
            exibirMenu();
            opcao = lerNumero("Escolha uma opcao: ");

            try {
                switch (opcao) {
                    case 1 -> adicionar(finTracker);
                    case 2 -> listar(finTracker);
                    case 3 -> System.out.println("Saldo atual: " + Formatador.moeda(finTracker.calcularSaldoTotal()));
                    case 4 -> remover(finTracker);
                    case 5 -> System.out.println("Ate logo!");
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (EntradaInvalidaException e) {
                System.out.println("Erro de validacao: " + e.getMessage());
            } catch (RepositorioException e) {
                System.out.println("Erro no banco: " + e.getMessage() + " " + e.detalhe());
            }
        } while (opcao != 5);
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("===== FINTRACK - SEU CONTROLE FINANCEIRO =====");
        System.out.println("1. Adicionar nova transacao");
        System.out.println("2. Listar transacoes");
        System.out.println("3. Mostrar saldo atual");
        System.out.println("4. Remover transacao");
        System.out.println("5. Sair");
    }

    private static void adicionar(FinTracker finTracker) {
        System.out.print("Descricao: ");
        String descricao = TECLADO.nextLine();
        double valor = lerValor("Valor (ex.: 150,50): ");
        boolean ehReceita = lerNumero("Tipo (1 = receita, 2 = despesa): ") == 1;

        // O próprio Transacao valida descrição e valor e lança EntradaInvalidaException.
        finTracker.adicionarTransacao(new Transacao(descricao, valor, ehReceita, LocalDate.now()));
        System.out.println("Transacao adicionada.");
    }

    private static void listar(FinTracker finTracker) {
        List<Transacao> transacoes = finTracker.listarTransacoes();

        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transacao registrada.");
            return;
        }

        for (int i = 0; i < transacoes.size(); i++) {
            Transacao t = transacoes.get(i);
            System.out.printf("%d | %s | %-20s | %-8s | %s%n", i + 1,
                    Formatador.data(t.getData()), t.getDescricao(),
                    Formatador.tipo(t.isEhReceita()), Formatador.moeda(t.getValor()));
        }
    }

    private static void remover(FinTracker finTracker) {
        List<Transacao> transacoes = finTracker.listarTransacoes();

        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transacao registrada.");
            return;
        }

        listar(finTracker);
        int numero = lerNumero("Numero da transacao a remover: ");

        if (numero < 1 || numero > transacoes.size()) {
            System.out.println("Numero fora da lista.");
            return;
        }

        finTracker.removerTransacao(transacoes.get(numero - 1));
        System.out.println("Transacao removida.");
    }

    private static int lerNumero(String pergunta) {
        System.out.print(pergunta);
        try {
            return Integer.parseInt(TECLADO.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double lerValor(String pergunta) {
        System.out.print(pergunta);
        try {
            return Double.parseDouble(TECLADO.nextLine().trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0; 
        }
    }
}
