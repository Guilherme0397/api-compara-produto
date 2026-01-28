# Item Comparison

**Descrição**  
Aplicação backend em Java (Spring Boot) que demonstra uma API REST para comparação de produtos, com suporte a validação, paginação, filtros por especificação (Specification pattern) e persistência em banco H2 (modo em memória por padrão). Projeto estruturado com Maven.

## Funcionalidades principais

- CRUD para Produtos.
- CRUD para Categorias.
- Paginação e ordenação nos endpoints de listagem.
- Filtros dinâmicos usando SpecificationTemplate (consultas com critérios).
- Validação de entrada usando Jakarta Validation.
- Tratamento centralizado de exceções (GlobalExceptionHandler).
- Banco em memória H2 para desenvolvimento e testes.

## Tecnologias utilizadas

- Java 21
- Spring Boot (Web, Data JPA)
- Maven
- PostgreSQL
- H2 Database
- Jakarta Validation
- Spring Data Specifications

## Pré-requisitos

- Java 21 JDK
- Maven 3.6+
- PostgreSQL (Docker)



## Endpoints de Produtos

**Base URL:** `http://localhost:8082/comparison`
### POST Criar Produto

`POST` `http://localhost:8082/comparison/produtos`

#### Request Body

```json
{
  "nome": "Samsung A1",
  "urlImagem": "http://example.com/image.png",
  "descricao": "Celular de entrada",
  "preco": 899.99,
  "classificacao": 3,
  "categoria": {
    "idCategoria": "8d0dce8f-dd15-4f03-b24f-527dafc4f6bf",
    "nome": "Celular"
  },
  "especificacoes": [
    {
      "nomeAtributo": "RAM",
      "valorAtributo": "8GB"
    },
    {
      "nomeAtributo": "COR",
      "valorAtributo": "Vermelho"
    }
  ]
}
```

### GET Detalhe Produto (UUID)

`GET` `http://localhost:8082/comparison/produtos/99f7ac93-58ba-435e-b913-a08f27aab27f`

### GET Listar/Paginacao/Filtro

`GET` `http://localhost:8082/comparison/produtos`

#### Query Parameters

| Name | Example Value | Description |
|------|---------------|-------------|
| `especificacoes.nomeAtributo` | `Memoria` |  |
| `especificacoes.valorAtributo` | `Vermelho` |  |

### GET Comparar Produtos (Lista UUIDs)

`GET` `http://localhost:8082/comparison/produtos/comparar?ids=9df16c5d-997c-409c-adcf-4c45b6d1fdeb&ids=06defd4d-5ec8-4405-8dcd-a47de8281c5d`

### DELETE Deletar Produto

`DELETE` `http://localhost:8082/comparison/produtos/99f7ac93-58ba-435e-b913-a08f27aab27f`

### PUT Atualizar Produto

`PUT` `http://localhost:8082/comparison/produtos/`

#### Request Body

```json
{
  "nome": "Smartphone Exemplo ATUALIZADO",
  "urlImagem": "http://example.com/image.png",
  "descricao": "Descrição detalhada do produto, agora atualizada.",
  "preco": 699.99,
  "classificacao": 4.3,
  "categoria": {
    "id": "{{categoria_uuid_existente}}"
  },
  "especificacoes": []
}
```

### GET ListarPorCategoria

`GET` `http://localhost:8082/comparison/produtos/categoria/12794d9f-2d48-4cd7-8ea1-5217e3028c87`

### GET Listar Categoria Filtrada

`GET` `http://localhost:8082/comparison/produtos/categoria/12794d9f-2d48-4cd7-8ea1-5217e3028c87/filtrar`

#### Query Parameters

| Name | Example Value | Description |
|------|---------------|-------------|
| `page` | `0` |  |
| `size` | `10` |  |
| `nomeAtributo` | `COR` |  |
| `valorAtributo` | `Vermelho` |  |

## Endpoints de Categorias

Endpoints para gerenciar categorias

### Listar Todas Categorias

`GET` `http://localhost:8082/comparison/categorias`

### Buscar Categoria por ID

`GET` `http://localhost:8082/comparison/categorias/509f9ece-81a7-4722-99b9-40fbb232f313`

### Criar Categoria

`POST` `http://localhost:8082/comparison/categorias`

#### Request Body

```json
{
  "nome": "Celular"
}
```

### Atualizar Categoria

`PUT` `http://localhost:8082/comparison/categorias/`

#### Request Body

```json
{
  "nome": "Categoria Atualizada"
}
```

### Deletar Categoria

`DELETE` `http://localhost:8082/comparison/categorias/`



