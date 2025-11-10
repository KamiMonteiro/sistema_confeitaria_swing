package screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.Encomenda;
import data.StatusEncomenda;
import data.TipoProduto;
import utils.SistemaConfeitaria;

public class RelatoriosEncomendas extends JDialog {

    // Labels
    JLabel labelTitulo = new JLabel();
    JLabel labelFiltroStatus = new JLabel();
    JLabel labelFiltroTipo = new JLabel();
    JLabel labelDataInicio = new JLabel();
    JLabel labelDataFim = new JLabel();

    // Componentes
    JComboBox<StatusEncomenda> comboBoxStatus = new JComboBox<>();
    JComboBox<TipoProduto> comboBoxTipoProduto = new JComboBox<>();
    JTextField textFieldDataInicio = new JTextField();
    JTextField textFieldDataFim = new JTextField();
    JTextArea textAreaRelatorio = new JTextArea();
    JScrollPane scrollRelatorio = new JScrollPane(textAreaRelatorio);

    // Buttons
    JButton buttonGerar = new JButton();
    JButton buttonFechar = new JButton();

    public RelatoriosEncomendas() {
        super();
        initialize();
    }

    private void gerarRelatorio() {
        try {
            List<Encomenda> encomendas = SistemaConfeitaria.getListaEncomendasUsuarioLogado().getEncomendas();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // Filtros
            StatusEncomenda statusFiltro = (StatusEncomenda) comboBoxStatus.getSelectedItem();
            TipoProduto tipoFiltro = (TipoProduto) comboBoxTipoProduto.getSelectedItem();
            String dataInicioStr = textFieldDataInicio.getText().trim();
            String dataFimStr = textFieldDataFim.getText().trim();

            // Aplicar filtros
            List<Encomenda> encomendasFiltradas = encomendas.stream()
                .filter(e -> {
                    if (statusFiltro != null) {
                        if (!e.getStatus().equals(statusFiltro)) return false;
                    }
                    if (tipoFiltro != null) {
                        if (!e.getTipoProduto().equals(tipoFiltro)) return false;
                    }
                    if (!dataInicioStr.isEmpty() && e.getDataEntrega() != null) {
                        try {
                            Date dataInicio = sdf.parse(dataInicioStr);
                            if (e.getDataEntrega().before(dataInicio)) return false;
                        } catch (Exception ex) {}
                    }
                    if (!dataFimStr.isEmpty() && e.getDataEntrega() != null) {
                        try {
                            Date dataFim = sdf.parse(dataFimStr);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dataFim);
                            cal.add(Calendar.DAY_OF_MONTH, 1);
                            if (e.getDataEntrega().after(cal.getTime())) return false;
                        } catch (Exception ex) {}
                    }
                    return true;
                })
                .collect(java.util.stream.Collectors.toList());

            // Gerar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("═══════════════════════════════════════════════════════════\n");
            relatorio.append("           RELATÓRIO DE ENCOMENDAS - CONFEITARIA\n");
            relatorio.append("═══════════════════════════════════════════════════════════\n\n");
            relatorio.append("Data do Relatório: ").append(sdf.format(new Date())).append("\n");
            relatorio.append("Usuário: ").append(SistemaConfeitaria.getUsuarioLogado().getLogin()).append("\n\n");
            
            if (statusFiltro != null) {
                relatorio.append("Filtro Status: ").append(statusFiltro.getDescricao()).append("\n");
            }
            if (tipoFiltro != null) {
                relatorio.append("Filtro Tipo: ").append(tipoFiltro.getDescricao()).append("\n");
            }
            if (!dataInicioStr.isEmpty()) {
                relatorio.append("Período: ").append(dataInicioStr);
                if (!dataFimStr.isEmpty()) {
                    relatorio.append(" até ").append(dataFimStr);
                }
                relatorio.append("\n");
            }
            relatorio.append("\n");
            relatorio.append("Total de Encomendas: ").append(encomendasFiltradas.size()).append("\n");
            relatorio.append("───────────────────────────────────────────────────────────\n\n");

            // Estatísticas
            double valorTotal = 0;
            double sinalTotal = 0;
            int[] contadorStatus = new int[StatusEncomenda.values().length];
            
            for (Encomenda e : encomendasFiltradas) {
                valorTotal += e.getValor();
                sinalTotal += e.getSinalPago();
                for (int i = 0; i < StatusEncomenda.values().length; i++) {
                    if (e.getStatus().equals(StatusEncomenda.values()[i])) {
                        contadorStatus[i]++;
                    }
                }
            }

            relatorio.append("ESTATÍSTICAS:\n");
            relatorio.append("───────────────────────────────────────────────────────────\n");
            relatorio.append(String.format("Valor Total: R$ %.2f\n", valorTotal));
            relatorio.append(String.format("Sinal Total Pago: R$ %.2f\n", sinalTotal));
            relatorio.append(String.format("Valor Restante: R$ %.2f\n", valorTotal - sinalTotal));
            relatorio.append("\n");
            relatorio.append("Por Status:\n");
            for (int i = 0; i < StatusEncomenda.values().length; i++) {
                relatorio.append("  ").append(StatusEncomenda.values()[i].getDescricao())
                         .append(": ").append(contadorStatus[i]).append("\n");
            }
            relatorio.append("\n");
            relatorio.append("═══════════════════════════════════════════════════════════\n");
            relatorio.append("DETALHAMENTO DAS ENCOMENDAS:\n");
            relatorio.append("═══════════════════════════════════════════════════════════\n\n");

            // Detalhes
            int numero = 1;
            for (Encomenda e : encomendasFiltradas) {
                relatorio.append("Encomenda #").append(numero++).append("\n");
                relatorio.append("  Cliente: ").append(e.getNomeCliente()).append("\n");
                relatorio.append("  Telefone: ").append(e.getTelefoneWhatsApp()).append("\n");
                relatorio.append("  Produto: ").append(e.getTipoProduto().getDescricao()).append("\n");
                relatorio.append("  Sabor: ").append(e.getSabor()).append("\n");
                relatorio.append("  Tamanho/Peso: ").append(e.getTamanhoPeso()).append("\n");
                if (e.getDataEntrega() != null) {
                    relatorio.append("  Data Entrega: ").append(sdf.format(e.getDataEntrega())).append("\n");
                }
                relatorio.append("  Hora: ").append(e.getHoraEntrega()).append("\n");
                relatorio.append(String.format("  Valor: R$ %.2f\n", e.getValor()));
                relatorio.append(String.format("  Sinal Pago: R$ %.2f\n", e.getSinalPago()));
                relatorio.append(String.format("  Valor Restante: R$ %.2f\n", e.getValorRestante()));
                relatorio.append("  Status: ").append(e.getStatus().getDescricao()).append("\n");
                if (e.getObservacoes() != null && !e.getObservacoes().isEmpty()) {
                    relatorio.append("  Observações: ").append(e.getObservacoes()).append("\n");
                }
                relatorio.append("───────────────────────────────────────────────────────────\n");
            }

            textAreaRelatorio.setText(relatorio.toString());
        } catch (Exception e) {
            textAreaRelatorio.setText("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private void initialize() {
        this.setTitle("Relatórios de Encomendas");
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);

        int yPos = 20;

        // Título
        labelTitulo.setSize(300, 30);
        labelTitulo.setLocation(20, yPos);
        labelTitulo.setText("Relatório de Encomendas");
        labelTitulo.setFont(labelTitulo.getFont().deriveFont(16f));
        yPos += 40;

        // Filtro Status
        labelFiltroStatus.setSize(120, 20);
        labelFiltroStatus.setLocation(20, yPos);
        labelFiltroStatus.setText("Filtrar por Status:");
        comboBoxStatus.setSize(200, 25);
        comboBoxStatus.setLocation(150, yPos);
        comboBoxStatus.addItem(null); // Opção "Todos"
        for (StatusEncomenda status : StatusEncomenda.values()) {
            comboBoxStatus.addItem(status);
        }
        yPos += 35;

        // Filtro Tipo
        labelFiltroTipo.setSize(120, 20);
        labelFiltroTipo.setLocation(20, yPos);
        labelFiltroTipo.setText("Filtrar por Tipo:");
        comboBoxTipoProduto.setSize(200, 25);
        comboBoxTipoProduto.setLocation(150, yPos);
        comboBoxTipoProduto.addItem(null); // Opção "Todos"
        for (TipoProduto tipo : TipoProduto.values()) {
            comboBoxTipoProduto.addItem(tipo);
        }
        yPos += 35;

        // Data Início
        labelDataInicio.setSize(120, 20);
        labelDataInicio.setLocation(20, yPos);
        labelDataInicio.setText("Data Início (dd/MM/yyyy):");
        textFieldDataInicio.setSize(150, 25);
        textFieldDataInicio.setLocation(150, yPos);
        yPos += 35;

        // Data Fim
        labelDataFim.setSize(120, 20);
        labelDataFim.setLocation(20, yPos);
        labelDataFim.setText("Data Fim (dd/MM/yyyy):");
        textFieldDataFim.setSize(150, 25);
        textFieldDataFim.setLocation(150, yPos);
        yPos += 40;

        // Botões
        buttonGerar.setSize(120, 35);
        buttonGerar.setLocation(20, yPos);
        buttonGerar.setText("Gerar Relatório");
        buttonGerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gerarRelatorio();
            }
        });

        buttonFechar.setSize(120, 35);
        buttonFechar.setLocation(150, yPos);
        buttonFechar.setText("Fechar");
        buttonFechar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        yPos += 50;

        // Área de Relatório
        textAreaRelatorio.setEditable(false);
        textAreaRelatorio.setFont(new java.awt.Font("Courier New", java.awt.Font.PLAIN, 11));
        scrollRelatorio.setSize(650, 400);
        scrollRelatorio.setLocation(20, yPos);

        this.add(labelTitulo);
        this.add(labelFiltroStatus);
        this.add(comboBoxStatus);
        this.add(labelFiltroTipo);
        this.add(comboBoxTipoProduto);
        this.add(labelDataInicio);
        this.add(textFieldDataInicio);
        this.add(labelDataFim);
        this.add(textFieldDataFim);
        this.add(buttonGerar);
        this.add(buttonFechar);
        this.add(scrollRelatorio);

        this.setVisible(true);
    }
}

