/** 
 * MIT License
 *
 * Copyright(c) 2022 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Produtor/consumidor com semáforo encapsulado na lista de dados
 */
public class ThreadEmprestimos extends Thread{

    static int ID = 0;
    private ListaSem<Livro> minhaListaLivros;
    private ListaSem<Emprestimo> minhaListaEmprestimos;
    private List<Cliente> listaClientes;
    private int quantRepeticoes;
    private int id;
    private int contErros;

    public ThreadEmprestimos(List<Cliente> listaClientes, ListaSem<Livro> listaLivrosDisponiveisSem, ListaSem<Emprestimo> minhaListaEmprestimos, int quant){
        this.minhaListaLivros = listaLivrosDisponiveisSem;
        this.minhaListaEmprestimos = minhaListaEmprestimos;
        this.listaClientes = listaClientes;
        this.quantRepeticoes = quant;
        this.id = 1;
        this.contErros=0;
    }

    @Override
    /**
     * Execução: um laço chamando a remoção da classe de dados (que encapsula a sincronização). Soma utilizada para conferência com o produtor.
     */
    public void run() {
        Random random = new Random(12);
        int qtdEmprestimos = 0;
        int qtdFaltaLivros = 0;
        for (int i = 0; i < this.quantRepeticoes; i++) {
            LocalDate dataEmprestimo = LocalDate.now().minus(random.nextInt(30), ChronoUnit.DAYS);
            try {
                Cliente cliente = listaClientes.get(random.nextInt(listaClientes.size()));
                Livro livroEmprestimo = minhaListaLivros.removeAt(random.nextInt(minhaListaLivros.getSize()));
                Emprestimo emprestimo = new Emprestimo(cliente, dataEmprestimo, livroEmprestimo);
                minhaListaEmprestimos.add(emprestimo);
                cliente.emprestarLivro(emprestimo);
                qtdEmprestimos++;
            } catch (InterruptedException e) {
                contErros++;
            } catch (IllegalArgumentException e) {
                qtdFaltaLivros++;
            }


            
        }

        System.out.println("Produtor out (Emprestimo): | Emprestimos: "+ qtdEmprestimos+ "| Errors: "+contErros + "| Falta de livros: "+qtdFaltaLivros);


    }
}
