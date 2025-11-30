package utils;

import javax.swing.table.AbstractTableModel;
import data.TipoProduto;
import java.util.ArrayList;
import java.util.List;

public class TipoProdutoTableModel extends AbstractTableModel {
    private List<TipoProduto> tiposProdutos;
    private String[] colunas = {"ID", "Descrição"};

    public TipoProdutoTableModel() {
        this.tiposProdutos = new ArrayList<>();
    }

    public void setTiposProdutos(List<TipoProduto> tiposProdutos) {
        this.tiposProdutos = tiposProdutos;
        fireTableDataChanged();
    }

    public TipoProduto getTipoProdutoAt(int rowIndex) {
        return tiposProdutos.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return tiposProdutos.size();
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
        TipoProduto tipoProduto = tiposProdutos.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return tipoProduto.getId();
            case 1:
                return tipoProduto.getDescricao();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

