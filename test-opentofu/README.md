# Prerequisites

1. Hetzner account, see also https://www.hetzner.com
1. Hetzner API token in environment variable `HCLOUD_TOKEN`
1. Your own internet domain, for example via https://www.united-domains.de with Hetzner nameservers
    1. hydrogen.ns.hetzner.com
    1. oxygen.ns.hetzner.com
    1. helium.ns.hetzner.de
1. `ssh` locally installed, for creating an SSH key pair
1. `opentofu` locally installed

# Quickstart

1. `ssh-keygen -t ed25519 -f id_ed25519` generate key pair
1. `tofu init` install OpenTofu provider
1. `tofu apply` create resources in Hetzner Cloud, please enter your domain name here like `company.de`