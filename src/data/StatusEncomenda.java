package data;

public enum StatusEncomenda {
    PENDENTE("Pendente"),
    EM_PRODUCAO("Em Produção"),
    PRONTO("Pronto"),
    ENTREGUE("Entregue");

    private String descricao;

    StatusEncomenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}

