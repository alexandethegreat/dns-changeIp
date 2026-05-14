# Documentação — DNS Change IP

## Visão Geral

O projeto **DNS Change IP** é um script Java executado em Docker responsável por atualizar automaticamente o IP de um host DNS dinâmico utilizando a API do ChangeIP.

O sistema:

1. Obtém o IP atual configurado no DNS.
2. Obtém o IP público atual da máquina.
3. Compara os dois IPs.
4. Caso sejam diferentes, envia uma requisição para atualizar o DNS automaticamente.

---

# Estrutura do Projeto

```text
Dns-ChangeIP/
│
├── src/
│   └── main/
│       └── java/
│           └── com.update/
│               ├── Main.java
│               └── UpdateIp.java
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

# Tecnologias Utilizadas

* Java 26
* Maven
* Docker
* Docker Compose
* ChangeIP Dynamic DNS API

---

# Funcionamento

O script executa continuamente em loop.

A cada intervalo configurado:

1. Resolve o IP do host DNS.
2. Consulta o IP público da máquina usando:

   ```text
   https://api.ipify.org
   ```
3. Compara os valores.
4. Se o IP mudou:

    * envia atualização para:

   ```text
   https://nic.changeip.com/nic/update
   ```

---

# Classes

## Classe `Main`

Responsável por:

* Ler variáveis de ambiente
* Validar configurações
* Iniciar o loop principal
* Executar verificações periódicas

### Variáveis de Ambiente

| Variável         | Descrição                    |
| ---------------- | ---------------------------- |
| `APP_EMAIL`      | Email da conta ChangeIP      |
| `APP_PASSWORD`   | Senha da conta               |
| `APP_API_URL`    | URL da API do ChangeIP       |
| `APP_YOUR_HOST`  | Host DNS que será atualizado |
| `APP_TIME_CHECK` | Intervalo em minutos         |

---

## Classe `UpdateIp`

Responsável por:

* Buscar IP DNS atual
* Buscar IP público
* Comparar IPs
* Atualizar DNS quando necessário

### Fluxo da Classe

```text
1. Busca IP do host DNS
2. Busca IP público
3. Compara os IPs
4. Se diferente:
      envia atualização
5. Exibe logs no console
```

---

# Docker

## Dockerfile

O projeto utiliza build multi-stage:

### Etapa 1 — Build

Compila o projeto usando Maven.

```dockerfile
FROM maven:3.9-eclipse-temurin-26-alpine AS build
```

### Etapa 2 — Runtime

Executa apenas o `.jar` final em uma imagem leve.

```dockerfile
FROM eclipse-temurin:26-jre-alpine
```

---

# Docker Compose

## Configuração

```yaml
services:
  meu-script-java:
    build: .
    image: dns:changeIp
    container_name: dns-changeIP
    restart: always

    environment:
      - APP_EMAIL=
      - APP_PASSWORD=
      - APP_API_URL=https://nic.changeip.com/nic/update?hostname=
      - APP_YOUR_HOST=
      - APP_TIME_CHECK=10
```

---

# Como Executar

## 1. Clonar Projeto

```bash
git clone <repositorio>
cd Dns-ChangeIP
```

---

## 2. Configurar Variáveis

Editar o arquivo `docker-compose.yml`:

```yaml
environment:
  - APP_EMAIL=seuemail@email.com
  - APP_PASSWORD=suasenha
  - APP_API_URL=https://nic.changeip.com/nic/update?hostname=
  - APP_YOUR_HOST=seudominio.ddns.net
  - APP_TIME_CHECK=10
```

---

## 3. Build do Container

```bash
docker compose build
```

---

## 4. Iniciar Aplicação

```bash
docker compose up -d
```

---

## 5. Ver Logs

```bash
docker logs -f dns-changeIP
```

---

# Logs Esperados

## Quando o IP não mudou

```text
Entering UpdateIp
IP Public Response: 192.168.0.1
O ip não mudou
```

---

## Quando o IP é atualizado

```text
Entering UpdateIp
IP Public Response: 192.168.0.2
BODY: Successful Update
```

---

# Segurança

## Recomendações

* Nunca commitar credenciais no GitHub.
* Utilize `.env` para variáveis sensíveis.
* Utilize tokens ou autenticação mais segura quando possível.

---

# Melhorias Futuras

* Adicionar logs estruturados
* Implementar retry automático
* Adicionar suporte para múltiplos hosts
* Adicionar monitoramento
* Criar healthcheck Docker
* Adicionar testes unitários
* Implementar cache do último IP
* Suporte IPv6

---

# Exemplo com `.env`

## Arquivo `.env`

```env
APP_EMAIL=seuemail@email.com
APP_PASSWORD=suasenha
APP_API_URL=https://nic.changeip.com/nic/update?hostname=
APP_YOUR_HOST=seudominio.ddns.net
APP_TIME_CHECK=10
```

## docker-compose.yml

```yaml
services:
  meu-script-java:
    build: .
    image: dns:changeIp
    container_name: dns-changeIP
    restart: always
    env_file:
      - .env
```

---

# Possíveis Problemas

| Problema                    | Causa                      |
| --------------------------- | -------------------------- |
| `Erro: Variáveis inválidas` | Variáveis não configuradas |
| `UnknownHostException`      | Host inválido              |
| `401 Unauthorized`          | Credenciais incorretas     |
| `Connection timed out`      | Problema de rede           |

---

# Licença

Projeto para automação de atualização DNS dinâmico utilizando Java + Docker.
