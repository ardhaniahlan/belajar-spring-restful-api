# User API Spec

## Register User

Endpoint: POST /api/users

Request body:
```json
{
  "username": "ardhan",
  "password": "diam",
  "name": "Ardhani Ahlan"
}
```

Response body (Success):
```json
{
  "data": "OK"
}
```

Response body (Failed):
```json
{
  "errors": "Usermane must not black"
}
```

## Login User

Endpoint: POST /api/auth/login

Request body:
```json
{
  "username": "ardhan",
  "password": "diam"
}
```

Response body (Success):
```json
{
  "data": {
    "token": "TOKEN",
    "expiredAt": 123132 // milisecond 
  }
}
```

Response body (Failed, 401):
```json
{
  "errors": "Usermane or Password wrong"
}
```

## Get User

Endpoint: GET /api/users/current

Request Header:

- X-API-TOKEN: Token (mandatory) 

Response body (Success):
```json
{
  "data": {
    "username": "ardhan",
    "name": "Ardhani Ahlan" 
  }
}
```

Response body (Failed, 401):
```json
{
  "errors": "Unauthorize"
}
```

## Update User

Endpoint: PATCH /api/users/current

Request Header:

- X-API-TOKEN: Token (mandatory)

Request body:
```json
{
  "name": "Ahlan Ardhani",
  "password": "new password"
}
```

Response body (Success):
```json
{
  "data": {
    "username": "ardhan",
    "name": "Ardhani Ahlan" 
  }
}
```

Response body (Failed, 401):
```json
{
  "errors": "Unauthorize"
}
```

## Logout User

Endpoint: DELETE /api/auth/logout

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):
```json
{
  "data": "OK"
}
```