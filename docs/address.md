# Address API Spec

## Create Address
EndPoint: POST /api/contacts/{idContact}/addresses

Request Header:

- X-API-TOKEN: Token (mandatory)

Request body:
```json
{
  "street": "Jalan ....",
  "city": "kota...",
  "province": "provinsi...",
  "country": "Negara ...",
  "portalCode": "143243"
}
```

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "street": "Jalan ....",
    "city": "kota...",
    "province": "provinsi...",
    "country": "Negara ...",
    "portalCode": "143243"
  }
}
```

Response body (Failed):

```json
{
  "errors": "Contact is not found"
}
```

## Update Address
EndPoint: PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header:

- X-API-TOKEN: Token (mandatory)

Request body:
```json
{
  "street": "Jalan ....",
  "city": "kota...",
  "province": "provinsi...",
  "country": "Negara ...",
  "portalCode": "143243"
}
```

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "street": "Jalan ....",
    "city": "kota...",
    "province": "provinsi...",
    "country": "Negara ...",
    "portalCode": "143243"
  }
}
```
Response body (Failed):

```json
{
  "errors": "Address is not found"
}
```

## Get Address
EndPoint: GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):

```json
{
  "data" : {
    "id": "random String",
    "street": "Jalan ....",
    "city": "kota...",
    "province": "provinsi...",
    "country": "Negara ...",
    "portalCode": "143243"
  }
}
```
Response body (Failed):

```json
{
  "errors": "Address is not found"
}
```

## Remove Address
EndPoint: DELETE /api/contacts/{idContact}/address/{idAddress}

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):

```json
{
  "data" : "OK"
}
```
Response body (Failed):

```json
{
  "errors": "Address is not found"
}
```
## List Address
EndPoint: GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header:

- X-API-TOKEN: Token (mandatory)

Response body (Success):
```json
{
  "data": [
    {
      "id": "random String",
      "street": "Jalan ....",
      "city": "kota...",
      "province": "provinsi...",
      "country": "Negara ...",
      "portalCode": "143243"
    }
  ]
}
```

Response body (Failed):
```json
{
  "errors": "Contact is not found"
}
```