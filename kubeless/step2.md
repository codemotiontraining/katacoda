
Le funzioni serverless si sposano bene con l'utilizzo di Database Serverless. In questo esempio deployeremo FaunaDB, il database serverless sviluppato dai creatori di Twitter e vi inseriremo dei dati di esempio.

## Deployment con Kubectl
Visualizziamo il nostro deployment:
`less faunadb-deployment.yaml`{{execute}}

Abbiamo un pod che avvia l'immagine docker di FaunaDB e persiste le informazioni su un _persistent volume_ in locale tramite il _persistent volume claim_ definito nel file.

Il pod e' raggiungibile tramite un servizio di tipo ClusterIP.

Procediamo al deploy degli elementi definiti nel file:

`kubectl apply -f faunadb-deployment.yaml`{{execute}}

Attendiamo qualche attimo e verifichiamo che gli elementi rilasciati siano attivi:

`kubectl get services`{{execute}}

`kubectl get pv`{{execute}}

`kubectl get pvc`{{execute}}

`kubectl get statefulset`{{execute}}

Il nostro deploy è composto da un solo pod:

`kubectl get pods`{{execute}}

Salviamo il nome del pod:

`export FAUNA_POD=$(kubectl get pods | grep faunadb | cut -d " " -f1)`{{execute}}

## Ispezioniamo il pod di FaunaDB

Per ispezionare il pod usiamo il comando:

`kubectl exec -it $FAUNA_POD /bin/bash`{{execute}}

Siamo entrati nel pod dove è installato FaunaDB ed anche la CLI che possiamo richiamare come segue:

`fauna`{{execute}}

## Generiamo un database di esempio

Possiamo generare un database in FaunaDB a partire da un applicativo che ne utilizza i driver, tramite interfaccia web, oppure tramite CLI.
A titolo di esempio generiamo un nuovo database (pets_db) ed una nuova chiave d'accesso tramite CLI a partire dal pod che stiamo ispezionando.

Non è il modo più canonico di procedere, ma è sicuramente quello più immediato che abbiamo per avere un ecosistema funzionante per il nostro applicativo serverless.

Creiamo il database:

`fauna create-database pets_db`{{execute}}

Per avere la lista dei database:

`fauna list-databases`{{execute}}

## Generiamo una collection

A titolo di esempio generiamo una nuova collection (pets) e un indice di ricerca ad esso collegato.

Entriamo con lo strumento _shell_ nel database appena creato:

`fauna shell pets_db`{{execute}}

creiamo una collection: 

`CreateCollection({ name: "pets" })`{{execute}}

creiamo un indice di ricerca:

`CreateIndex(
    {
       name: "pets_by_name",
       source: Collection("pets"),
       terms: [{ field: ["data", "name"] }]
    })`{{execute}}

usciamo dallo strumento shell:

`.exit`{{execute}}

Per il database appena creato generiamo una chiave di accesso che servirà a collegare la nostra applicazione serverless:

`fauna create-key pets_db`{{execute}}

*Copiamo la chiave appena generata*: ci servirà subito dopo.

Per avere la lista delle chiavi per la nostra istanza di FaunaDB:

`fauna list-keys`{{execute}}

usciamo dal pod che stiamo ispezionando:

`exit`{{execute}}

## Salviamo la chiave generata

Mettiamo la chiave di accesso al database in una variabile:

`export FAUNA_KEY=\`{{execute}}

incolliamo la chiave copiata e premiamo INVIO.


