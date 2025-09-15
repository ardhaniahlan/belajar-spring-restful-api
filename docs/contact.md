# Contact API Spec

## Create Contact

Endpoint: POST /api/contacts

Request Header:

- X-API-TOKEN: Token (mandatory)

Request body:

```json
{
  "firstname": "Ardhani",
  "lastname": "Ahlan",
  "email": "ardhan@example.com",
  "phone": "0812213131231"
}
```

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "firstname": "Ardhani",
    "lastname": "Ahlan",
    "email": "ardhan@example.com",
    "phone": "0812213131231"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Email format invalid, phone format invalid, ..."
}
```

## Update Contact 

Endpoint: PUT /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (mandatory)

Request body:

```json
{
  "firstname": "Ardhani",
  "lastname": "Ahlan",
  "email": "ardhan@example.com",
  "phone": "0812213131231"
}
```

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "firstname": "Ardhani",
    "lastname": "Ahlan",
    "email": "ardhan@example.com",
    "phone": "0812213131231"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Email format invalid, phone format invalid, ..."
}
```

## Get Contact

Endpoint: GET /api/contacts/{idContact}

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "firstname": "Ardhani",
    "lastname": "Ahlan",
    "email": "ardhan@example.com",
    "phone": "0812213131231"
  }
}
```

Response body (Failed, 404):

```json
{
  "errors": "Contact is not found"
}
```

## Search Contact

Endpoint: GET /api/contacts

Request/Query Param:

- name : String, contact firstname or lastname, using like query, optional
- phone : String, contact phone, using like query, optional
- email : String, contact email, using like query, optional
- page : Integer, start from 0, default 0 
- size : Integer, default 10 

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):

```json
{
  "data" : [
    {
      "id": "random String",
      "firstname": "Ardhani",
      "lastname": "Ahlan",
      "email": "ardhan@example.com",
      "phone": "0812213131231"
    }
  ],
  "paging": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
  } 
}
```

Response body (Failed):

```json
{
  "errors" : "Unauthorize"
}
```

## Remove Contact

Endpoint: DELETE /api/contact/{idContact}

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):

```json
{
  "data": "OK"
}
```

Response body (Failed):
```json
{
  "errors": "Contact is not found"
}
```