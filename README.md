# Sistema de Gerenciamento de Encomendas - Confeitaria

Sistema desktop desenvolvido em Java para gerenciar encomendas de uma confeitaria.

## O que o sistema faz

### Funcionalidades Principais

1. **Gerenciamento de Encomendas**
   - Cadastrar novas encomendas com informações do cliente, produto, valores e datas
   - Consultar encomendas com filtros de busca
   - Editar encomendas existentes
   - Excluir encomendas

2. **Gerenciamento de Tipos de Produtos**
   - Cadastrar novos tipos de produtos (Bolo, Torta, Docinhos, etc.)
   - Editar tipos existentes
   - Excluir tipos de produtos (com validação para não excluir se estiver em uso)

3. **Relatórios**
   - Gerar relatórios detalhados das encomendas
   - Filtrar por status, tipo de produto e período
   - Visualizar estatísticas de valores e quantidades

4. **Sistema de Usuários**
   - Login com autenticação
   - Cada usuário possui sua própria lista de encomendas

### Dados Armazenados

- **Encomendas**: Nome do cliente, telefone, tipo de produto, sabor, tamanho, data/hora de entrega, valores, status e observações
- **Tipos de Produtos**: Lista de tipos cadastrados (Bolo, Torta, Docinhos, etc.)
- **Usuários**: Login e senha para acesso ao sistema

## Organização das Pastas

```
sistema_confeitaria-main/
│
├── src/                          # Código-fonte do sistema
│   │
│   ├── data/                     # Classes que representam os dados
│   │   ├── Encomenda.java       # Representa uma encomenda
│   │   ├── Usuario.java         # Representa um usuário do sistema
│   │   ├── ListaEncomendas.java # Lista de encomendas de um usuário
│   │   ├── StatusEncomenda.java # Status possíveis (Pendente, Em Produção, Pronto, Entregue)
│   │   └── TipoProduto.java     # Tipo de produto (Bolo, Torta, etc.)
│   │
│   ├── screen/                   # Telas da aplicação
│   │   ├── Login.java           # Tela de login
│   │   ├── App.java             # Tela principal com menu
│   │   ├── ConsultaEncomendas.java      # Tela para ver e buscar encomendas
│   │   ├── ManutencaoEncomendas.java   # Tela para cadastrar/editar encomendas
│   │   ├── RelatoriosEncomendas.java   # Tela de relatórios
│   │   ├── ConsultaTiposProdutos.java  # Tela para ver tipos de produtos
│   │   └── ManutencaoTiposProdutos.java # Tela para cadastrar/editar tipos
│   │
│   └── utils/                    # Classes auxiliares
│       ├── SistemaConfeitaria.java    # Classe principal que gerencia o sistema
│       ├── EncomendaTableModel.java   # Modelo para exibir encomendas em tabela
│       ├── TipoProdutoTableModel.java  # Modelo para exibir tipos em tabela
│       └── Utilitario.java            # Funções auxiliares e validações
│
├── lib/                          # Bibliotecas externas
│   └── gson-2.8.2.jar          # Biblioteca para trabalhar com JSON
│
├── bin/                          # Arquivos compilados (gerados automaticamente)
│
├── dados.json                    # Arquivo com dados dos usuários e encomendas
├── tipos_produtos.json          # Arquivo com os tipos de produtos cadastrados
│
└── README.md                     # Este arquivo
```

## Descrição das Pastas

### `src/data/`
Contém as classes que representam os dados do sistema:
- **Encomenda**: Todas as informações de uma encomenda
- **Usuario**: Dados de login e lista de encomendas do usuário
- **TipoProduto**: Tipo de produto que pode ser cadastrado
- **StatusEncomenda**: Enum com os status possíveis de uma encomenda

### `src/screen/`
Contém todas as telas da aplicação:
- **Login**: Tela inicial para fazer login no sistema
- **App**: Tela principal com menu de navegação
- **ConsultaEncomendas**: Lista todas as encomendas com opção de filtro
- **ManutencaoEncomendas**: Formulário para cadastrar ou editar encomendas
- **RelatoriosEncomendas**: Gera relatórios com filtros e estatísticas
- **ConsultaTiposProdutos**: Lista os tipos de produtos cadastrados
- **ManutencaoTiposProdutos**: Formulário para cadastrar ou editar tipos

### `src/utils/`
Contém classes auxiliares:
- **SistemaConfeitaria**: Gerencia todo o sistema (carrega/salva dados, gerencia usuário logado)
- **EncomendaTableModel**: Configura como as encomendas aparecem na tabela
- **TipoProdutoTableModel**: Configura como os tipos aparecem na tabela
- **Utilitario**: Funções para validar dados (telefone, data, email, etc.)

### Arquivos JSON
- **dados.json**: Armazena todos os usuários e suas encomendas
- **tipos_produtos.json**: Armazena os tipos de produtos cadastrados

## Como Executar

1. Abra o projeto em uma IDE Java (VS Code, IntelliJ, Eclipse)
2. Execute a classe `src/screen/App.java`
3. Use as credenciais padrão:
   - Login: `usuario0`
   - Senha: `senha0`

## Tecnologias Utilizadas

- **Java**: Linguagem de programação
- **Java Swing**: Interface gráfica
- **Gson**: Biblioteca para trabalhar com arquivos JSON
