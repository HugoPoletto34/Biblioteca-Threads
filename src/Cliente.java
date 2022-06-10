import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Cliente implements Serializable {
  private static final long serialVersionUID = 1L;
  private Integer matricula;
  private String nome;
  private List<Emprestimo> emprestimos;
  
  public Cliente(Integer matricula, String nome) {
    this.matricula = matricula;
    this.nome = nome;
    this.emprestimos = new LinkedList<>();
  }



  public void addEmprestimo(Emprestimo emprestimo) {
    this.emprestimos.add(emprestimo);
  }

  public List<Emprestimo> getEmprestimos() {
    return emprestimos;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder("CLIENTE\n");
    str.append("- Nome: ").append(this.nome);
    str.append("\n- Matrícula: ").append(this.matricula);
    str.append("\n- Realizou ").append(this.emprestimos.size()).append(" empréstimos: \n");
    for (Emprestimo emprestimo : this.emprestimos) {
      str.append("     ");
      str.append(emprestimo);
      str.append("\n");
    }
    return str.toString();
  }

    public void emprestarLivro(Emprestimo emprestimo) {
        this.emprestimos.add(emprestimo);
    }

    public Livro devolverLivro(Emprestimo emprestimo, LocalDate data) {
        this.emprestimos.remove(emprestimo);
        return emprestimo.devolucao(data);
    }
}