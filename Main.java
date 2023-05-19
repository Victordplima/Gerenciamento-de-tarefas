import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    String origem = "tarefasPendentes.txt";
    String destino = "tarefasConcluidas.txt";
    ArrayList<Tarefa> listaTarefas = new ArrayList<Tarefa>();
    GerenciadorTarefas gerenciador = new GerenciadorTarefas();
    Scanner scanner = new Scanner(System.in);
    LocalDate dataCriacao;

    // Realizar Login
    GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
    GerenciadorTarefas gerenciadorTarefas;
    gerenciadorUsuarios.criarPastaUsuarios();

    boolean loginSucesso = gerenciadorUsuarios.realizarLogin();

    if (loginSucesso) {
      String nomeUsuario = gerenciadorUsuarios.getNomeUsuario(); // Obtém o nome do usuário
      gerenciadorTarefas = new GerenciadorTarefas(nomeUsuario); // Cria o objeto GerenciadorTarefas com o nome do usuário

      while (true) {
        System.out.println("==========Sistema de Gerenciamento de Tarefas==========");
        System.out.println("Escolha uma opcao:");
        System.out.println("1. Criar nova tarefa");
        System.out.println("2. Concluir tarefa");
        System.out.println("3. Exibir tarefas pendentes");
        System.out.println("4. Exibir tarefas concluidas");
        System.out.println("5. Sair");
        System.out.println("=======================================================");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
          case 1:
            System.out.println("Digite o titulo da nova tarefa:");
            String titulo = scanner.nextLine();
            System.out.println("Digite a descricao da nova tarefa:");
            String descricao = scanner.nextLine();
            gerenciadorTarefas.criarTarefa(titulo, descricao);
            break;

          case 2:
            System.out.println("Selecione a tarefa que deseja concluir:");
            gerenciadorTarefas.exibirTarefasPendentes();
            System.out.println("Digite o título da tarefa que deseja concluir:");
            String tituloProcurado = scanner.nextLine();

            try {
              boolean tarefaConcluidaComSucesso = gerenciadorTarefas.concluirTarefa(tituloProcurado);

              if (tarefaConcluidaComSucesso) {
                System.out.println("Tarefa concluída com sucesso: " + tituloProcurado);
              } else {
                System.out.println("Tarefa não encontrada ou ocorreu um erro ao concluí-la.");
              }
            } catch (IOException e) {
              System.out.println("Ocorreu um erro ao concluir a tarefa: " + e.getMessage());
            }
            break;

          case 3:
            System.out.println("Tarefas pendentes:");
            gerenciadorTarefas.exibirTarefasPendentes();
            break;

          case 4:
            System.out.println("Tarefas concluídas:");
            gerenciadorTarefas.exibirTarefasConcluidas();
            break;

          case 5:
            System.out.println("Saindo...");
            scanner.close();
            System.exit(0);

          default:
            System.out.println("Opção inválida.");
        }
      }
    } else {
      System.out.println("Falha no login. Encerrando o programa.");
      scanner.close();
      System.exit(0);
    }
  }
}
