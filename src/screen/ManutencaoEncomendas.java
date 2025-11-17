package screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import data.Encomenda;
import data.TipoProduto;
import data.StatusEncomenda;
import utils.SistemaConfeitaria;
import utils.Utilitario;

public class ManutencaoEncomendas extends JDialog {
    private Encomenda encomenda;

    // Labels
    JLabel labelNomeCliente = new JLabel();
    JLabel labelTelefone = new JLabel();
    JLabel labelTipoProduto = new JLabel();
    JLabel labelSabor = new JLabel();
    JLabel labelTamanhoPeso = new JLabel();
    JLabel labelDataEntrega = new JLabel();
    JLabel labelHoraEntrega = new JLabel();
    JLabel labelValor = new JLabel();
    JLabel labelSinalPago = new JLabel();
    JLabel labelObservacoes = new JLabel();
    JLabel labelStatus = new JLabel();

    // TextFields
    JTextField textFieldNomeCliente = new JTextField();
    JFormattedTextField textFieldTelefone;
    JComboBox<TipoProduto> comboBoxTipoProduto = new JComboBox<>();
    JTextField textFieldSabor = new JTextField();
    JTextField textFieldTamanhoPeso = new JTextField();
    JFormattedTextField textFieldDataEntrega;
    JFormattedTextField textFieldHoraEntrega;
    JTextField textFieldValor = new JTextField();
    JTextField textFieldSinalPago = new JTextField();
    JTextArea textAreaObservacoes = new JTextArea();
    JComboBox<StatusEncomenda> comboBoxStatus = new JComboBox<>();

    // Buttons
    JButton buttonSalvar = new JButton();
    JButton buttonCancelar = new JButton();

    // Inserir
    public ManutencaoEncomendas() {
        super();
        encomenda = null;
        initialize();
    }

    // Alterar
    public ManutencaoEncomendas(Encomenda encomenda) {
        super();
        this.encomenda = encomenda;
        initialize();
    }

    // Recarrega os tipos de produtos no combo box
    private void recarregarTiposProdutos() {
        comboBoxTipoProduto.removeAllItems();
        for (TipoProduto tipo : SistemaConfeitaria.getTiposProdutos()) {
            comboBoxTipoProduto.addItem(tipo);
        }
    }

    // Preenche os campos com os dados da encomenda
    private void preencheCampos() {
        try {
            textFieldNomeCliente.setText(encomenda.getNomeCliente());
            textFieldTelefone.setText(encomenda.getTelefoneWhatsApp());
            comboBoxTipoProduto.setSelectedItem(encomenda.getTipoProduto());
            textFieldSabor.setText(encomenda.getSabor());
            textFieldTamanhoPeso.setText(encomenda.getTamanhoPeso());
            textFieldDataEntrega.setText(new SimpleDateFormat("dd/MM/yyyy").format(encomenda.getDataEntrega()));
            textFieldHoraEntrega.setText(encomenda.getHoraEntrega());
            textFieldValor.setText(String.valueOf(encomenda.getValor()));
            textFieldSinalPago.setText(String.valueOf(encomenda.getSinalPago()));
            textAreaObservacoes.setText(encomenda.getObservacoes());
            comboBoxStatus.setSelectedItem(encomenda.getStatus());
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    // Valida os campos do formulário
    private void validaCampos() throws Exception {
        if (textFieldNomeCliente.getText().trim().equals("")) {
            textFieldNomeCliente.requestFocus();
            throw new Exception("Digite o nome do cliente");
        }

        String telefone = textFieldTelefone.getText().trim();
        if (telefone.equals("")) {
            textFieldTelefone.requestFocus();
            throw new Exception("Digite o telefone/WhatsApp");
        }
        // Valida formato de telefone brasileiro
        if (!Utilitario.isTelefone(telefone)) {
            textFieldTelefone.requestFocus();
            throw new Exception("Telefone inválido! Digite um número válido.");
        }

        if (comboBoxTipoProduto.getSelectedItem() == null) {
            comboBoxTipoProduto.requestFocus();
            throw new Exception("Selecione o tipo de produto");
        }

        if (textFieldSabor.getText().trim().equals("")) {
            textFieldSabor.requestFocus();
            throw new Exception("Digite o sabor");
        }

        if (!Utilitario.isDate(textFieldDataEntrega.getText().trim())) {
            textFieldDataEntrega.requestFocus();
            throw new Exception("Digite uma data de entrega válida (dd/MM/yyyy)");
        }

        try {
            double valor = Double.parseDouble(textFieldValor.getText().trim());
            if (valor <= 0) {
                throw new Exception("O valor deve ser maior que zero");
            }
        } catch (NumberFormatException e) {
            textFieldValor.requestFocus();
            throw new Exception("Digite um valor válido");
        }

        try {
            double sinal = Double.parseDouble(textFieldSinalPago.getText().trim());
            if (sinal < 0) {
                throw new Exception("O sinal pago não pode ser negativo");
            }
            // Valida se sinal não é maior que o valor
            double valor = Double.parseDouble(textFieldValor.getText().trim());
            if (sinal > valor) {
                textFieldSinalPago.requestFocus();
                throw new Exception("O sinal pago não pode ser maior que o valor total");
            }
        } catch (NumberFormatException e) {
            textFieldSinalPago.requestFocus();
            throw new Exception("Digite um valor de sinal válido");
        }
    }

    private void salvar() {
        // Inserir
        if (encomenda == null) {
            try {
                validaCampos();
                
                encomenda = new Encomenda(
                    textFieldNomeCliente.getText().trim(),
                    textFieldTelefone.getText().trim(),
                    (TipoProduto) comboBoxTipoProduto.getSelectedItem(),
                    textFieldSabor.getText().trim(),
                    textFieldTamanhoPeso.getText().trim(),
                    Utilitario.stringToDate(textFieldDataEntrega.getText().trim()),
                    textFieldHoraEntrega.getText().trim(),
                    Double.parseDouble(textFieldValor.getText().trim()),
                    Double.parseDouble(textFieldSinalPago.getText().trim()),
                    textAreaObservacoes.getText().trim(),
                    (StatusEncomenda) comboBoxStatus.getSelectedItem()
                );

                SistemaConfeitaria.getListaEncomendasUsuarioLogado().adicionarEncomenda(encomenda);
                SistemaConfeitaria.serializarUsuarios();

                // Se inseriu o registro, fecha a tela
                dispose();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, exc.getMessage(), "Erro ao Inserir Registro",
                        JOptionPane.ERROR_MESSAGE);
                System.out.println(exc.getMessage());
            }
        } else // Alterar
        {
            try {
                validaCampos();
                
                encomenda.setNomeCliente(textFieldNomeCliente.getText().trim());
                encomenda.setTelefoneWhatsApp(textFieldTelefone.getText().trim());
                encomenda.setTipoProduto((TipoProduto) comboBoxTipoProduto.getSelectedItem());
                encomenda.setSabor(textFieldSabor.getText().trim());
                encomenda.setTamanhoPeso(textFieldTamanhoPeso.getText().trim());
                encomenda.setDataEntrega(Utilitario.stringToDate(textFieldDataEntrega.getText().trim()));
                encomenda.setHoraEntrega(textFieldHoraEntrega.getText().trim());
                encomenda.setValor(Double.parseDouble(textFieldValor.getText().trim()));
                encomenda.setSinalPago(Double.parseDouble(textFieldSinalPago.getText().trim()));
                encomenda.setObservacoes(textAreaObservacoes.getText().trim());
                encomenda.setStatus((StatusEncomenda) comboBoxStatus.getSelectedItem());

                SistemaConfeitaria.serializarUsuarios();

                // Se atualizou o registro, fecha a tela
                dispose();
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, exc.getMessage(), "Erro ao Atualizar Registro",
                        JOptionPane.ERROR_MESSAGE);
                System.out.println(exc.getMessage());
            }
        }
    }

    private void initialize() {
        this.setTitle("Manutenção de Encomendas");
        this.setSize(600, 700);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);

        int yPos = 20;
        int labelWidth = 120;
        int fieldWidth = 250;
        int fieldHeight = 20;
        int spacing = 30;

        // Nome do Cliente
        labelNomeCliente.setSize(labelWidth, fieldHeight);
        labelNomeCliente.setLocation(20, yPos);
        labelNomeCliente.setText("Nome do Cliente:");
        textFieldNomeCliente.setSize(fieldWidth, fieldHeight);
        textFieldNomeCliente.setLocation(150, yPos);
        yPos += spacing;

        // Telefone/WhatsApp
        labelTelefone.setSize(labelWidth, fieldHeight);
        labelTelefone.setLocation(20, yPos);
        labelTelefone.setText("Telefone/WhatsApp:");
        try {
            MaskFormatter mascaraTelefone = new MaskFormatter("(##) #####-####");
            textFieldTelefone = new JFormattedTextField(mascaraTelefone);
            textFieldTelefone.setSize(fieldWidth, fieldHeight);
            textFieldTelefone.setLocation(150, yPos);
        } catch (Exception exc) {
            System.out.println("Erro ao formatar máscara de telefone: " + exc.getMessage());
        }
        yPos += spacing;

        // Tipo de Produto
        labelTipoProduto.setSize(labelWidth, fieldHeight);
        labelTipoProduto.setLocation(20, yPos);
        labelTipoProduto.setText("Tipo de Produto:");
        comboBoxTipoProduto.setSize(fieldWidth, fieldHeight);
        comboBoxTipoProduto.setLocation(150, yPos);
        recarregarTiposProdutos();
        yPos += spacing;

        // Sabor
        labelSabor.setSize(labelWidth, fieldHeight);
        labelSabor.setLocation(20, yPos);
        labelSabor.setText("Sabor:");
        textFieldSabor.setSize(fieldWidth, fieldHeight);
        textFieldSabor.setLocation(150, yPos);
        yPos += spacing;

        // Tamanho/Peso
        labelTamanhoPeso.setSize(labelWidth, fieldHeight);
        labelTamanhoPeso.setLocation(20, yPos);
        labelTamanhoPeso.setText("Tamanho/Peso:");
        textFieldTamanhoPeso.setSize(fieldWidth, fieldHeight);
        textFieldTamanhoPeso.setLocation(150, yPos);
        yPos += spacing;

        // Data de Entrega
        labelDataEntrega.setSize(labelWidth, fieldHeight);
        labelDataEntrega.setLocation(20, yPos);
        labelDataEntrega.setText("Data de Entrega:");
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            textFieldDataEntrega = new JFormattedTextField(mascaraData);
            textFieldDataEntrega.setSize(fieldWidth, fieldHeight);
            textFieldDataEntrega.setLocation(150, yPos);
        } catch (Exception exc) {
            System.out.println("Erro ao formatar máscara de data: " + exc.getMessage());
        }
        yPos += spacing;

        // Hora de Entrega
        labelHoraEntrega.setSize(labelWidth, fieldHeight);
        labelHoraEntrega.setLocation(20, yPos);
        labelHoraEntrega.setText("Hora de Entrega:");
        try {
            MaskFormatter mascaraHora = new MaskFormatter("##:##");
            textFieldHoraEntrega = new JFormattedTextField(mascaraHora);
            textFieldHoraEntrega.setSize(fieldWidth, fieldHeight);
            textFieldHoraEntrega.setLocation(150, yPos);
        } catch (Exception exc) {
            System.out.println("Erro ao formatar máscara de hora: " + exc.getMessage());
        }
        yPos += spacing;

        // Valor
        labelValor.setSize(labelWidth, fieldHeight);
        labelValor.setLocation(20, yPos);
        labelValor.setText("Valor (R$):");
        textFieldValor.setSize(fieldWidth, fieldHeight);
        textFieldValor.setLocation(150, yPos);
        yPos += spacing;

        // Sinal Pago
        labelSinalPago.setSize(labelWidth, fieldHeight);
        labelSinalPago.setLocation(20, yPos);
        labelSinalPago.setText("Sinal Pago (R$):");
        textFieldSinalPago.setSize(fieldWidth, fieldHeight);
        textFieldSinalPago.setLocation(150, yPos);
        yPos += spacing;

        // Status
        labelStatus.setSize(labelWidth, fieldHeight);
        labelStatus.setLocation(20, yPos);
        labelStatus.setText("Status:");
        comboBoxStatus.setSize(fieldWidth, fieldHeight);
        comboBoxStatus.setLocation(150, yPos);
        for (StatusEncomenda status : StatusEncomenda.values()) {
            comboBoxStatus.addItem(status);
        }
        yPos += spacing;

        // Observações
        labelObservacoes.setSize(labelWidth, fieldHeight);
        labelObservacoes.setLocation(20, yPos);
        labelObservacoes.setText("Observações:");
        textAreaObservacoes.setSize(fieldWidth, 80);
        textAreaObservacoes.setLocation(150, yPos);
        textAreaObservacoes.setLineWrap(true);
        textAreaObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObservacoes = new JScrollPane(textAreaObservacoes);
        scrollObservacoes.setSize(fieldWidth, 80);
        scrollObservacoes.setLocation(150, yPos);
        yPos += 100;

        // Buttons
        buttonSalvar.setSize(100, 50);
        buttonSalvar.setLocation(20, yPos);
        buttonSalvar.setText(encomenda == null ? "Inserir" : "Alterar");
        buttonSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });

        buttonCancelar.setSize(100, 50);
        buttonCancelar.setLocation(140, yPos);
        buttonCancelar.setText("Cancelar");
        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Se nenhuma encomenda foi passada por parâmetro, então é uma inserção
        if (encomenda == null) {
            buttonSalvar.setText("Inserir");
        } else { // Senão, é uma atualização
            buttonSalvar.setText("Alterar");
            preencheCampos();
        }

        // Adiciona todos os componentes
        this.add(labelNomeCliente);
        this.add(textFieldNomeCliente);
        this.add(labelTelefone);
        this.add(textFieldTelefone);
        this.add(labelTipoProduto);
        this.add(comboBoxTipoProduto);
        this.add(labelSabor);
        this.add(textFieldSabor);
        this.add(labelTamanhoPeso);
        this.add(textFieldTamanhoPeso);
        this.add(labelDataEntrega);
        this.add(textFieldDataEntrega);
        this.add(labelHoraEntrega);
        this.add(textFieldHoraEntrega);
        this.add(labelValor);
        this.add(textFieldValor);
        this.add(labelSinalPago);
        this.add(textFieldSinalPago);
        this.add(labelStatus);
        this.add(comboBoxStatus);
        this.add(labelObservacoes);
        this.add(scrollObservacoes);
        this.add(buttonSalvar);
        this.add(buttonCancelar);

        this.setVisible(true);
    }
}

