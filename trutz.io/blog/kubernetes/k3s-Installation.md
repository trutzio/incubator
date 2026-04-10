## Minimale Hardware-Anforderungen

1. siehe https://docs.k3s.io/installation/requirements?os=debian#server-sizing-guide
1. 2 CPUs und 4 GB RAM
1. `ssh -i schulung root@[ip]` in einem Debian 13 System
1. `apt update && apt upgrade` optional (zeitintensiv)
1. Netzwerk Konfiguration https://docs.k3s.io/installation/requirements?os=debian#networking
    - The K3s server needs port 6443 to be accessible by all nodes.
    - The nodes need to be able to reach other nodes over UDP port 8472 when using the Flannel VXLAN backend, or over UDP port 51820 (and 51821 if IPv6 is used) when using the Flannel WireGuard backend. 
    - The node should not listen on any other port.
1. Firewall Konfiguration https://docs.k3s.io/installation/requirements?os=debian#ufw

## Installation

```sh
curl -sfL https://get.k3s.io | sh -
```

1. `k3s kubectl get nodes`
1. `cat /var/lib/rancher/k3s/server/node-token` Server Token
1. `curl -sfL https://get.k3s.io | K3S_URL=https://myserver:6443 K3S_TOKEN=mynodetoken sh -` auf einem neuen Rechner