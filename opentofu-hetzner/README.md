# Prerequisites

1. Hetzner account, see also https://www.hetzner.com
1. Hetzner API token in environment variable `HCLOUD_TOKEN`
1. Your own Internet domain, for example via https://www.united-domains.de with Hetzner nameservers
    1. `hydrogen.ns.hetzner.com`
    1. `oxygen.ns.hetzner.com`
    1. `helium.ns.hetzner.de`
1. `ssh` locally installed, for creating an SSH key pair
1. `opentofu` locally installed

# Quickstart

1. `ssh-keygen -t ed25519 -f id_ed25519` — generate key pair
1. Disable security inheritance on file `./id_ed25519`, become owner of this file and get full access. This is important because `ssh` doesn't allow key files with bad permissions.
1. `tofu init` — install OpenTofu provider
1. `tofu apply -auto-approve` — create resources in Hetzner Cloud. Please enter your domain name here, like `yourcompany.de`.
1. Open http://test.yourcompany.de:8080 in your browser.