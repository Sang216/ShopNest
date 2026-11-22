# ShopNest Catalog smoke tests

Run against a disposable Product Catalog instance with a fresh database.
Set `CATALOG_URL` to the catalog base URL (default `http://localhost:8080`).
Wait for `GET /products` to return `200` before running the checks.

Send all requests to `CATALOG_URL` without an `Authorization` header.
For request 3, send `Content-Type: application/json`.

| # | Request | JSON body | Expected status |
| --- | --- | --- | --- |
| 1 | `GET /products` | — | `200` |
| 2 | `GET /products/00000000-0000-0000-0000-000000000001` | — | `404` |
| 3 | `POST /products` | `{"name":"Handmade Candle","price":199.00,"category":{"name":"home-decoration"}}` | `401` |

Print `PASS` or `FAIL` for each request with its expected and actual status.
Exit nonzero if any request fails to connect or returns an unexpected status.