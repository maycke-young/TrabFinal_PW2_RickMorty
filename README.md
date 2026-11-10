# TrabFinal_PW2_RickMorty

> **Armazenamento de missões — não implementado:** o armazenamento em banco de dados não foi desenvolvido porque esse requisito não constava na proposta inicial do projeto. Implementá-lo é uma melhoria interessante para uma próxima oportunidade.

## Roteiro de testes

> **Uso de inteligência artificial:** o conteúdo desta seção e das seguintes foi elaborado com auxílio de IA (Copilot Fusion, GPT-6 Luna).

O projeto é composto por três serviços Quarkus: `auth-service`, `status-personagem-service` e `missao-service`. O roteiro abaixo descreve verificações manuais dos endpoints.

### Pré-requisitos

- Java 25 instalado (versão configurada nos `pom.xml`).
- Acesso à internet para consultar a API pública do Rick and Morty.
- `curl` para os testes manuais. O `jq` é opcional.

### 1. Iniciar os serviços

Abra um terminal para cada comando e mantenha os processos em execução:

```bash
cd auth-service && ./mvnw quarkus:dev
cd status-personagem-service && ./mvnw quarkus:dev
cd missao-service && ./mvnw quarkus:dev
```

As portas HTTPS configuradas são `8445` (autenticação), `8444` (status) e `8443` (missões). Os certificados locais são autoassinados; por isso, os exemplos de `curl` usam `-k` apenas no ambiente de desenvolvimento.

### 2. Obter um token

O endpoint de autenticação recebe `user` e `password` como formulário e retorna um JWT em texto puro:

```bash
TOKEN=$(curl -k -sS -X POST https://localhost:8445/auth/token \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode 'user=teste' \
  --data-urlencode 'password=teste')
```

Use o token nas chamadas protegidas com `Authorization: Bearer $TOKEN`.

### 3. Testar `status-personagem-service`

1. **Personagem vivo** — consulte o personagem de ID `1` (Rick Sanchez):

   ```bash
   curl -k -i -X POST https://localhost:8444/status \
     -H "Authorization: Bearer $TOKEN" \
     -H 'Content-Type: application/json' \
     -d '{"id":1}'
   ```

   Esperado: HTTP `200`, `isAlive: true` e os dados do personagem.

2. **Personagem não vivo** — repita com o ID de um personagem cujo status na API seja `Dead` ou `unknown`.

   Esperado: HTTP `200`, `isAlive: false`; o objeto `personagem` não é preenchido.

3. **ID inexistente** — consulte um ID inexistente, por exemplo `-1`.

   Esperado: o serviço repassa o status de erro retornado pela API externa (normalmente HTTP `404`).

4. **Sem token** — repita uma consulta sem o cabeçalho `Authorization`.

   Esperado: acesso negado (HTTP `401`).

### 4. Testar `missao-service`

O estado das missões fica em memória; reiniciar o serviço limpa a lista. Faça os cenários nesta ordem:

1. **Listar missões** — `GET https://localhost:8443/missoes`, sem token.

   Esperado: HTTP `200` e uma lista JSON (inicialmente vazia).

2. **Criar missão** — envie uma missão sem tripulação:

   ```bash
   curl -k -i -X POST https://localhost:8443/missoes \
     -H "Authorization: Bearer $TOKEN" \
     -H 'Content-Type: application/json' \
     -d '{"titulo":"Teste","descricao":"Missão criada pelo roteiro"}'
   ```

   Esperado: HTTP `201`, ID gerado e `tripulacao` vazia ou nula. Anote o ID retornado para os próximos passos.

3. **Consultar missão** — `GET https://localhost:8443/missoes?id=<ID>`.

   Esperado: HTTP `200` e os dados da missão. Para um ID inexistente, esperado HTTP `404`.

4. **Adicionar tripulação viva** — troque `<ID>` pelo ID da missão criada e envie um ou mais IDs válidos de personagens vivos. Para adicionar vários personagens de uma vez, informe o parâmetro `idPersonagem=<idPersonagem>` uma vez para cada personagem:

   ```bash
   curl -k -i -X PUT "https://localhost:8443/missoes/<ID>/tripulantes" \
     -H "Authorization: Bearer $TOKEN" \
     -H 'Content-Type: application/x-www-form-urlencoded' \
     --data-urlencode 'idPersonagem=1' \
     --data-urlencode 'idPersonagem=2'
   ```

   Esperado: HTTP `201` e missão atualizada com a tripulação.

5. **Tripulação inválida** — repita a atualização com um personagem morto (esperado HTTP `409`) e com um ID inexistente (esperado HTTP `404`). Em ambos os casos, confira a mensagem de erro e que a tripulação anterior não foi substituída parcialmente.

6. **Missão inexistente** — tente atualizar tripulantes de um ID de missão que não exista.

   Esperado: HTTP `404`.

7. **Remover missão** — `DELETE https://localhost:8443/missoes/<ID>` com token.

   Esperado: HTTP `204`. Consulte a missão novamente e confirme HTTP `404`. Ao tentar excluir um ID inexistente, esperado HTTP `404`.

8. **Acesso protegido** — repita criação, atualização e exclusão sem token.

   Esperado: acesso negado (HTTP `401`). A listagem e a consulta de missões devem continuar disponíveis sem token.

### 5. Registrar resultados

Para cada cenário, registre o resultado obtido, o status HTTP e eventuais diferenças em relação ao esperado.
