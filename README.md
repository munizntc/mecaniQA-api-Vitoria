# mecaniQA-api
Repositório da API da empresa fictícia mecaniQA na disciplina de Desenvolvimento Web Orientado a Objetos na Unex

Equipe Vitória: Diego Ailton Silva Barbosa, Pedro Henrique Costa, Alexandre Muniz, Ângelo de Carvalho e Júlio César Aragão Santana.

## Como rodar

```
./gradlew bootRun
```

No Windows: `gradlew.bat bootRun`. A API sobe em `http://localhost:8080`.

## Estrutura

- `model` — entidades (Peca, Servico, OrdemServico com Builder, PedidoPecas, ItemPedido) e enums
- `dto` — objetos que entram e saem da API
- `mapper` — conversão DTO ↔ entidade
- `repository` — persistência em memória (Singleton, acessado por `getInstance()`)
- `controller` — rotas REST

## Rotas

| Recurso | Verbo | Rota | Status |
|---|---|---|---|
| Peça | POST | /api/pecas | 201 / 400 |
| Peça | GET | /api/pecas | 200 |
| Peça | GET | /api/pecas/{id} | 200 / 404 |
| Peça | PUT | /api/pecas/{id} | 200 / 400 / 404 |
| Peça | DELETE | /api/pecas/{id} | 204 / 404 |
| Serviço | POST | /api/servicos | 201 / 400 |
| Serviço | GET | /api/servicos | 200 |
| Serviço | GET | /api/servicos/{id} | 200 / 404 |
| Serviço | PUT | /api/servicos/{id} | 200 / 400 / 404 |
| Serviço | DELETE | /api/servicos/{id} | 204 / 404 |
| Ordem de Serviço | POST | /api/ordens-servico | 201 / 400 / 404 |
| Ordem de Serviço | GET | /api/ordens-servico | 200 |
| Ordem de Serviço | GET | /api/ordens-servico/{id} | 200 / 404 |
| Ordem de Serviço | PUT | /api/ordens-servico/{id}/status | 200 / 400 / 404 |
| Pedido de Peças | POST | /api/pedidos | 201 / 400 / 404 |
| Pedido de Peças | GET | /api/pedidos | 200 |
| Pedido de Peças | GET | /api/pedidos/{id} | 200 / 404 |
| Pedido de Peças | POST | /api/pedidos/{id}/itens | 200 / 400 / 404 / 409 |
| Pedido de Peças | PUT | /api/pedidos/{id}/status | 200 / 400 / 404 |

## Entregáveis

- `apresentacoes/` — apresentações das OATs
- `docs/diagramas/` — diagramas de classe e de atividade
- `collection/` — collection do Postman com todas as rotas
