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


import java.util.Random;

/**
 * Produtor/consumidor com semáforo encapsulado na lista de dados
 */
public class ThreadRenovacoes extends Thread{
 
    static int ID = 0;
    private ListaSem<Livro> minhaListaLivros;
    private ListaSem<Emprestimo> minhaListaEmprestimos;
    private int quantRepeticoes;
    private int id;
    private int contErros;

    public ThreadRenovacoes(ListaSem<Livro> listaLivrosDisponiveisSem, ListaSem<Emprestimo> minhaListaEmprestimos, int quant){
        this.minhaListaLivros = listaLivrosDisponiveisSem;
        this.minhaListaEmprestimos = minhaListaEmprestimos;
        this.quantRepeticoes = quant;
        this.id = 2;
        this.contErros=0;
    }
    
    @Override
    /**
     * Execução: um laço chamando a remoção da classe de dados (que encapsula a sincronização). Soma utilizada para conferência com o produtor.
     */
    public void run() {
        
        int qtdRenovacoes = 0;
        for (int i = 0; i < this.quantRepeticoes; i++) {

            try {
                Emprestimo emprestimo = minhaListaEmprestimos.removeAleatorio();
                emprestimo.renovarEmprestimo();
                minhaListaEmprestimos.add(emprestimo);
                qtdRenovacoes++;
            } catch (IndexOutOfBoundsException e) {
                contErros++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

        System.out.println("Consumer out (renovacao): | Renovações: "+ qtdRenovacoes+ "| Errors: "+contErros);


    }
}
