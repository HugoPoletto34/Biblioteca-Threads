import java.io.Serializable;

public class Livro implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer codigo;
  private String titulo;
  private boolean emprestado;

  public Livro(Integer codigo, String titulo) {
    this.codigo = codigo;
    this.titulo = titulo;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public void setEmprestado(boolean emprestado) {
    this.emprestado = emprestado;
  }

  public boolean isEmprestado() {
    return emprestado;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("LIVRO\n");
    str.append("- Título: ").append(this.titulo);
    str.append("\n- Código: ").append(this.codigo);
    return str.toString();
  }
}