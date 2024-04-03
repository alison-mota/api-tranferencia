
# API de Transferências
Esta API permite realizar transferências de valores entre contas bancárias de forma rápida e segura.

## Funcionalidades:

- Realização de transferências entre contas bancárias.  
- Envio de email de confirmação da transferência.

## Recursos:

- Endpoint: /usuario
- Método: POST
- Requisição:
  - Body: JSON com os dados da transferência (valor da transação e id do usuario de destino).
  - Headers:
    - Authorization: Bearer - token de autenticação.
- Resposta:
  - Código 200: OK.
  - Códigos de erro:
    - 400: Bad Request - Erro na validação dos dados da transferência.
    - 401: Unauthorized - Token de autenticação inválido.
    - 403: Forbidden - Serviço não autorizado ou perfil de usuário não pode realizar transferências.
    - 404: Not Found - Os usuários da requisição não foram encontrados no banco de dados.
    - 503: Service Unavaiable - Erro interno no servidor.

Exemplo de requisição:

JSON

``
{
"valor": 100.00,
"beneficiario": 2
}
``

## Segurança:

A API utiliza autenticação via token para garantir a segurança das transações.

### Observações:

Esta API é um exemplo didático e não deve ser utilizada em produção sem as devidas adaptações e configurações de segurança.
Consulte a documentação completa da API para obter mais informações sobre os endpoints, requisições e respostas.

### Links Úteis:

Documentação completa da API: [em breve]_  
Repositório do código-fonte: https://github.com/alison-mota/api-tranferencia  
Contato: https://github.com/alison-mota

Em caso de dúvidas ou sugestões, entre em contato através do email alisonalvesmota@gmail.com