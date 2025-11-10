package data;

public enum TipoProduto {
    BOLO("Bolo"),
    TORTA("Torta"),
    DOCINHOS("Docinhos"),
    CUPCAKE("Cupcake"),
    BRIGADEIRO("Brigadeiro"),
    BEIJINHO("Beijinho"),
    BEM_CASADO("Bem Casado"),
    OUTROS("Outros");

    private String descricao;

    TipoProduto(String descricao) {
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

