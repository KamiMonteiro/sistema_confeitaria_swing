package screen;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
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

import data.Encomenda;
import utils.EncomendaTableModel;
import utils.SistemaConfeitaria;

public class ConsultaEncomendas extends JDialog {

    // Labels
    JLabel labelFiltro = new JLabel();

    // TextField
    JTextField textFieldFiltro = new JTextField();

    // Table
    EncomendaTableModel tableModel = new EncomendaTableModel();
    JTable tabela = new JTable(tableModel);
    JScrollPane scrollTable = new JScrollPane(tabela);

    // Buttons
    JButton buttonInserir = new JButton();
    JButton buttonAlterar = new JButton();
    JButton buttonExcluir = new JButton();

    public ConsultaEncomendas() {
        super();
        initialize();
    }

    public void consultar() {
        try {
            // Limpa a tabela
            tableModel.setEncomendas(new ArrayList<>());

            // Aplicar filtro
            String filtro = textFieldFiltro.getText().toLowerCase();

            // Formato de data brasileiro
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            List<Encomenda> encomendasFiltradas = SistemaConfeitaria.getListaEncomendasUsuarioLogado().getEncomendas().stream()
                .filter(encomenda -> filtro.isEmpty() || 
                    (encomenda.getNomeCliente() != null && encomenda.getNomeCliente().toLowerCase().contains(filtro)) ||
                    (encomenda.getTelefoneWhatsApp() != null && encomenda.getTelefoneWhatsApp().toLowerCase().contains(filtro)) ||
                    (encomenda.getTipoProduto() != null && encomenda.getTipoProduto().getDescricao().toLowerCase().contains(filtro)) ||
                    (encomenda.getSabor() != null && encomenda.getSabor().toLowerCase().contains(filtro)) ||
                    (encomenda.getDataEntrega() != null && sdf.format(encomenda.getDataEntrega()).contains(filtro)) ||
                    (encomenda.getStatus() != null && encomenda.getStatus().getDescricao().toLowerCase().contains(filtro)))
                .collect(Collectors.toList());

            tableModel.setEncomendas(encomendasFiltradas);
        } catch (Exception exc) {
            System.out.printf(exc.getMessage());
        }
    }

    public void excluir(Encomenda encomenda) {
        SistemaConfeitaria.getListaEncomendasUsuarioLogado().getEncomendas().removeIf(e -> e == encomenda);
        SistemaConfeitaria.serializarUsuarios();
    }

    private void initialize() {
        this.setTitle("Consulta de Encomendas");
        this.setSize(1000, 600);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);

        // Buttons
        buttonInserir.setSize(100, 50);
        buttonInserir.setLocation(20, 510);
        buttonInserir.setText("Inserir");
        buttonInserir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Instancia a janela de Manutenção
                new ManutencaoEncomendas();
                // Realiza uma consulta logo após fechar a janela
                consultar();
            }
        });

        buttonAlterar.setSize(100, 50);
        buttonAlterar.setLocation(140, 510);
        buttonAlterar.setText("Alterar");
        buttonAlterar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se existe um registro selecionado
                if (tabela.getSelectedRow() != -1) {
                    // Pega o objeto Encomenda da linha selecionada da tabela
                    Encomenda encomenda = tableModel.getEncomendaAt(tabela.getSelectedRow());
                    // Passa o objeto Encomenda como parâmetro para o construtor da classe ManutencaoEncomendas
                    new ManutencaoEncomendas(encomenda);
                    // Realiza uma consulta logo após fechar a janela
                    consultar();
                }
            }
        });

        buttonExcluir.setSize(100, 50);
        buttonExcluir.setLocation(260, 510);
        buttonExcluir.setText("Excluir");
        buttonExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Verifica se existe um registro selecionado
                if (tabela.getSelectedRow() != -1) {
                    String message = "Deseja realmente excluir a encomenda?";
                    String title = "Confirmação de exclusão";
                    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                    // Verifica se o usuário selecionou SIM
                    if (reply == JOptionPane.YES_OPTION) {
                        // Pega o objeto Encomenda da linha selecionada da tabela
                        Encomenda encomenda = tableModel.getEncomendaAt(tabela.getSelectedRow());
                        // Chama o método de exclusão
                        excluir(encomenda);
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
        scrollTable.setSize(975, 450);
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

