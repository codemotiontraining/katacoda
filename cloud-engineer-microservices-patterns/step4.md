
In questa esercitazione andremo a fare il _deploy_ di un'architettura a microservizi utilizzando i Pattern:

- API-Gateway
- Service-Discovery
- Load Balancing
- Circuit Breaker

Utilizzeremo Docker-compose e le librerie viste negli step precedenti (*eureka*, *histrix*, *zuul*).

## Codice sorgente





Diamo uno sguardo al codice sorgente. Possiamo utilizzare l'editor o il terminale.
Se vogliamo utilizzare il terminale possiamo fare:

`ls ./service-discovery/code`{{execute}}

Abbiamo il codice sorgente dei 3 progetti:
- *spring-eureka-server* il servizio che implementa la Service Discovery e possiede il Service Registry;
- *discovery-shop-service* il microservizio dei negozi;
- *discovery-product-service* il microservizio dei prodotti.

I microservizi *product-service* e *shop-service* una volta avviati notificano la propria presenza al server *eureka* e sono quindi registrati nel Service Registry.

Affinché possano registrarsi al server *eureka* ne devono conoscere l'indirizzo:

`less ./service-discovery/code/discovery-shop-service/src/main/resources/application.yaml`{{execute}}

`less ./service-discovery/code/discovery-product-service/src/main/resources/application.yaml`{{execute}}

Quando al microservizio *shop-service* chiediamo il dettaglio di un prodotto, questo effettua una chiamata HTTP al microservizio *product-service*.
In realtà lo *shop-service* non conosce a priori l'indirizzo del *product-service*, pertanto, prima di effettuare la chiamata, interroga il server *eureka* per ottenere l'indirizzo (o gli indirizzi) del *product-service*.

Nel codice sorgente dello *shop-service* abbiamo il _placeholder_ dell'indirizzo IP del *product-service* che viene opportunamente sostituito con il reale indirizzo IP dopo che la libreria client di *eureka* effettua l'interrogazione al Service Registry (linea 78):

`less ./service-discovery/code/discovery-shop-service/src/main/java/com/example/discovery/controller/ShopController.java`{{execute}}

## Docker-compose

Diamo uno sguardo al file _docker-compose.yaml_:

`less ./service-discovery/docker-compose.yaml`{{execute}}

Questo _manifest_ permette a Docker di avviare il server *eureka* e 3 repliche del *product-service* oltre che una replica dello *shop-service*.

Avviamo il Docker-compose con:

`docker-compose up`{{execute}}

## Test dell'architettura ##

Facciamo una chiamata al microservizio *shop-service* per ottenere i dettagli di un negozio:

`curl http://localhost:8080/shops/WEDD321 | jq`{{execute}}

Facciamo una chiamata al microservizio *shop-service* per ottenere l'inventario di un negozio:

`curl http://localhost:8080/shops/WEDD321/products | jq`{{execute}}

Facciamo la chiamata che coinvolge entrambi i microservizi, collegati indirettamente tramite il servizio di Service Discovery offerto dal server *eureka* e chiediamo i dettagli di uno specifico prodotto presente nell'inventario di uno dei negozi:

`curl http://localhost:8080/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

