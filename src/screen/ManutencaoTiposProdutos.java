package screen;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import data.TipoProduto;
import utils.SistemaConfeitaria;

public class ManutencaoTiposProdutos extends JDialog {
    private TipoProduto tipoProduto;

    // Labels
    JLabel labelDescricao = new JLabel();

    // TextFields
    JTextField textFieldDescricao = new JTextField();

    // Buttons
    JButton buttonSalvar = new JButton();
    JButton buttonCancelar = new JButton();

    // Inserir
    public ManutencaoTiposProdutos() {
        super();
        tipoProduto = null;
        initialize();
    }

    // Alterar
    public ManutencaoTiposProdutos(TipoProduto tipoProduto) {
        super();
        this.tipoProduto = tipoProduto;
        initialize();
    }

    // Preenche os campos com os dados do tipo de produto
    private void preencheCampos() {
        try {
            textFieldDescricao.setText(tipoProduto.getDescricao());
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }

    // Valida os campos do formulário
    private void validaCampos() throws Exception {
        if (textFieldDescricao.getText().trim().equals("")) {
            textFieldDescricao.requestFocus();
            throw new Exception("Digite a descrição do tipo de produto");
        }

        // Verifica se já existe um tipo de produto com a mesma descrição
        String descricao = textFieldDescricao.getText().trim();
        for (TipoProduto tp : SistemaConfeitaria.getTiposProdutos()) {
            if (tp.getDescricao().equalsIgnoreCase(descricao)) {
                // Se estiver alterando, permite se for o mesmo objeto
                if (tipoProduto != null && tp.equals(tipoProduto)) {
                    continue;
                }
                textFieldDescricao.requestFocus();
                throw new Exception("Já existe um tipo de produto com esta descrição");
            }
        }
    }

    private void salvar() {
        // Inserir
        if (tipoProduto == null) {
            try {
                validaCampos();
                
                // Gera um novo ID baseado no maior ID existente + 1
                int novoId = 1;
                if (!SistemaConfeitaria.getTiposProdutos().isEmpty()) {
                    novoId = SistemaConfeitaria.getTiposProdutos().stream()
                        .mapToInt(TipoProduto::getId)
                        .max()
                        .orElse(0) + 1;
                }

                tipoProduto = new TipoProduto(novoId, textFieldDescricao.getText().trim());

                SistemaConfeitaria.getTiposProdutos().add(tipoProduto);
                SistemaConfeitaria.serializarTiposProdutos();

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
                
                tipoProduto.setDescricao(textFieldDescricao.getText().trim());

                SistemaConfeitaria.serializarTiposProdutos();

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
        this.setTitle("Manutenção de Tipos de Produtos");
        this.setSize(500, 200);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);

        int yPos = 20;
        int labelWidth = 120;
        int fieldWidth = 250;
        int fieldHeight = 20;
        int spacing = 40;

        // Descrição
        labelDescricao.setSize(labelWidth, fieldHeight);
        labelDescricao.setLocation(20, yPos);
        labelDescricao.setText("Descrição:");
        textFieldDescricao.setSize(fieldWidth, fieldHeight);
        textFieldDescricao.setLocation(150, yPos);
        yPos += spacing;

        // Buttons
        buttonSalvar.setSize(100, 50);
        buttonSalvar.setLocation(20, yPos);
        buttonSalvar.setText(tipoProduto == null ? "Inserir" : "Alterar");
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

        // Se nenhum tipo de produto foi passado por parâmetro, então é uma inserção
        if (tipoProduto == null) {
            buttonSalvar.setText("Inserir");
        } else { // Senão, é uma atualização
            buttonSalvar.setText("Alterar");
            preencheCampos();
        }

        // Adiciona todos os componentes
        this.add(labelDescricao);
        this.add(textFieldDescricao);
        this.add(buttonSalvar);
        this.add(buttonCancelar);

        this.setVisible(true);
    }
}

