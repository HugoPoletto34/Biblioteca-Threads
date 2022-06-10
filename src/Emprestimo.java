import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe Empréstimo para controle dos pedidos.
 */
public class Emprestimo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DIAS_EMPRESTIMO = 7;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    private Cliente cliente;

    public Emprestimo(LocalDate data) {
        this.livro = null;
        this.dataEmprestimo = data;
        this.dataDevolucao = data.plus(DIAS_EMPRESTIMO, ChronoUnit.DAYS);
    }

    public Emprestimo(Cliente cliente, LocalDate data, Livro livro) {
        this.livro = livro;
        this.cliente = cliente;
        livro.setEmprestado(true);
        this.dataEmprestimo = data;
        this.dataDevolucao = data.plus(DIAS_EMPRESTIMO, ChronoUnit.DAYS);
    }

    public Livro getLivro() {
        return livro;
    }

    public void renovarEmprestimo() {
        this.dataDevolucao = this.dataEmprestimo.plus(DIAS_EMPRESTIMO, ChronoUnit.DAYS);;
    }

    public Livro devolucao(LocalDate data) {
        this.cliente.getEmprestimos().remove(this);
        this.dataDevolucao = data;
        this.livro.setEmprestado(false);
        return this.livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("EMPRESTIMO\n");
        str.append("- Data de Empréstimo: ").append(this.dataEmprestimo.toString());
        str.append("\n- Data de Devolução: ").append(this.dataDevolucao.toString());
        str.append("\n- Empréstimo de ").append(this.livro);
        return str.toString();
    }

    public void setDataDevolucao(LocalDate data) {
        this.dataDevolucao = data;
    }
}
