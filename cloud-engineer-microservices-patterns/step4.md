
In questa esercitazione andremo a fare il _deploy_ di un'architettura a microservizi utilizzando i Pattern:

- API Gateway
- Service Discovery
- Load Balancing
- Circuit Breaker

Utilizzeremo Docker-compose e le librerie viste negli step precedenti (*eureka*, *histrix*, *zuul*).

## Codice sorgente

Diamo uno sguardo al codice sorgente. Possiamo utilizzare l'editor o il terminale.
Se vogliamo utilizzare il terminale possiamo fare:

`ls ./final/code`{{execute}}

Abbiamo il codice sorgente dei 3 progetti:
- *eureka-server* il servizio che implementa la Service Discovery e possiede il Service Registry;
- *zuul-proxy* il servizio che funge da API Gateway
- *shop-service* il microservizio dei negozi;
- *product-service* il microservizio dei prodotti.

I microservizi *product-service* e *shop-service* una volta avviati notificano la propria presenza al server *eureka* e sono quindi registrati nel Service Registry.

Il microservizio *shop-service* utilizza la libreria *hystrix* per implementare il pattern Circuit Breaker nelle chiamate verso il *product-service*.

Il microservizio *zuul-proxy* funge da API Gateway ed implementa la logica di autorizzazione attraverso una fase di _login_. Anche l'API Gateway si registra al microservizio *eureka-server* e recupera le informazioni dei servizi da raggiungere (*shop-service*, *product-service*) interrogando il Service Registry mantenuto da *eureka-server*.

I vari microservizi, per registrarsi al server *eureka* ne devono conoscere l'indirizzo:

`less -N ./final/code/zuul-proxy/src/main/resources/application.yaml`{{execute}}

`less -N ./final/code/shop-service/src/main/resources/application.yaml`{{execute}}

`less -N ./final/code/product-service/src/main/resources/application.yaml`{{execute}}

Quando al microservizio *shop-service* chiediamo il dettaglio di un prodotto, questo effettua una chiamata HTTP al microservizio *product-service*.
In realtà lo *shop-service* non conosce a priori l'indirizzo del *product-service*, pertanto, prima di effettuare la chiamata, interroga il server *eureka* per ottenere l'indirizzo (o gli indirizzi) del *product-service*.

Nel codice sorgente dello *shop-service* abbiamo il _placeholder_ dell'indirizzo IP del *product-service* che viene opportunamente sostituito con il reale indirizzo IP dopo che la libreria client di *eureka* effettua l'interrogazione al Service Registry. Nel caso in cui la chiamata verso il microservizio dei prodotti non vada a buon fine entra in gioco il Circuit Breaker:

`less -N ./final/code/shop-service/src/main/java/com/example/microservices/service/ProductService.java`{{execute}}

## Docker

Avviamo i microservizi secondo il seguente schema:
- eureka-server (porta 8761)
- product-service 3 repliche (porte: 8085, 8086, 8087)
- shop-service 2 repliche (porte: 8081, 8082)
- zuul-proxy (porta 8080)


`docker run --rm -d --network host -e PORT=8761 --name eureka-server codemotiontraining/microservices-eureka-server`{{execute}} 

`docker run --rm -d --network host -e PORT=8085 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-1 codemotiontraining/microservices-product-service`{{execute}} 

`docker run --rm -d --network host -e PORT=8086 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-2 codemotiontraining/microservices-product-service`{{execute}}

`docker run --rm -d --network host -e PORT=8087 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-3 codemotiontraining/microservices-product-service`{{execute}}

`docker run --rm -d --network host -e PORT=8081 -e EUREKA_URL="http://localhost:8761/eureka" --name shop-service-1 codemotiontraining/microservices-shop-service`{{execute}} 

`docker run --rm -d --network host -e PORT=8082 -e EUREKA_URL="http://localhost:8761/eureka" --name shop-service-2 codemotiontraining/microservices-shop-service`{{execute}}

`docker run --rm -d --network host -e PORT=8080 -e EUREKA_URL="http://localhost:8761/eureka" --name zuul-proxy codemotiontraining/microservices-zuul-proxy`{{execute}} 

## Test dell'architettura ##

Chiamiamo le istanze del microservizio *product-service*:

`curl http://localhost:8085/products/CDF5463GG56 | jq`{{execute}}

`curl http://localhost:8086/products/CDF5463GG56 | jq`{{execute}}

`curl http://localhost:8087/products/CDF5463GG56 | jq`{{execute}}

Chiamiamo le istanze del microservizio *shop-service* in modo che interagisca con *product-service* passando per *eureka-server* ed applicando _load-balancing_ sulle istanze del microservizio dei prodotti:

`curl http://localhost:8081/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

`curl http://localhost:8082/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

Chiamiamo i servizi *product-service* e *shop-service* tramite *zuul-proxy* l'API Gateway senza passare alcun token di autorizzazione:

`curl -s -D - http://localhost:8080/api/product-service/products/CDF5463GG56`{{execute}}

`curl -s -D - http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56`{{execute}}

Otteniamo lo status *401 Unauthorized*.

Effettuando la chiamata di login con credenziali volutamente sbagliate:

`curl -s -D - -H 'Content-Type:application/json; charset=utf8' -X POST 'http://localhost:8080/api/shop-service/login' -d '{"username": "shopAdmin", "password": "wrongPassword"}'`{{execute}}

Effettuiamo la chiamata di login con le credenziali corrette:

`curl -s -D - -H 'Content-Type:application/json; charset=utf8' -X POST 'http://localhost:8080/api/shop-service/login' -d '{"username": "shopAdmin", "password": "secretPassword"}'`{{execute}}

Ottenuto il _token_ lo alleghiamo alla chiamata verso il *product-service* tramite l'API Gateway *zuul-proxy*:

`curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/product-service/products/CDF5463GG56 | jq`{{execute}}

E verso lo *shop-service* che chiama il *product-service*:

`curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}


Spegniamo il microservizio dei prodotti nelle sue 3 repliche per far entrare in azione *hystrix*:

`docker stop product-service-1 product-service-2 product-service-3`{{execute}}

Verifichiamo che il Circuit Breaker sia attivo:

`curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}

Riavviamo il microservizio dei prodotti:

`docker run --rm -d --network host -e PORT=8085 -e EUREKA_URL="http://localhost:8761/eureka" --name product-service-1 codemotiontraining/microservices-product-service`{{execute}} 

E ritestiamo la chiamata:

`curl -H "Authorization: bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd" http://localhost:8080/api/shop-service/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}




