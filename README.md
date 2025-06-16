# Parking Management API

## Requisitos

Antes de rodar a aplicação, certifique-se de ter instalado e configurado:

- **Java**: JDK 21  
- **Spring Boot**: 3.2.5 (via `spring-boot-starter-parent`)  
- **Maven**: 3.6+  
- **Banco de dados**: PostgreSQL 12+  
- **Migrations**:  
  1. Execute `database-migrations-2025-06-14.sql` para criar schemas e tabelas  
  2. Execute `data-migrations-2025-06-14.sql` para popular dados iniciais  
- **Porta da aplicação**: `3003` (configurado em `application.properties`)  

---

## Funcionalidades

- **Setores**  
  - Obter informações de todos os setores e vagas (`GET /garage`)  
  - Calcular receita diária por setor (`POST /revenue`)

- **Vagas**  
  - Verificar status de vaga por coordenadas (`POST /spot-status`)

- **Controle de Fluxo**  
  - Registrar entrada, estacionamento e saída de veículos (`POST /webhook`)  
  - Consultar status da placa dentro do estacionamento (`POST /plate-status`)

---

## Modelagem no Banco de Dados

1. **Sector**  
   - `sector` (PK, char)  
   - `currency` (string)  
   - `base_price` (decimal)  
   - `max_capacity` (int)  
   - `open_hour`, `close_hour` (time)  
   - `duration_limit_minutes` (int)  
   - `current_occupancy` (int)  

2. **Spot**  
   - `id_spot` (PK, long)  
   - `lat`, `lng` (decimal)  
   - `sector` (FK → Sector)  
   - `occupied` (boolean)  

3. **SpotOccupancy**  
   - `id_occupancy` (PK, long)  
   - `license_plate` (string)  
   - `entry_time`, `exit_time` (timestamp)  
   - `price_per_hour` (decimal)  
   - `final_price` (decimal)  
   - `spot_id` (FK → Spot)  

> **Obs.**: `price_per_hour` é armazenado na entrada para fixar o valor já com desconto, e `current_occupancy` em Sector agiliza cálculos de lotação e descontos.

---

## Documentação Swagger

Após rodar a aplicação é possível acessar a documentação através de:

```
http://localhost:3003/swagger-ui/index.html
```

---

## Melhoria Proposta

**Associar setor já na entrada (ENTRY)

- Ao registrar ENTRY, já informar o setor na requisição.

- Permite validar lotação e calcular desconto/preço por hora imediatamente, evitando situações de vaga indisponível ou setor cheio apenas no evento de PARKED.

**Observação em relação ao endpoint '/revenue'

- No requisito o endpoint foi especificado como um GET, porém pelo fato de receber um json durante sua requisição, foi entendido que seria melhor (por boas práticas) utilizar um método POST já que ele possui um corpo enviado junto a requisição.

---

**Desenvolvido por Marcos Alencar**
