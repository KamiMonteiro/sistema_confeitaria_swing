package utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.Usuario;
import data.Encomenda;
import data.ListaEncomendas;
import data.TipoProduto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

public final class SistemaConfeitaria {
    private static Usuario usuarioLogado;
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<TipoProduto> tiposProdutos = new ArrayList<>();
    private static final String ARQUIVO_JSON = "dados.json";
    private static final String ARQUIVO_TIPOS_JSON = "tipos_produtos.json";

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuarioLogado_) {
        usuarioLogado = usuarioLogado_;
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static ListaEncomendas getListaEncomendasUsuarioLogado() {
        Usuario usuarioLogado = SistemaConfeitaria.getUsuarios().get(SistemaConfeitaria.getUsuarios().indexOf(getUsuarioLogado()));
        return usuarioLogado.getListaEncomendas();
    }

    // Métodos para gerenciar tipos de produtos
    public static List<TipoProduto> getTiposProdutos() {
        return tiposProdutos;
    }

    public static void setTiposProdutos(List<TipoProduto> tiposProdutos) {
        SistemaConfeitaria.tiposProdutos = tiposProdutos;
    }

    // Método para serializar a lista de tipos de produtos em um arquivo JSON
    public static void serializarTiposProdutos() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(ARQUIVO_TIPOS_JSON)) {
            gson.toJson(tiposProdutos, writer);
            System.out.println("Tipos de produtos serializados com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para desserializar o arquivo JSON e popular a lista de tipos de produtos
    public static void desserializarTiposProdutos() {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(ARQUIVO_TIPOS_JSON)) {
            Type tiposListType = new TypeToken<List<TipoProduto>>() {}.getType();
            tiposProdutos = gson.fromJson(reader, tiposListType);
            if (tiposProdutos == null) {
                tiposProdutos = new ArrayList<>();
                // Inicializa com tipos padrão se a lista estiver vazia
                inicializarTiposProdutosPadrao();
            }
            System.out.println("Tipos de produtos desserializados com sucesso! Total: " + tiposProdutos.size());
        } catch (IOException e) {
            // Se o arquivo não existir, inicializa com tipos padrão
            tiposProdutos = new ArrayList<>();
            inicializarTiposProdutosPadrao();
            serializarTiposProdutos();
        } catch (Exception e) {
            e.printStackTrace();
            tiposProdutos = new ArrayList<>();
            inicializarTiposProdutosPadrao();
        }
    }

    // Inicializa com tipos de produtos padrão
    private static void inicializarTiposProdutosPadrao() {
        if (tiposProdutos.isEmpty()) {
            tiposProdutos.add(new TipoProduto(1, "Bolo"));
            tiposProdutos.add(new TipoProduto(2, "Torta"));
            tiposProdutos.add(new TipoProduto(3, "Docinhos"));
            tiposProdutos.add(new TipoProduto(4, "Cupcake"));
            tiposProdutos.add(new TipoProduto(5, "Brigadeiro"));
            tiposProdutos.add(new TipoProduto(6, "Beijinho"));
            tiposProdutos.add(new TipoProduto(7, "Bem Casado"));
            tiposProdutos.add(new TipoProduto(8, "Outros"));
        }
    }

    // Método removido - não é mais necessário

    // Método removido - não é mais necessário popular dados de agenda
    // O sistema agora trabalha apenas com encomendas cadastradas pelo usuário

// Método para serializar a lista de usuários em um arquivo JSON
    public static void serializarUsuarios() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();  // Gson com formatação "bonita"
        try (FileWriter writer = new FileWriter(ARQUIVO_JSON)) {
            gson.toJson(usuarios, writer);
            System.out.println("Usuários serializados com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para desserializar o arquivo JSON e popular a lista de usuários
    public static void desserializarUsuarios() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        
        // Adiciona adaptador customizado para parsear datas do formato do JSON
        gsonBuilder.registerTypeAdapter(Date.class, criarAdaptadorData());
        
        // Adiciona adaptador customizado para parsear TipoProduto (pode vir como string do JSON antigo)
        gsonBuilder.registerTypeAdapter(TipoProduto.class, criarAdaptadorTipoProduto());
        
        Gson gson = gsonBuilder.create();
        try (FileReader reader = new FileReader(ARQUIVO_JSON)) {
            Type usuariosListType = new TypeToken<List<Usuario>>() {}.getType();
            usuarios = gson.fromJson(reader, usuariosListType);
            if (usuarios == null) {
                usuarios = new ArrayList<>();
            }
            // Converte tipos de produtos antigos (strings) para objetos
            converterTiposProdutosAntigos();
            System.out.println("Usuários desserializados com sucesso! Total: " + usuarios.size());
        } catch (IOException e) {
            e.printStackTrace();
            usuarios = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            usuarios = new ArrayList<>();
        }
    }

    // Adaptador para converter strings antigas do JSON em objetos TipoProduto
    private static com.google.gson.JsonDeserializer<TipoProduto> criarAdaptadorTipoProduto() {
        return new com.google.gson.JsonDeserializer<TipoProduto>() {
            @Override
            public TipoProduto deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, 
                    com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
                // Se for uma string (formato antigo), converte
                if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                    String descricaoAntiga = json.getAsString();
                    // Mapeia os valores antigos do enum para as descrições
                    java.util.Map<String, String> mapeamento = new java.util.HashMap<>();
                    mapeamento.put("BOLO", "Bolo");
                    mapeamento.put("TORTA", "Torta");
                    mapeamento.put("DOCINHOS", "Docinhos");
                    mapeamento.put("CUPCAKE", "Cupcake");
                    mapeamento.put("BRIGADEIRO", "Brigadeiro");
                    mapeamento.put("BEIJINHO", "Beijinho");
                    mapeamento.put("BEM_CASADO", "Bem Casado");
                    mapeamento.put("OUTROS", "Outros");
                    
                    String descricao = mapeamento.getOrDefault(descricaoAntiga, descricaoAntiga);
                    
                    // Procura na lista de tipos de produtos existentes
                    for (TipoProduto tp : tiposProdutos) {
                        if (tp.getDescricao().equalsIgnoreCase(descricao)) {
                            return tp;
                        }
                    }
                    
                    // Se não encontrou, cria um novo (não deveria acontecer, mas por segurança)
                    return new TipoProduto(descricao);
                }
                
                // Se já for um objeto, desserializa normalmente
                return context.deserialize(json, TipoProduto.class);
            }
        };
    }

    // Converte tipos de produtos antigos (strings) para objetos TipoProduto nas encomendas
    private static void converterTiposProdutosAntigos() {
        for (Usuario usuario : usuarios) {
            for (Encomenda encomenda : usuario.getListaEncomendas().getEncomendas()) {
                if (encomenda.getTipoProduto() != null) {
                    // Se o tipo de produto não está na lista, tenta encontrar ou criar
                    TipoProduto tipoEncomenda = encomenda.getTipoProduto();
                    boolean encontrado = false;
                    
                    for (TipoProduto tipo : tiposProdutos) {
                        if (tipo.getDescricao().equalsIgnoreCase(tipoEncomenda.getDescricao())) {
                            encomenda.setTipoProduto(tipo);
                            encontrado = true;
                            break;
                        }
                    }
                    
                    // Se não encontrou, adiciona à lista
                    if (!encontrado && tipoEncomenda.getDescricao() != null) {
                        int novoId = tiposProdutos.isEmpty() ? 1 : 
                            tiposProdutos.stream().mapToInt(TipoProduto::getId).max().orElse(0) + 1;
                        tipoEncomenda.setId(novoId);
                        tiposProdutos.add(tipoEncomenda);
                        encomenda.setTipoProduto(tipoEncomenda);
                    }
                }
            }
        }
    }

    // Cria um adaptador customizado para parsear datas em diferentes formatos
    private static com.google.gson.JsonDeserializer<Date> criarAdaptadorData() {
        return new com.google.gson.JsonDeserializer<Date>() {
            @Override
            public Date deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT, 
                    com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
                String dateStr = json.getAsString();
                
                // Limpa a string removendo vírgulas e normalizando espaços
                String dateStrFixed = dateStr.replace(",", "")
                                             .replaceAll("[^a-zA-Z0-9\\s:/-]", "")
                                             .replaceAll("\\s+", " ")
                                             .trim();
                
                // Formatos de data suportados
                String[] formats = {
                    "MMM d yyyy h:mm:ss a",
                    "MMM dd yyyy h:mm:ss a",
                    "MMM d, yyyy h:mm:ss a",
                    "MMM dd, yyyy h:mm:ss a",
                    "dd/MM/yyyy",
                    "EEE MMM d HH:mm:ss zzz yyyy",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
                };
                
                // Tenta parsear com a string limpa
                for (String format : formats) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format, java.util.Locale.ENGLISH);
                        sdf.setLenient(false);
                        return sdf.parse(dateStrFixed);
                    } catch (Exception e) {
                        // Continua tentando outros formatos
                    }
                }
                
                // Tenta parsear com a string original (mais flexível)
                for (String format : formats) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(format, java.util.Locale.ENGLISH);
                        sdf.setLenient(true);
                        return sdf.parse(dateStr);
                    } catch (Exception e) {
                        // Continua tentando outros formatos
                    }
                }
                
                // Tenta parsing manual extraindo componentes
                try {
                    String[] parts = dateStrFixed.split("\\s+");
                    if (parts.length >= 6) {
                        String month = parts[0];
                        String day = parts[1];
                        String year = parts[2];
                        String time = parts[3] + " " + parts[4] + " " + parts[5];
                        String manualDate = month + " " + day + " " + year + " " + time;
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy h:mm:ss a", java.util.Locale.ENGLISH);
                        return sdf.parse(manualDate);
                    }
                } catch (Exception e) {
                    // Ignora e continua
                }
                
                // Fallback: retorna data atual se não conseguir parsear
                return new Date();
            }
        };
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Serializando os dados dos usuários para um arquivo JSON
        serializarUsuarios();

        // Limpando os dados atuais da lista de usuários
        getUsuarios().clear();

        // Desserializando os dados do arquivo JSON e carregando na lista de usuários
        desserializarUsuarios();

        // Exibindo dados dos usuários e suas encomendas
        for (Usuario usuario : getUsuarios()) {
            System.out.println("Usuário: " + usuario.getLogin());
            System.out.println("Total de encomendas: " + usuario.getListaEncomendas().getEncomendas().size());
            
            for (Encomenda encomenda : usuario.getListaEncomendas().getEncomendas()) {
                System.out.println("  Cliente: " + encomenda.getNomeCliente());
                System.out.println("  Telefone: " + encomenda.getTelefoneWhatsApp());
                System.out.println("  Produto: " + encomenda.getTipoProduto());
                System.out.println("  Sabor: " + encomenda.getSabor());
                System.out.println("  Tamanho/Peso: " + encomenda.getTamanhoPeso());
                System.out.println("  Data de Entrega: " + (encomenda.getDataEntrega() != null ? sdf.format(encomenda.getDataEntrega()) : "N/A"));
                System.out.println("  Hora: " + encomenda.getHoraEntrega());
                System.out.println("  Valor: R$ " + String.format("%.2f", encomenda.getValor()));
                System.out.println("  Sinal Pago: R$ " + String.format("%.2f", encomenda.getSinalPago()));
                System.out.println("  Valor Restante: R$ " + String.format("%.2f", encomenda.getValorRestante()));
                System.out.println("  Status: " + encomenda.getStatus());
                if (encomenda.getObservacoes() != null && !encomenda.getObservacoes().isEmpty()) {
                    System.out.println("  Observações: " + encomenda.getObservacoes());
                }
                for(int i = 0; i < 50; i++){
                    System.out.print("-");
                }
                System.out.println();
            }
        }
    }
}

