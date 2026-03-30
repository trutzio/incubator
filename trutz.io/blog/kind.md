## Installation

Eine ausführliche Installationsanleitung für kind (Kubernetes in Docker) befindet sich [hier](https://kind.sigs.k8s.io/docs/user/quick-start/#installation). Unter Windows wird kind am einfachsten mit

```sh
winget install Kubernetes.kind
```

installiert. Ein Kubernetes Cluster entsteht mit dem Befehl

```sh
kind create cluster
```

Nachdem der Cluster erstellt wurde, kann man mit [kubectl](kubectl) den Cluster bedienen. Ein neuer k8s Context mit dem Namen `kind-kind` wurde automatisch erzeugt und in die Datei `.kube/config` eingetragen und aktiviert. Man kann diesen Context mit dem Befehl `kubectl config get-contexts` sichten.

Mit `docker container ls` sieht man, dass der komplette k8s Cluster (Control Plane) innerhalb eines Docker Containers läuft. Mit `docker exec -it kind-control-plane bash` erhält man eine Shell innerhalb des Control Planes.
