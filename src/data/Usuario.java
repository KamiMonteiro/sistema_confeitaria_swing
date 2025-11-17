package data;

public class Usuario {
    private String login;
    private String senha;
    private ListaEncomendas listaEncomendas;

    // Construtor sem parâmetros necessário para o Gson
    public Usuario() {
        this.listaEncomendas = new ListaEncomendas();
    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
        this.listaEncomendas = new ListaEncomendas();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ListaEncomendas getListaEncomendas() {
        return listaEncomendas;
    }

    public void setListaEncomendas(ListaEncomendas listaEncomendas) {
        this.listaEncomendas = listaEncomendas;
    }

    // Método removido - não é mais necessário

    @Override
    public String toString() {
        return login;
    }

    @Override
    public boolean equals(Object item) {
         if ((this.login.equals(((Usuario) item).getLogin())) &&
            (this.senha.equals(((Usuario) item).getSenha()))){
              return true;
         }
         return false;
    }
}

