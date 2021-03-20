
In questa esercitazione effettueremo il deploy di un applicativo serverless in un cluster kubernetes.

Utilizzeremo Minikube come distribuzione di kubernetes e Kubeless come framework per il deploy di applicazioni serverless in un cluster kubernetes.

L'esempio consta di una funzione serverless scritta in Golang, attivabile tramite una chiamata https, che persiste i dati ricevuti in un database serverless (FaunaDB).

Utilizzeremo FaunaDB nel cluster Minikube per non dover attivare account esterni e creeremo al volo database, collezioni e indice di ricerca tramite la shell di FaunaDB.

I punti cardine dell'esercitazione sono i seguenti:

- installazione di FaunaDB nel cluster k8s;
- installazione del framework Kubeless e della Kubeless CLI;
- deploy della funzione serverless con variabili d'ambiente e dipendenze esterne;
- creazione di un trigger https per la funzione serverless.