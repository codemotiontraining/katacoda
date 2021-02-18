
In questa esercitazione andremo ad implementare il pattern *Circuit Breaker* utilizzando Docker.

## Codice sorgente

Diamo uno sguardo al codice sorgente. Possiamo utilizzare l'editor o il terminale.
Se vogliamo utilizzare il terminale possiamo fare:

`ls ./circuit-breaker/code`{{execute}}

Abbiamo il codice sorgente dei 3 progetti:
- *circuit-breaker-shop-service* il microservizio dei negozi;
- *circuit-breaker-product-service* il microservizio dei prodotti.

Lo *shop-service* possiede l'indirizzo del *product-service* ed utilizza *Hystrix* come libreria per implementare il Pattern Circuit Breaker:

`less -N ./circuit-breaker/code/circuit-breaker-shop-service/src/main/java/com/example/circuitbreaker/service/ProductService.java`{{execute}}

L'intento è quello di avviare il *product-service* ed lo *shop-service* e spegnere il microservizio dei prodotti per renderlo irraggiungibile a quello dei prodotti e, quindi, far intervenire il Circuit Breaker.


## Docker

Avviamo i servizi tramite Docker:

`docker run --rm -d --network host -e PORT=8081 --name product-service codemotiontraining/circuit-breaker-product-service`{{execute}}

`docker run --rm -d --network host -e PORT=8080 -e PRODUCT_SERVICE_URL="http://localhost:8081" --name shop-service codemotiontraining/circuit-breaker-shop-service`{{execute}}

## Test dell'architettura ##

Chiediamo al microservizio dei negozi il dettaglio di uno dei prodotti presenti nell'inventario di un negozio specifico affinché entrambi i microservizi siano coinvolti nella chiamata:

`curl http://localhost:8080/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

Terminiamo il servizio dei prodotti per permettere al Circuit Breaker di entrare in gioco:

`docker stop product-service`{{execute}}

Effettuiamo nuovamente la chiamata che coinvolge entrambi i microservizi:

`curl http://localhost:8080/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

Riavviamo il servizio dei prodotti e ritestiamo la chiamata:

`docker run --rm -d --network host -e PORT=8081 --name product-service codemotiontraining/circuit-breaker-product-service`{{execute}}

`curl http://localhost:8080/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

## Stop dei container

Terminiamo i container avviati:

`docker stop $(docker ps -a -q)`{{execute}}
