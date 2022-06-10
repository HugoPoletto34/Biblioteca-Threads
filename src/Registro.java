import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Registro {

    // serialização: gravando o objetos no arquivo binário "nomeArq"
    public static<T> void gravarArquivoBinario(Map<Integer, T> lista, String nomeArq) {
        File arq = new File(nomeArq);
        try {
            arq.delete();
            arq.createNewFile();

            ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
            objOutput.writeObject(lista);
            objOutput.close();

        } catch(IOException erro) {
            System.out.printf("Erro: %s\n", erro.getMessage());
        }
    }

    // desserialização: recuperando os objetos gravados no arquivo binário "nomeArq"
    public static<T> HashMap<Integer, T> lerArquivoBinario(String nomeArq) {
        HashMap<Integer, T> lista = new HashMap<>();
        try {
            File arq = new File(nomeArq);
            if (arq.exists()) {
                ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(arq));
                lista = (HashMap<Integer, T>)objInput.readObject();
                objInput.close();
            }
        } catch(IOException erro1) {
            System.out.printf("Erro: %s\n", erro1.getMessage());
        } catch(ClassNotFoundException erro2) {
            System.out.printf("Erro: %s\n", erro2.getMessage());
        }

        return(lista);
    }

    public static<T> Map<Integer, T> carregarDados(String nomeArq, String nomeArqBinario, Class<T> classe) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        HashMap<Integer, T> lista;
        if (new File(nomeArqBinario).exists()) {
            lista = Registro.lerArquivoBinario(nomeArqBinario);
        } else {
            lista = carregarLista(nomeArq, classe);
            Registro.gravarArquivoBinario(lista, nomeArqBinario);
        }
        return lista;
    }

    public static<T> HashMap<Integer, T> carregarLista(String nomeArq, Class<T> classe) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner leitor =  new Scanner(new File(nomeArq));
        HashMap<Integer,T>  entidades = new HashMap<>();
        while(leitor.hasNextLine()){
            String[] entidade = leitor.nextLine().split(";");

            int id = Integer.parseInt(entidade[0]);
            String nome = entidade[1];
            T novo = classe.getConstructor(Integer.class, String.class).newInstance(id, nome);
            entidades.put(id, novo);


        }

        leitor.close();
        return entidades;
    }
}