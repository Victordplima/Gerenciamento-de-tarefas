import java.util.Scanner;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;

public class GerenciadorTarefas {
  private List<Tarefa> tarefasPendentes;
  private List<Tarefa> tarefasConcluidas;
  private final String PENDENTES_FILE = "tarefasPendentes.txt";
  private final String CONCLUIDAS_FILE = "tarefasConcluidas.txt";
  private ArrayList<Tarefa> listaTarefas = new ArrayList<Tarefa>();
  private String nomeUsuario;

  public GerenciadorTarefas(String nomeUsuario) {
    this.tarefasPendentes = new ArrayList<>();
    this.tarefasConcluidas = new ArrayList<>();
    this.nomeUsuario = nomeUsuario;
  }

  public void adicionarTarefa(Tarefa t) {
    String caminhoArquivo = "Usuarios/" + nomeUsuario + "/tarefasPendentes.txt";

    try {
      FileWriter fw = new FileWriter(caminhoArquivo, true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(t.toString());
      bw.newLine();
      bw.close();
      fw.close();
    } catch (IOException e) {
      System.out.println("Erro ao salvar tarefa no arquivo de texto.");
    }

    tarefasPendentes.add(t);
  }

  public void criarTarefa(String titulo, String descricao) {
    LocalDate dataCriacao = LocalDate.now();
    Tarefa novaTarefa = new Tarefa(titulo, descricao, dataCriacao, false);
    adicionarTarefa(novaTarefa);
    System.out.println("Tarefa adicionada com sucesso.");
  }

  public boolean concluirTarefa(String tituloProcurado) throws IOException {
    String caminhoUsuario = "Usuarios/" + nomeUsuario;
    String caminhoPendentes = caminhoUsuario + "/tarefasPendentes.txt";
    String caminhoConcluidas = caminhoUsuario + "/tarefasConcluidas.txt";

    boolean tarefaEncontrada = false;
    boolean tarefaConcluida = false;

    try (Scanner scanner = new Scanner(new FileReader(caminhoPendentes));
        BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoConcluidas, true))) {

      while (scanner.hasNextLine()) {
        String linha = scanner.nextLine();

        if (linha.contains(tituloProcurado)) {
          tarefaEncontrada = true;

          // Marcar a tarefa como concluída.
          linha = linha.replace("pendente", "concluida");
          tarefaConcluida = true;
        }

        writer.write(linha + "\n");
      }
    }

    if (tarefaEncontrada && tarefaConcluida) {
      // Remover a tarefa concluída do arquivo de tarefas pendentes.
      removerLinha(caminhoPendentes, tituloProcurado);
      return true;
    } else {
      return false;
    }
  }

  public void removerLinha(String caminhoArquivo, String linhaRemover) throws IOException {
    File arquivoTemporario = new File(caminhoArquivo + ".temp");
    BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo));
    BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoTemporario));
    try {
      String linha;
      while ((linha = reader.readLine()) != null) {
        // Verificar se a linha contém o que estamos procurando.
        if (!linha.startsWith(linhaRemover)) {
          // A linha não é a linha que deve ser removida, copiá-la para o arquivo
          // temporário.
          if (linha != null) {
            writer.write(linha);
            writer.newLine();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      reader.close();
      writer.close();
    }
    Files.move(arquivoTemporario.toPath(), new File(caminhoArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  public void exibirTarefasPendentes() {
    String caminhoArquivo = "Usuarios/" + nomeUsuario + "/tarefasPendentes.txt";
    try {
      File arquivo = new File(caminhoArquivo);
      Scanner scanner = new Scanner(arquivo);
      while (scanner.hasNextLine()) {
        String linha = scanner.nextLine();
        System.out.println(linha);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não encontrado.");
    }
  }

  public void exibirTarefasConcluidas() {
    String caminhoArquivo = "Usuarios/" + nomeUsuario + "/tarefasConcluidas.txt";
    try {
      File arquivo = new File(caminhoArquivo);
      Scanner scanner = new Scanner(arquivo);
      while (scanner.hasNextLine()) {
        String linha = scanner.nextLine();
        System.out.println(linha);
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não encontrado.");
    }
  }
}