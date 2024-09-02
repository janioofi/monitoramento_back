# Back-end do sistema Monitoramento de Dispositivo 💻

## O que é este projeto
Este projeto é uma API Rest para um sistema de monitoramento de dispositivos conectados à internet. A aplicação permite o gerenciamento de dispositivos, visualização de status, configuração de alertas e geração de logs. Os alertas são aplicáveis a todos os dispositivos, e o sistema é projetado para enviar notificações por e-mail quando um alerta é disparado.
## Tecnologias Utilizadas
- [Front-end](https://github.com/janioofi/monitoramento_front)
    - JavaScript
    - TypeScript
    - Angular
    - HTML
    - CSS
    - Bootstrap
- Back-end
    - Java 17
    - Spring Boot
    - Maven
    - Spring Data
    - Spring Security
    - Spring Mail
    - PostgreSQL
    - JWT

## Documentação
```
https://localhost:8080/swagger-ui/index.html
```
## Testes unitários
![testes_unitarios](https://github.com/user-attachments/assets/fe55e2e6-237c-4265-b672-148780809916)

## Build  do projeto
- Clonar o repositório: `git clone https://github.com/janioofi/monitoramento_back.git`
- Entrar na pasta do projeto: `cd monitoramento_back`
  - Será necessário atualizar o arquivo application-dev.yml, atualizar as propriedades mail.username e mail.password.
- Limpar o diretório target e reconstruir o projeto: `mvn clean install`
- Rodar o projeto: `mvn spring-boot:run`

## Funcionalidades
- CRUD para Dispositivos.
- CRUD para Alertas.
- Autenticação JWT.
- Envio de e-mail quando alerta acontece.

## Request e Response
- **Register Request**: O JSON enviado para criar um usuário é:
```json
{
  "username": "string",
  "password": "string"
}
```
- **Register Response**: Recebe como retorno uma string de confirmação ou um erro caso não seja concluído.

- **Login Request**: O JSON enviado para criar um usuário é:
```json
{
  "username": "string",
  "password": "string"
}
```

- **Device Request**: O JSON enviado para criar um dispositivo é:
```json
{
  "name": "string",
  "status": "ATIVO",
  "location": "string"
}
```
- Valor do campo status:
  - status = ['ATIVO', 'INATIVO', 'EM_FALHA']
- **Device Response**: Caso concluído, o retorno será o dispositivo no corpo da resposta; caso contrário, será enviado um erro

- **Alert Request**: O JSON enviado para criar um alerta é:
```json
{
  "level": "ERRO",
  "message": "Device failure detected"
}
```
- Valor do campo level:
  - level = ['NORMAL', 'ERRO', 'CRITICO']
- **Alert Response**: Caso concluído, o retorno será o alerta no corpo da resposta; caso contrário, será enviado um erro

## License
[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)
