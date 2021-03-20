
Abbiamo installato FaunaDB, Kubeless, Kafka e Zookeper, adesso possiamo passare al deploy e al test delle nostre funzioni serverless.


## Visioniamo il codice sorgente

Visioniamo il codice sorgente della nostra funzione serverless _Save_:

`less petfunction.go`{{execute}}

Essa si interfaccerà con FaunaDB, la cui dipendenza è definita nel file:

`less go.mod`{{execute}}

## Facciamo il deploy con Kubeless

Utilizziamo Kubeless CLI precedentemente installata per fare il deploy delle due funzioni serverless scritte in Golang.

`kubeless function deploy save-pets --runtime go1.14 --handler petfunction.Save --from-file petfunction.go --dependencies go.mod --env FAUNA_KEY=$FAUNA_KEY --env FAUNA_URL=http://faunadb.default.svc.cluster.local:8443`{{execute}}

Visioniamo la funzione tramite Kubeless CLI:

`kubeless function ls`{{execute}}

## Dichiariamo il trigger

Per attivare la funzione _Save_ dobbiamo dichiarare quale sia il _trigger_ associato, ovvero la sorgente degli eventi che attiva la funzione stessa.

La funzione _save-pets_ sarà attivata da una chiamata https.

Prima di procedere con la creazione di un _trigger_ sinceriamoci che il nostro cluster kubernetes abbia un _Ingress_ attivo che renda raggiungibile il cluster dall'esterno tramite chiamate http/https.

Con la distribuzione kubernetes Minikube, questo compito è molto semplice, perchè ci viene in aiuto il comando _addon_:

`minikube addons enable ingress`{{execute}}

Vediamo quindi la lista aggiornata degli _addons_ di Minikube con:

`minikube addons list`{{execute}}

Possiamo verificare quando l'ingress è correttamente avviato lanciando:

`kubectl get pod -n kube-system | grep nginx`{{execute}}

La funzione save-pets sarà attivata da una chiamata verso https://pets.example.com/pets.
Per simulare la registrazione del dominio _pets.example.com_ possiamo registrare il dominio localmente in /etc/hosts:

`echo "$(minikube ip) pets.example.com" | sudo tee -a /etc/hosts`{{execute}}

Andiamo adesso a creare un _self signed certificate_ per poter fare le chiamate in https:

`openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -subj "/CN=pets.example.com"`{{execute}}

Questi sono i file generati:

`ls -t | head -2`{{execute}}

Salviamo il certificato prodotto in un _secret_ di kubernetes:

`kubectl create secret tls tls-secret --key tls.key --cert tls.crt`{{execute}}

Andiamo a creare il trigger https per la funzione:

`kubeless trigger http create save-pets-trigger --function-name save-pets --path pets --hostname pets.example.com --cors-enable --tls-secret tls-secret`{{execute}}


## Testiamo il nostro sistema

Adesso che abbiamo fatto il deploy della funzione serverless e del trigger, proviamo a salvare sul database e recuperare l'informazione salvata.
Per attivare la funzione save-pets chiamiamo:

`curl -s -D - -k  -X POST 'https://pets.example.com/pets' -d '{"name": "Fuffy", "age": 3}'`{{execute}}

