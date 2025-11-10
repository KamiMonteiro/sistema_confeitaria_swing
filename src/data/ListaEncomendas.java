package data;

import java.util.ArrayList;
import java.util.List;

public class ListaEncomendas {
    private List<Encomenda> encomendas;

    public ListaEncomendas() {
        this.encomendas = new ArrayList<>();
    }

    public List<Encomenda> getEncomendas() {
        return encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }

    public void adicionarEncomenda(Encomenda encomenda) {
        encomendas.add(encomenda);
    }

    public void removerEncomenda(Encomenda encomenda) {
        encomendas.remove(encomenda);
    }
}

