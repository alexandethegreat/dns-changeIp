# DNS Change IP (Daemon Automático)

## Visão Geral

O **DNS Change IP** é um *daemon* (serviço em background) desenvolvido em Java 26, projetado para rodar em contêineres Docker.

Sua responsabilidade é monitorar e manter atualizado o apontamento de um IP dinâmico junto à plataforma ChangeIP.

O sistema opera de forma autônoma e otimizada:

1. Consulta o IP público atual da rede via `ipify.org`
2. Verifica internamente se o IP mudou desde a última checagem
3. Executa a atualização HTTP apenas quando necessário
4. Suporta *Graceful Shutdown* ao receber sinais do Docker

---

# Como Funciona

O núcleo da aplicação está no `DnsMonitorJob`, responsável por um loop contínuo de verificação baseado no valor configurado em `APP_TIME_CHECK`.

## Primeira Execução

Quando o contêiner inicia, o estado do último IP conhecido (`lastKnowIP`) começa vazio.

Por isso, na primeira execução do loop, a aplicação força uma atualização do DNS para garantir sincronização total entre o host configurado e o IP público atual.

Após essa sincronização inicial, novas requisições só serão realizadas caso o IP realmente mude.

---

# Estrutura do Projeto
```text
Dns-ChangeIP/
├── src/main/java/com/update/
│   ├── config/
│   │   └── AppConfig.java
│   │
│   ├── infra/
│   │   ├── client/
│   │   │   └── ChangeIpClient.java
│   │   │
│   │   └── network/
│   │       └── GetPublicIp.java
│   │
│   ├── job/
│   │   └── DnsMonitorJob.java
│   │
│   └── Main.java
│
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

# Responsabilidade dos Componentes

| Arquivo | Responsabilidade |
|---|---|
| `AppConfig.java` | Validação das variáveis de ambiente |
| `ChangeIpClient.java` | Comunicação com a API ChangeIP |
| `GetPublicIp.java` | Consulta do IP público |
| `DnsMonitorJob.java` | Monitoramento e atualização do DNS |
| `Main.java` | Inicialização e Shutdown Hook |

---

# Tecnologias Utilizadas

- **Java 26**
   - Records
   - `HttpClient` nativo
- **Maven 3.9**
- **Docker**
- **Docker Compose**
- **Alpine Linux**

---

# Variáveis de Ambiente

Toda configuração é feita via variáveis de ambiente no próprio `docker-compose.yml`.

| Variável | Tipo | Descrição | Exemplo |
|---|---|---|---|
| `APP_EMAIL` | String | Email da conta ChangeIP | `admin@exemplo.com` |
| `APP_PASSWORD` | String | Senha da conta | `senha123` |
| `APP_API_URL` | String | Endpoint da API | `https://nic.changeip.com/nic/update?hostname=` |
| `APP_YOUR_HOST` | String | Host/FQDN dinâmico | `teste.host.com` |
| `APP_TIME_CHECK` | Byte | Intervalo de checagem em minutos | `10` |

---

# Deploy e Execução

## 1. Clonar o Projeto

```bash
git clone <repositorio>
cd Dns-ChangeIP
```

---

## 2. Configurar o `docker-compose.yml`

Exemplo:

```yaml
    environment:
      APP_EMAIL: seuemail@gmail.com
      APP_PASSWORD: suasenha
      APP_API_URL: https://nic.changeip.com/nic/update?hostname=
      APP_YOUR_HOST: teste.host.com
      APP_TIME_CHECK: 10
```

---

## 3. Build e Inicialização

```bash
docker compose up -d --build
```

---

## 4. Monitoramento de Logs

```bash
docker logs -f dns-changeIP
```

### Exemplo esperado

```text
Serviço iniciado com sucesso.
Tentando atualizar host: teste.host.com
Iniciando processo de verificação e atualização
IP atualizado com sucesso
```

---

## 5. Encerrar Serviço

```bash
docker compose down
```

A aplicação executará *Graceful Shutdown* antes do encerramento completo.

---

# Otimizações Aplicadas

## Serial Garbage Collector

O Dockerfile utiliza:

```text
-XX:+UseSerialGC -Xms16m -Xmx32m
```

Isso reduz significativamente o consumo de RAM da JVM em cenários de baixo throughput.

---

# Graceful Shutdown

A aplicação utiliza:

```java
Runtime.getRuntime().addShutdownHook(...)
```

Isso permite que o Docker aguarde a conclusão da operação atual antes de finalizar a JVM.

Benefícios:

- Evita encerramento abrupto
- Finaliza requisições HTTP corretamente
- Mantém o desligamento seguro

---

# Segurança

- Nunca envie credenciais para o GitHub
- Restrinja permissões do contêiner
- Utilize senhas fortes
- Evite expor portas desnecessárias

---

# Possíveis Problemas

| Problema | Causa Comum | Solução |
|---|---|---|
| `UnknownHostException` | DNS inválido ou sem internet | Verificar conectividade |
| `401 Unauthorized` | Credenciais incorretas | Validar email e senha |
| `Connection timed out` | Timeout de rede | Verificar firewall ou internet |

---

# Licença

Projeto para automação de atualização de DNS dinâmico utilizando Java + Docker.