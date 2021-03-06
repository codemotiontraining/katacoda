
In questa esercitazione andremo ad implementare il pattern *API Gateway* utilizzando Docker-compose.

## Codice sorgente

Diamo uno sguardo al codice sorgente. Possiamo utilizzare l'editor o il terminale.
Se vogliamo utilizzare il terminale possiamo fare:

`ls ./api-gateway/code`{{execute}}

Abbiamo il codice sorgente dei 3 progetti:
- *api-gateway-zuul-proxy* il servizio che funge da API Gateway;
- *api-gateway-product-service* il microservizio dei prodotti.

L'intento è quello di raggiungere il microservizio dei prodotti tramite un API Gateway che funga da proxy per le chiamate ed integri un sistema centralizzato di autenticazione, valido quando impediamo un accesso diretto al microservizio dei prodotti e ne vincoliamo l'interrogazione tramite l'utilizzo del Gateway.

Il Gateway è implementato tramite *zuul* e si aspetta l'indirizzo del microservizio dei prodotti tramite variabile d'ambiente:

`less -N ./api-gateway/code/api-gateway-zuul-proxy/src/main/resources/application.properties`{{execute}}

Inoltre, abbiamo settato un filtro che permette di fare _proxying_ della chiamata verso il microservizio dei prodotti solo se opportunamente autenticati:

`less -N ./api-gateway/code/api-gateway-zuul-proxy/src/main/java/com/example/apigateway/filters/PreFilter.java`{{execute}}

## Docker-compose

Diamo uno sguardo al file _docker-compose.yaml_:

`less -N ./api-gateway/docker-compose.yaml`{{execute}}

Questo _manifest_ permette a Docker di avviare il microservizio dei prodotti e il server *zuul* che funge da API Gateway.

Avviamo il Docker-compose in modalità _detached_ con:

`cd ./api-gateway && docker-compose up -d`{{execute}}

Visualizziamo i log:

`docker-compose logs -t -f`{{execute}}

Al termine del processo stacchiamoci dalla visualizzazione dei log con:

`Ctrl + C`

## Test dell'architettura ##

Facciamo una chiamata al microservizio *product-service* per ottenere i dettagli di un prodotto:

`curl http://localhost:8081/products/CDF5463GG56 | jq`{{execute}}

Supponendo di impedire l'accesso diretto al microservizio dei prodotti, chiamiamo il *product-service* passando per l'API Gateway:

`curl -s -D - http://localhost:8080/product-service/products/CDF5463GG56`{{execute}}

Riceviamo uno status *401 Unauthorized*.

Procediamo con il login per ottenere il _token_ di autorizzazione inserendo una password volutamente *errata*:

`curl -s -D - -H 'Content-Type: application/json; charset=utf8' -X POST 'http://localhost:8080/product-service/login' -d '{"username": "shopAdmin", "password": "wrongPassword"}'`{{execute}}

Rifacciamo il _login_ inserendo nome utente e password corretti e recuperando il _token_:

`curl -s -D - -H 'Content-Type:application/json; charset=utf8' -X POST 'http://localhost:8080/product-service/login' -d '{"username": "shopAdmin", "password": "secretPassword"}'`{{execute}}

Effettuiamo la chiamata al microservizio dei prodotti tramite l'API Gateway inserendo negli _Headers_ della chiamata il _token_ ottenuto dalla chiamata precedente:

`curl -H "Authorization: Bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/product-service/products/CDF5463GG56 | jq`{{execute}}


## Stop dei container

Terminiamo i container avviati e torniamo sulla directory _home_:

`docker-compose down && cd ..`{{execute}}