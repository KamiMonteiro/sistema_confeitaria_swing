package screen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import utils.SistemaConfeitaria;

public class App extends JFrame {
    public App(){
        super();
        initialize();
    }

    private void initialize()
    {
        this.setTitle("Sistema de Encomendas - Confeitaria: " + SistemaConfeitaria.getUsuarioLogado().getLogin());
        this.setSize(800, 600);
        this.setLayout(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setJMenuBar(menu());
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private JMenuBar menu(){
        JMenuBar menuBar = new JMenuBar();

        JMenu manutencao = new JMenu("Manutenção");
        JMenu ajuda = new JMenu("Ajuda");
        menuBar.add(manutencao);
        menuBar.add(ajuda);

        JMenuItem encomendas = new JMenuItem("Encomendas");
        JMenuItem tiposProdutos = new JMenuItem("Tipos de Produtos");
        JMenuItem relatorios = new JMenuItem("Relatórios");
        JMenuItem sair = new JMenuItem("Sair");
        JMenuItem ajudaItem = new JMenuItem("Ajuda");
        JMenuItem sobre = new JMenuItem("Sobre...");
        
        encomendas.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new ConsultaEncomendas();
                }
            }
        );
        
        tiposProdutos.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new ConsultaTiposProdutos();
                }
            }
        );
        
        relatorios.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new RelatoriosEncomendas();
                }
            }
        );
        
        ajudaItem.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String ajuda = "SISTEMA DE GERENCIAMENTO DE ENCOMENDAS\n\n" +
                                  "Funcionalidades:\n" +
                                  "• Cadastro de Encomendas\n" +
                                  "• Consulta e Filtro de Encomendas\n" +
                                  "• Edição de Encomendas\n" +
                                  "• Exclusão de Encomendas\n" +
                                  "• Relatórios Detalhados\n\n" +
                                  "Para usar:\n" +
                                  "1. Acesse 'Manutenção > Encomendas' para gerenciar\n" +
                                  "2. Use 'Manutenção > Relatórios' para visualizar estatísticas";
                    JOptionPane.showMessageDialog(null, ajuda, "Ajuda", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        );
        
        sobre.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String sobre = "Sistema de Gerenciamento de Encomendas de Confeitaria\n\n" +
                                  "Versão 1.0\n" +
                                  "Desenvolvido em Java com Swing\n" +
                                  "Persistência de dados em JSON";
                    JOptionPane.showMessageDialog(null, sobre, "Sobre", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        );
        
        manutencao.add(encomendas);
        manutencao.add(tiposProdutos);
        manutencao.add(relatorios);
        manutencao.addSeparator();
        manutencao.add(sair);
        ajuda.add(ajudaItem);
        ajuda.addSeparator();
        ajuda.add(sobre);

        sair.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String message = "Deseja realmente sair?";
                    String title = "Sair";
                    int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                
                    if(reply == JOptionPane.YES_OPTION){
                        System.exit(0);
                    }
                }
            }
        );

        return menuBar;
    }

    public static void main(String[] args) throws Exception {
        new Login();
    }
}