
In questa esercitazione utilizzeremo Minikube per effettuare il Deploy di due container nel medesimo Pod.

Nel contesto di Kubernetes, un Pod equivale ad uno stesso **Host Logico** e possiamo implementare il **Sidecar Pattern** facendo il deploy di più container nel medesimo Pod.

Utilizziamo l'esempio presentato nel sito ufficiale di Kubernetes alla pagina:

[**Communicate Between Containers in the Same Pod Using a Shared Volume**](https://kubernetes.io/docs/tasks/access-application-cluster/communicate-containers-same-pod-shared-volume/)

## Avviamo minikube

Avviamo il cluster Kubernetes tramite:

`minikube start`{{execute}}

## Uno sguardo al manifest

Guardiamo il file per il deployment:

`less -N deployment.yaml`{{execute}}

L'intero Pod ha un **Volume** chiamato *shared-data*.
Questo volume è condiviso da entrambi i container:
- **nginx-container** che viene avviato dall'immagine base del server *nginx*
- **debian-container** che viene avviato dall'immagine base del sistema operativo *debian*

Il container Debian, all'avvio, esegue il seguente comando:

```
echo Hello from the debian container > /pod-data/index.html

```
che scrive il messaggio nel volume condiviso e viene terminato.
Proveremo ad effettuare l'accesso al container nginx per leggere il file *index.html*, la pagina di default di nginx, verificando che il container Debian abbia correttamente sovrascritto il file *index.html* e che quindi i due container condividono lo stesso volume.

## Deploy del Pod

Effettuiamo il deploy del Pod con i due container:

`kubectl apply -f deployment.yaml`{{execute}}

Verifichiamo lo stato dei due container:

`kubectl get pod two-containers --output=yaml | tail -34 | less -N`{{execute}}

Come si evince dal file, il container Debian è già terminato, mentre nginx rimane in fase di *running*:
```
containerStatuses:

  - containerID: docker://c1d8abd1 ...
    image: debian
    ...
    lastState:
      terminated:
        ...
    name: debian-container
    ...

  - containerID: docker://96c1ff2c5bb ...
    image: nginx
    ...
    name: nginx-container
    ...
    state:
      running:
```

## Connettiamoci al container nginx

Apriamo una shell sul container nginx:

`kubectl exec -it two-containers -c nginx-container -- /bin/bash`{{execute}}

Installiamo *curl* per poter effettuare le chiamate HTTP al web server:

`apt-get update && apt-get install curl procps -y`{{execute}}

Facciamo la chiamata a *localhost* con *curl* :

`curl localhost`{{execute}}

Se l'output visualizzato è `Hello from the debian container` allora i due container hanno comunicato correttamente.

Usciamo dalla shell di nginx:

`exit`{{execute}}

Distruggiamo il Pod:

`kubectl delete pod two-containers`{{execute}}


