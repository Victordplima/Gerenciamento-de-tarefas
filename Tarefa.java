import java.time.LocalDate;

public class Tarefa {
  private String titulo;
  private String descricao;
  private LocalDate dataCriacao;
  private LocalDate dataConclusao;
  private boolean concluida;
  private String categoria;

  public Tarefa(String titulo, String descricao, LocalDate dataCriacao, boolean concluida, String categoria) {
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataCriacao = dataCriacao;
    this.concluida = concluida;
    this.categoria = categoria;
}

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public LocalDate getDataCriacao() {
    return dataCriacao;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public boolean isConcluida() {
    return concluida;
  }

  public void setConcluida(boolean concluida) {
    this.concluida = concluida;
  }

  public String getCategoria() {
    return categoria;
  }

  @Override
  public String toString() {
    String status = isConcluida() ? "Concluída em " + getDataConclusao() + " Concluida" : "Pendente";
    return getTitulo() + " - " + getDescricao() + " (" + getDataCriacao() + ") - " + status;
  }
}
