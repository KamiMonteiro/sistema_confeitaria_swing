package data;

<<<<<<< HEAD
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
=======
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
>>>>>>> 72d25b3042a27cd145dccd5015782b2ce9a7769c
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

<<<<<<< HEAD
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

=======
>>>>>>> 72d25b3042a27cd145dccd5015782b2ce9a7769c
    @Override
    public String toString() {
        return descricao;
    }
<<<<<<< HEAD

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
=======
>>>>>>> 72d25b3042a27cd145dccd5015782b2ce9a7769c
}

