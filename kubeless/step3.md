È arrivato il momento di parlare di funzioni serverless!
Ci appoggeremo all'infrastruttura di kubernetes per rilasciare delle funzioni serverless tramite il framework kubeless.

## Installiamo kubeless

Kubeless è un framework per kubernetes che utilizza le _custom resource definition_ per implementare la logica delle applicazioni serverless.

Visioniamo brevemente il file yaml con la definizione degli oggetti che saranno deployati nel nostro cluster:

`less kubeless-v1.0.7.yaml`{{execute}}  

Creiamo un namespace apposito e applichiamo gli oggetti appena visionati:

`kubectl create ns kubeless`{{execute}}

`kubectl create -f kubeless-v1.0.7.yaml`{{execute}}

Visioniamo quali _custom resource_ sono stati deployati nel nostro cluster:

`kubectl get customresourcedefinition`{{execute}}

Visioniamo il controller di kubeless:

`kubectl get deployment -n kubeless`{{execute}}

`kubectl get pods -n kubeless`{{execute}}

## Installiamo kubeless CLI 

La command line interface di kubeless ci fornisce una serie di facilitazioni per comunicare con il nostro cluster ed effettuare il deploy delle _custom resource_ ovvero _function_, _trigger_, _cronjob_.

Installiamo la CLI partendo dal file di installazione che è presente nella root directory:

`ls *.zip`{{execute}}

`unzip kubeless_linux-amd64.zip && \
   sudo mv bundles/kubeless_linux-amd64/kubeless /usr/local/bin/`{{execute}}

Una volta installata la CLI proviamo subito ad utilizzarla per visualizzare i _runtime_ supportati:

`kubeless get-server-config`{{execute}}
