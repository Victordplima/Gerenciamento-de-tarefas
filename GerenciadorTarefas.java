import java.util.Scanner;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GerenciadorTarefas {
  public String nomeUsuario;
  public String categoria;

  public GerenciadorTarefas() {
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getCategoria() {
    return categoria;
  }

  public void adicionarTarefa(Tarefa t) {
    // Diretório em que será salva a tarefa:
    String caminhoDiretorio = "Usuarios/" + nomeUsuario + "/" + categoria;
    String caminhoArquivo = caminhoDiretorio + "/tarefasPendentes.txt";

    try {
      // Criar o diretório da categoria, se não existir
      Files.createDirectories(Paths.get(caminhoDiretorio));

      FileWriter fw = new FileWriter(caminhoArquivo, true);
      BufferedWriter bw = new BufferedWriter(fw);
      bw.write(t.toString());
      bw.newLine();
      bw.close();
      fw.close();
    } catch (IOException e) {
      System.out.println("Erro ao salvar tarefa no arquivo de texto.");
    }
  }

  public void criarTarefa(String titulo, String descricao, String categoria) {
    LocalDate dataCriacao = LocalDate.now();
    Tarefa novaTarefa = new Tarefa(titulo, descricao, dataCriacao, false, categoria);
    adicionarTarefa(novaTarefa);

    // Criar diretório da categoria, se não existir
    String caminhoUsuario = "Usuarios/" + nomeUsuario;
    String caminhoCategoria = caminhoUsuario + "/" + categoria;
    Path diretorioCategoria = Paths.get(caminhoCategoria);

    try {
      Files.createDirectories(diretorioCategoria);
      System.out.println("Diretório da categoria criado com sucesso.");
    } catch (IOException e) {
      System.out.println("Erro ao criar o diretório da categoria: " + e.getMessage());
    }
  }

  public boolean concluirTarefa(String tituloProcurado) {
    String caminhoDiretorio = "Usuarios/" + nomeUsuario + "/" + categoria;
    String caminhoPendentes = caminhoDiretorio + "/tarefasPendentes.txt";
    String caminhoConcluidas = caminhoDiretorio + "/tarefasConcluidas.txt";
    boolean tarefaEncontrada = false;
    boolean tarefaConcluida = false;
    try (Scanner scanner = new Scanner(new FileReader(caminhoPendentes));
        BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoConcluidas, true))) {
      while (scanner.hasNextLine()) {
        String linha = scanner.nextLine();
        if (linha.contains(tituloProcurado)) {
          tarefaEncontrada = true;
          // Marcar a tarefa como concluída.
          linha = linha.replace("Pendente", "Concluida");
          tarefaConcluida = true;
        }
        writer.write(linha + "\n");
      }
    } catch (IOException e) {
      // Tratamento da exceção
      System.out.println("Erro ao concluir a tarefa: " + e.getMessage());
      return false;
    }
    if (tarefaEncontrada && tarefaConcluida) {
      // Remover a tarefa concluída do arquivo de tarefas pendentes.
      try {
        removerLinha(caminhoPendentes, tituloProcurado);
      } catch (IOException e) {
        // Tratamento da exceção
        System.out.println("Erro ao remover a linha: " + e.getMessage());
        return false;
      }
      return true;
    } else {
      return false;
    }
  }

  public void removerLinha(String caminhoArquivo, String linhaRemover) throws IOException {
    File arquivoTemporario = new File(caminhoArquivo + ".temp");
    BufferedReader reader = null;
    BufferedWriter writer = null;
    try {
      reader = new BufferedReader(new FileReader(caminhoArquivo));
      writer = new BufferedWriter(new FileWriter(arquivoTemporario));

      String linha;
      while ((linha = reader.readLine()) != null) {
        if (!linha.startsWith(linhaRemover)) {
          writer.write(linha);
          writer.newLine();
        }
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
      if (writer != null) {
        writer.close();
      }
    }

    Files.move(arquivoTemporario.toPath(), new File(caminhoArquivo).toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  public void exibirTarefasPendentes() {
    String caminhoUsuario = "Usuarios/" + nomeUsuario;
    File diretorioUsuario = new File(caminhoUsuario);

    // Verificar se o diretório do usuário existe
    if (!diretorioUsuario.exists() || !diretorioUsuario.isDirectory()) {
      System.out.println("Diretório do usuário não encontrado.");
      return;
    }

    // Listar todas as categorias dentro do diretório do usuário
    File[] categorias = diretorioUsuario.listFiles();

    // Iterar sobre as categorias
    for (File categoria : categorias) {
      if (categoria.isDirectory()) {
        File diretorioCategoria = new File(categoria.getPath());
        File[] tarefasPendentes = diretorioCategoria.listFiles((dir, nome) -> nome.equals("tarefasPendentes.txt"));

        if (tarefasPendentes != null && tarefasPendentes.length > 0) {
          try (BufferedReader reader = new BufferedReader(new FileReader(tarefasPendentes[0]))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
              System.out.println(linha);
            }
          } catch (FileNotFoundException e) {
            System.out.println("Arquivo de tarefas pendentes não encontrado para a categoria " + categoria.getName());
          } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de tarefas pendentes: " + e.getMessage());
          }
        } else {
          System.out.println("Arquivo de tarefas pendentes não encontrado para a categoria " + categoria.getName());
        }
      }
    }
  }

  public void exibirTarefasConcluidas() {
    String caminhoUsuario = "Usuarios/" + nomeUsuario;
    File diretorioUsuario = new File(caminhoUsuario);

    if (!diretorioUsuario.exists() || !diretorioUsuario.isDirectory()) {
      System.out.println("Diretório do usuário não encontrado.");
      return;
    }

    File[] categorias = diretorioUsuario.listFiles();

    for (File categoria : categorias) {
      if (categoria.isDirectory()) {
        String caminhoArquivo = categoria.getPath() + "/tarefasConcluidas.txt";
        try {
          File arquivo = new File(caminhoArquivo);
          Scanner scanner = new Scanner(arquivo);
          System.out.println("Categoria: " + categoria.getName());
          while (scanner.hasNextLine()) {
            String linha = scanner.nextLine();
            System.out.println(linha);
          }
          scanner.close();
        } catch (FileNotFoundException e) {
          System.out.println("Arquivo de tarefas concluídas não encontrado para a categoria " + categoria.getName());
        }
      }
    }
  }
}
