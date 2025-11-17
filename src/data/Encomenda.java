package data;

import java.util.Date;

public class Encomenda {
    private String nomeCliente;
    private String telefoneWhatsApp;
    private TipoProduto tipoProduto;
    private String sabor;
    private String tamanhoPeso;
    private Date dataEntrega;
    private String horaEntrega;
    private double valor;
    private double sinalPago;
    private String observacoes;
    private StatusEncomenda status;

    // Construtor sem parâmetros para o Gson
    public Encomenda() {
        this.status = StatusEncomenda.PENDENTE;
    }

    // Construtor completo
    public Encomenda(String nomeCliente, String telefoneWhatsApp, TipoProduto tipoProduto,
                     String sabor, String tamanhoPeso, Date dataEntrega, String horaEntrega,
                     double valor, double sinalPago, String observacoes, StatusEncomenda status) {
        this.nomeCliente = nomeCliente;
        this.telefoneWhatsApp = telefoneWhatsApp;
        this.tipoProduto = tipoProduto;
        this.sabor = sabor;
        this.tamanhoPeso = tamanhoPeso;
        this.dataEntrega = dataEntrega;
        this.horaEntrega = horaEntrega;
        this.valor = valor;
        this.sinalPago = sinalPago;
        this.observacoes = observacoes;
        this.status = status != null ? status : StatusEncomenda.PENDENTE;
    }

    // Getters e Setters
    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getTelefoneWhatsApp() {
        return telefoneWhatsApp;
    }

    public void setTelefoneWhatsApp(String telefoneWhatsApp) {
        this.telefoneWhatsApp = telefoneWhatsApp;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public String getSabor() {
        return sabor;
    }

    public void setSabor(String sabor) {
        this.sabor = sabor;
    }

    public String getTamanhoPeso() {
        return tamanhoPeso;
    }

    public void setTamanhoPeso(String tamanhoPeso) {
        this.tamanhoPeso = tamanhoPeso;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getSinalPago() {
        return sinalPago;
    }

    public void setSinalPago(double sinalPago) {
        this.sinalPago = sinalPago;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusEncomenda getStatus() {
        return status;
    }

    public void setStatus(StatusEncomenda status) {
        this.status = status;
    }

    // Método auxiliar para calcular valor restante
    public double getValorRestante() {
        return valor - sinalPago;
    }

    @Override
    public String toString() {
        return nomeCliente + " - " + tipoProduto + " (" + status + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Encomenda encomenda = (Encomenda) obj;
        return nomeCliente != null && nomeCliente.equals(encomenda.nomeCliente) &&
               telefoneWhatsApp != null && telefoneWhatsApp.equals(encomenda.telefoneWhatsApp) &&
               dataEntrega != null && dataEntrega.equals(encomenda.dataEntrega) &&
               horaEntrega != null && horaEntrega.equals(encomenda.horaEntrega);
    }
}

