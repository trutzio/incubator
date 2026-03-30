## Installation

Eine ausführliche Installationsanleitung für `kubectl` findet man [hier](https://kubernetes.io/docs/tasks/tools/#kubectl).

## KUBECONFIG

Die Umgebungsvariable (environment variable) `KUBECONFIG` verweist auf eine Konfigurationsdatei in denen sich Kubernetes Contexts befinden. Ein Context entspricht einer Verbindung zu einem k8s Cluster.

Wird die Umgebungsvariable `KUBECONFIG` nicht gesetzt, so enthält die Datei `.kube/config` die k8s Context-Konfiguration, d.h. eine Datei kann mehrere k8s Contexts enthalten.
