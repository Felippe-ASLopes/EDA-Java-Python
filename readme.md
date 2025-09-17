# Atividade - EDA

Este projeto demonstra a comunicação assíncrona entre um produtor Java/Spring Boot e um consumidor Python, utilizando o RabbitMQ como message broker.

## Integrantes

* Anilmar Yasmani Choque Orellana - 01241218
* Felippe Augusto Siqueira Lopes - 01241219

---

## 1. Como Subir o Ambiente

### Pré-requisitos
* Docker e Docker Compose.
* Java 21.
* Python 3.

### Passo a Passo

1.  **Iniciar o RabbitMQ:**
    Na raiz do projeto `produtor` execute o seguinte comando para iniciar o container do RabbitMQ em background:
    ```bash
    docker compose up -d
    ```
    Você pode acessar a interface de gerenciamento do RabbitMQ em `http://localhost:15672` (usuário: `myuser`, senha: `secret`).

2.  **Executar o Produtor (Java/Spring):**
    Abra o projeto `produtor` em sua IDE e execute a classe principal `ProdutorApplication.java`. O serviço estará disponível na porta `8080`.

3.  **Executar o Consumidor (Python):**
    Abra o projeto `consumidor` e instale as dependências:
    ```bash
    pip install -r requirements.txt
    ```
    Em seguida, execute a aplicação:
    ```bash
    python main.py
    ```
    O consumidor começará a escutar a fila do RabbitMQ e o endpoint de consulta estará disponível na porta `5000`.

---

## 2. Como Enviar uma Mensagem para Teste

Para publicar uma nova mensagem, você deve enviar uma requisição para o endpoint do produtor.

* **Método HTTP:** `POST`
* **URL do Endpoint:** `http://localhost:8080/api/personagens`
* **Headers:**
    * `Content-Type: application/json`
* **Exemplo de JSON (Body):**
    ```json
    {
      "nome": "Yasuo",
      "apelido": "The Unforgiven"
    }
    ```

---

## 3. Exemplo de Retorno e Verificação

### Retorno do Produtor

Após enviar a requisição `POST`, o produtor irá persistir o personagem no banco de dados, enviá-lo para a fila e retornar a seguinte resposta com o status `201 Created`:

```json
{
    "mensagem": "Personagem cadastrado com sucesso.",
    "personagem": {
        "id": 1,
        "nome": "Yasuo",
        "apelido": "The Unforgiven"
    }
}
```

## Verificando a Mensagem no Consumidor

O consumidor irá receber a mensagem da fila. Para visualizar todos os personagens que já foram consumidos, envie uma requisição GET para o endpoint do consumidor:

* **Método HTTP:** `GET`
* **URL do Endpoint:** `http://localhost:5000/personagens`

### Retorno esperado no Consumidor:

Uma lista contendo todos os personagens já recebidos.

```json
[
    {
        "id": 1,
        "nome": "Yasuo",
        "apelido": "The Unforgiven"
    }
]
```