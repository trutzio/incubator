## Installation

Eine ausführliche Installationsanleitung befindet sich [hier](https://fluxcd.io/flux/installation/#install-the-flux-cli). Unter Windows wird Flux mit `choco install flux` installiert. Für die Installation von Chocolatey siehe auch [https://chocolatey.org/install](https://chocolatey.org/install).

## Bootstrap

Siehe auch [https://fluxcd.io/flux/installation/#bootstrap-with-flux-cli](https://fluxcd.io/flux/installation/#bootstrap-with-flux-cli) und speziell [diesen Link](https://fluxcd.io/flux/installation/bootstrap/github/) für den Bootstrap unter GitHub.

Der Bootstrap Befehl erzeugt innerhalb eines Git Repositories den Pfad für die entsprechenden Cluster und gleichzeitig die Flux Controller. Ein Beispiel Bootstrap Befehl sieht so aus:

```shell
flux bootstrap git --url=ssh://git@github.com/trutzonline/infrastructure.git --branch=dev --path=clusters/schulung/dev --private-key-file=schulung
```
