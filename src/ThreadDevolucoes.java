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
import java.util.List;
import java.util.Random;

/**
 * Produtor/consumidor com semáforo encapsulado na lista de dados
 */
public class ThreadDevolucoes extends Thread {
    static int ID = 0;
    private ListaSem<Livro> minhaListaLivros;
    private ListaSem<Emprestimo> minhaListaEmprestimos;

    private List<Cliente> listaClientes;
    private int quantRepeticoes;
    private int contErros;
    private int id;

    public ThreadDevolucoes(List<Cliente> listaClientes, ListaSem<Livro> listaLivrosDisponiveisSem, ListaSem<Emprestimo> minhaListaEmprestimos, int quant){
        this.minhaListaLivros = listaLivrosDisponiveisSem;
        this.listaClientes = listaClientes;
        this.minhaListaEmprestimos = minhaListaEmprestimos;
        this.quantRepeticoes = quant;
        this.id = 3;
        this.contErros=0;
    }

    @Override
    /**
     * Excecução:
     * sorteia um número; chama o método de inserção com sincronização encapsulada; guarda a soma para conferência com consumidores.
     */
    public void run(){
        int qtdDevolvidos = 0;
        for (int i = 0; i < this.quantRepeticoes; i++) {
            try {
                Emprestimo emprestimo = minhaListaEmprestimos.removeAleatorio();
                minhaListaLivros.add(emprestimo.devolucao(LocalDate.now()));
                qtdDevolvidos++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }  catch (IllegalArgumentException e) {
                contErros++;
            }

        }


        System.out.println("Consumidor out (devolucao): | Devolvidos: "+ qtdDevolvidos+ "| Errors: "+contErros);

    }
}
