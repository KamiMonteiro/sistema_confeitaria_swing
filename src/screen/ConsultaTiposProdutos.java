package screen;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import data.TipoProduto;
import data.Usuario;
import data.Encomenda;
import utils.TipoProdutoTableModel;
import utils.SistemaConfeitaria;

public class ConsultaTiposProdutos extends JDialog {

    // Labels
    JLabel labelFiltro = new JLabel();

    // TextField
    JTextField textFieldFiltro = new JTextField();

    // Table
    TipoProdutoTableModel tableModel = new TipoProdutoTableModel();
    JTable tabela = new JTable(tableModel);
    JScrollPane scrollTable = new JScrollPane(tabela);

    // Buttons
    JButton buttonInserir = new JButton();
    JButton buttonAlterar = new JButton();
    JButton buttonExcluir = new JButton();

    public ConsultaTiposProdutos() {
        super();
        initialize();
    }

    public void consultar() {
        try {
            // Limpa a tabela
            tableModel.setTiposProdutos(new ArrayList<>());

            // Aplicar filtro
            String filtro = textFieldFiltro.getText().toLowerCase();

            // Filtra os tipos de produtos
            List<TipoProduto> tiposFiltrados = SistemaConfeitaria.getTiposProdutos().stream()
                .filter(tipo -> filtro.isEmpty() || 
                    (tipo.getDescricao() != null && tipo.getDescricao().toLowerCase().contains(filtro)) ||
                    (String.valueOf(tipo.getId()).contains(filtro)))
                .collect(Collectors.toList());

            tableModel.setTiposProdutos(tiposFiltrados);
        } catch (Exception exc) {
            System.out.printf(exc.getMessage());
        }
    }

    public void excluir(TipoProduto tipoProduto) {
        // Verifica se o tipo de produto está sendo usado em alguma encomenda
        boolean emUso = false;
        for (Usuario usuario : SistemaConfeitaria.getUsuarios()) {
            for (Encomenda encomenda : usuario.getListaEncomendas().getEncomendas()) {
                if (encomenda.getTipoProduto() != null && 
                    encomenda.getTipoProduto().equals(tipoProduto)) {
                    emUso = true;
                    break;
                }
            }
            if (emUso) break;
        }

        if (emUso) {
            JOptionPane.showMessageDialog(this, 
                "Não é possível excluir este tipo de produto pois ele está sendo usado em uma ou mais encomendas.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        SistemaConfeitaria.getTiposProdutos().removeIf(t -> t.equals(tipoProduto));
        SistemaConfeitaria.serializarTiposProdutos();
    }

    private void initialize() {
        this.setTitle("Consulta de Tipos de Produtos");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);

        // Buttons
        buttonInserir.setSize(100, 50);
        buttonInserir.setLocation(20, 410);
        buttonInserir.setText("Inserir");
        buttonInserir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Instancia a janela de Manutenção
                new ManutencaoTiposProdutos();
                // Realiza uma consulta logo após fechar a janela
                consultar();
            }
        });

        buttonAlterar.setSize(100, 50);
        buttonAlterar.setLocation(140, 410);
        buttonAlterar.setText("Alterar");
        buttonAlterar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se existe um registro selecionado
                if (tabela.getSelectedRow() != -1) {
                    // Pega o objeto TipoProduto da linha selecionada da tabela
                    TipoProduto tipoProduto = tableModel.getTipoProdutoAt(tabela.getSelectedRow());
                    // Passa o objeto TipoProduto como parâmetro para o construtor da classe ManutencaoTiposProdutos
                    new ManutencaoTiposProdutos(tipoProduto);
                    // Realiza uma consulta logo após fechar a janela
                    consultar();
                }
            }
        });

        buttonExcluir.setSize(100, 50);
        buttonExcluir.setLocation(260, 410);
        buttonExcluir.setText("Excluir");
        buttonExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se existe um registro selecionado
                if (tabela.getSelectedRow() != -1) {
                    String message = "Deseja realmente excluir este tipo de produto?";
                    String title = "Confirmação de exclusão";
                    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                    // Verifica se o usuário selecionou SIM
                    if (reply == JOptionPane.YES_OPTION) {
                        // Pega o objeto TipoProduto da linha selecionada da tabela
                        TipoProduto tipoProduto = tableModel.getTipoProdutoAt(tabela.getSelectedRow());
                        // Chama o método de exclusão
                        excluir(tipoProduto);
                        // Realiza uma consulta logo após a exclusão
                        consultar();
                    }
                }
            }
        });

        // Labels
        labelFiltro.setSize(40, 20);
        labelFiltro.setLocation(20, 20);
        labelFiltro.setText("Filtro:");

        // TextField
        textFieldFiltro.setSize(400, 20);
        textFieldFiltro.setLocation(60, 20);
        textFieldFiltro.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent keyEvent) {
                // Verifica se a tecla (ENTER) foi pressionada
                if (keyEvent.getKeyCode() == 10) {
                    consultar();
                }
            }

            public void keyReleased(KeyEvent arg0) {
            }

            public void keyTyped(KeyEvent arg0) {
            }
        });

        //Tabela
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollTable.setSize(560, 350);
        scrollTable.setLocation(10, 50);

        this.add(buttonInserir);
        this.add(buttonAlterar);
        this.add(buttonExcluir);
        this.add(scrollTable);
        this.add(labelFiltro);
        this.add(textFieldFiltro);

        // Consulta ao abrir a tela
        consultar();

        this.setVisible(true);
    }
}

