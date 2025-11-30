package data;

public class TipoProduto {
    private String descricao;
    private int id; // ID único para identificação

    // Construtor sem parâmetros para o Gson
    public TipoProduto() {
    }

    // Construtor com descrição
    public TipoProduto(String descricao) {
        this.descricao = descricao;
        this.id = descricao.hashCode(); // Gera ID baseado na descrição
    }

    // Construtor completo
    public TipoProduto(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TipoProduto that = (TipoProduto) obj;
        return id == that.id || (descricao != null && descricao.equals(that.descricao));
    }

    @Override
    public int hashCode() {
        return descricao != null ? descricao.hashCode() : 0;
    }
}

