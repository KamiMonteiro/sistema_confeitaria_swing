package utils;

import javax.swing.table.AbstractTableModel;
import data.Encomenda;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EncomendaTableModel extends AbstractTableModel {
    private List<Encomenda> encomendas;
    private String[] colunas = {"Cliente", "Telefone", "Produto", "Sabor", "Data Entrega", "Hora", "Valor", "Status"};

    public EncomendaTableModel() {
        this.encomendas = new ArrayList<>();
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
        fireTableDataChanged();
    }

    public Encomenda getEncomendaAt(int rowIndex) {
        return encomendas.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return encomendas.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Encomenda encomenda = encomendas.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        switch (columnIndex) {
            case 0:
                return encomenda.getNomeCliente();
            case 1:
                return encomenda.getTelefoneWhatsApp();
            case 2:
                return encomenda.getTipoProduto() != null ? encomenda.getTipoProduto().getDescricao() : "";
            case 3:
                return encomenda.getSabor();
            case 4:
                return encomenda.getDataEntrega() != null ? sdf.format(encomenda.getDataEntrega()) : "";
            case 5:
                return encomenda.getHoraEntrega();
            case 6:
                return String.format("R$ %.2f", encomenda.getValor());
            case 7:
                return encomenda.getStatus() != null ? encomenda.getStatus().getDescricao() : "";
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

