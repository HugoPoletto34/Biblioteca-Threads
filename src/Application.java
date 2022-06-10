import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Application {
    static final String nomeArqClientes = "./Biblio/clientes.txt";
    static final String nomeArqLivros = "./Biblio/livros.txt";

    static final String nomeArqClientesBinario = "./Database/clientes.dat";
    static final String nomeArqLivrosBinario = "./Database/livros.dat";
    static Map<Integer, Livro> listaLivros;
    static Map<Integer, Cliente> listaClientes;
    static int TOTAL_OP = 500;
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException {

        listaLivros = Registro.carregarDados(nomeArqLivros, nomeArqLivrosBinario, Livro.class);
        listaClientes = Registro.carregarDados(nomeArqClientes, nomeArqClientesBinario, Cliente.class);

        ListaSem<Livro> listaLivrosDisponiveisSem = new ListaSem<>(getLivrosDisponiveis(), "Livros disponíveis");
        ListaSem<Emprestimo> listaEmprestimosSem = new ListaSem<>(getEmprestimos(), "Emprestimos");
        List<Cliente> clientes = new LinkedList<>(listaClientes.values());

        ThreadEmprestimos te  = new ThreadEmprestimos(clientes, listaLivrosDisponiveisSem, listaEmprestimosSem, TOTAL_OP);
        ThreadRenovacoes tr  = new ThreadRenovacoes(listaLivrosDisponiveisSem, listaEmprestimosSem, TOTAL_OP/2);
        ThreadDevolucoes td = new ThreadDevolucoes(clientes, listaLivrosDisponiveisSem, listaEmprestimosSem, TOTAL_OP/2);

        te.start();
        tr.start();
        td.start();

        te.join();
        td.join();
        tr.join();

        System.out.println(listaLivrosDisponiveisSem);
        System.out.println(listaEmprestimosSem);

        Registro.gravarArquivoBinario(listaClientes, nomeArqClientesBinario);
        Registro.gravarArquivoBinario(listaLivros, nomeArqLivrosBinario);

        System.out.println("\n1 - Livro mais emprestado:");
        Map<Object, Long> livros = listaClientes.values()
                .stream()
                .flatMap((cliente) -> (cliente).getEmprestimos().stream())
                .map(Emprestimo::getLivro)
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        Optional<Long> maior = livros.values().stream().max(Long::compareTo);
        List<Object> livroList = new ArrayList<>(livros.keySet());
        Optional<Object> livro = livroList.stream().filter(l -> livros.get(l).equals(maior.get())).findFirst();
        System.out.println("Foi emprestado " + maior.get() + " vezes.");
        System.out.println(livro);


        System.out.println("\n2 - Quantos empréstimos tem Arthur A Silva? ");
        System.out.println((listaClientes.get(13289)).getEmprestimos()
                .stream()
                .count() + " empréstimos");

        System.out.println("\n3 - Qual é o cliente mais frequente?");
        System.out.println(listaClientes.values().stream().max((c1, c2) -> {
            if ((c1).getEmprestimos().size() > (c2).getEmprestimos().size())
                return 1;
            else if ((c1).getEmprestimos().size() < (c2).getEmprestimos().size())
                return -1;
            return 0;
        }).stream().findFirst().get());


        System.out.println("\n4 - Qual é o empréstimo mais recente?");
        System.out.println(listaClientes.values()
                .stream()
                .flatMap(cliente -> (cliente).getEmprestimos().stream()).max((e1, e2) -> {
                    if ((e1).getDataEmprestimo().isAfter((e2).getDataEmprestimo()))
                        return 1;
                    else if ((e2).getDataEmprestimo().isAfter((e1).getDataEmprestimo()))
                        return -1;
                    return 0;
                }).get());

    }

    public static List<Livro> getLivrosDisponiveis() {
        return listaLivros.values().stream()
                .filter(l -> !l.isEmprestado())
                .collect(Collectors.toList());
    }

    public static List<Emprestimo> getEmprestimos() {
        return listaClientes.values().stream()
                .flatMap(cliente -> cliente.getEmprestimos().stream())
                .collect(Collectors.toList());
    }
}
