

Ciao, questo terminale ti permettera' di accedere alle funzionalita' del tuo Cloud Provider inventato che si chiama Easycloud. 
Potrai esercitare le tue nuove abilita' creando un cluster di macchine kubernetes ed effettuando il deploy di servizi web.

## Lanciamo l'ambiente Easycloud
Entriamo nel nostro ambiente cloud simulato:

`launch.sh`{{execute}}

## Crea il tuo primo cluster kubernetes
Creiamo un cluster kubernetes con il tool di Easycloud `clusterinit`
Il cluster avra' un nodo master e due nodi worker e si chiamera' `demo`

`clusterinit`{{execute}}

Controlliamo il cluster appena creato usando `kubectl`.

`kubectl cluster-info`{{execute}}

Quali sono le macchine (i nodi) del nostro cluster provisionati?

`kubectl get nodes`{{execute}}

## Registriamo un dominio
Simuliamo di registrare un dominio tramite il comando da terminale `domain` e chiamiamolo "shop.example.com".

Lanciamo il comando:

`domain`{{execute}}

scriviamo il dominio:

`shop.example.com`{{execute}}

## Uno sguardo ai file di deployment

Vogliamo ora rilasciare un applicazione composta da due microservizi: 
- _shop-service_
- _product-service_

Facciamo in modo che _shop-service_ sia disponibile con 2 repliche, mentre _product-service_ con 3 repliche all'interno del cluster.
A fare loadbalancing utilizzeremo dei servizi kubernetes di tipo ClusterIP raggiungibili solo all'interno del cluster kubernetes.

Diamo uno sguardo alla descrizione del deployment:

`less deployment.yaml`{{execute}}

Per raggiungere i servizi all'esterno del cluster kubernetes usiamo un *ingress* a cui applichiamo delle regole di inoltro delle chiamate ai singoli servizi:

`less ingress.yaml`{{execute}}

## Applichiamo i file yaml

Effettuiamo il deploy dell'applicazione tramite *kubectl*: 

`kubectl apply -f deployment.yaml`{{execute}}

Applichiamo le regole all'*ingress controller* per raggiungere l'applicazione all'esterno del cluster:

`kubectl apply -f ingress.yaml`{{execute}}

Controlliamo le repliche dei nostri microservizi:

`kubectl get pods`{{execute}}

Controlliamo i loadbalancer interni per raggiungere i due microservizi:

`kubectl get services`{{execute}}

Vediamo l'ingress installato:

`kubectl get ingress`{{execute}}

## Chiamiamo i due servizi

Servizio _product-service_ 

`curl http://shop.example.com/product-service/products/CDF5463GG56 | jq`{{execute}}

Servizio _shop-service_ che fa una chiamata a _product-service_:

`curl http://shop.example.com/shop-service/v2/shops/WEDD321/products/CDF5463GG56 | jq`{{execute}}




