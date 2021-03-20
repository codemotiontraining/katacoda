

In questa esercitazione utilizzeremo un cluster Kubernetes avviato tramite la distribuzione Minikube.

Avviamo il nostro cluster Minikube con:

`minikube start`{{execute}}

Minikube permette una serie di facilitazioni per abilitare il tuo cluster ad eseguire una serie di operazioni. 
Diamo uno sguardo agli _addons_ che sono abilitati nel nostro cluster Minikube:

`minikube addons list`{{execute}}

Come possiamo vedere, sono già abilitati lo _storage provisioner_ che permette al cluster di provisionare degli spazi di memoria per persistere i dati quando si utilizzano degli oggetti in kubernetes adibiti alla persistenza e chiamati  _persistent volume claim_.

Utilizzeremo i _persistent volume claim_ nell'oggetto database che stiamo per dichiarare.