import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ListaSem<T> {
        List<T> lista;

        String descricao;
        Semaphore posicoesCheias;
//        Semaphore posicoesVazias;
        Semaphore bloqLista;

        public ListaSem(List<T> lista, String descricao) {
            this.lista = lista;
            this.descricao = descricao;
            posicoesCheias = new Semaphore(lista.size());
            bloqLista = new Semaphore(1);
        }

        public List<T> getLista() throws InterruptedException {
            List<T> listaResp;
            this.posicoesCheias.acquire();
            this.bloqLista.acquire();
                listaResp = this.lista;
                this.bloqLista.release();
                this.posicoesCheias.release();

            return listaResp;
        }

        public boolean isEmpty() throws InterruptedException {
            boolean resp;
            this.bloqLista.acquire();
                resp = this.lista.isEmpty();
            this.bloqLista.release();
            return resp;
        }

        public void add(T novo) throws InterruptedException{
            this.bloqLista.acquire();
                this.lista.add(novo);
//                this.posicoesVazias.release();
                this.posicoesCheias.release();
            this.bloqLista.release();
        }

        public void remove(T objeto) throws InterruptedException{
            this.posicoesCheias.acquire();
            this.bloqLista.acquire();
                 this.lista.remove(objeto);
            this.bloqLista.release();
        }

        public T removeAt(int i) throws InterruptedException{
            T removed;
            this.posicoesCheias.acquire();
//            this.posicoesVazias.acquire();
            this.bloqLista.acquire();
                 removed = this.lista.remove(i);
            this.bloqLista.release();

            return removed;
        }

        public T removeAleatorio() throws InterruptedException{
            T removed;
            int random = (int) Math.random() * getSize();
            this.posicoesCheias.acquire();
    //            this.posicoesVazias.acquire();
            this.bloqLista.acquire();
            removed = this.lista.remove(random);
            this.bloqLista.release();

            return removed;
        }

        public int getSize() throws InterruptedException{
            int tam;
            this.bloqLista.acquire();
                tam =  this.lista.size();
            this.bloqLista.release();
        return tam;
        }

    @Override
    public String toString() {
        StringBuilder resp = new StringBuilder();
        resp.append("Lista ").append(this.descricao).append(": \n");
        resp.append("Tamanho: ").append(this.lista.size()).append("\n");

        return resp.toString();
    }
}
