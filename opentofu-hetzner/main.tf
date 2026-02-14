terraform {
  required_providers {
    hcloud = {
      source  = "hetznercloud/hcloud"
      version = "1.60.0"
    }
    docker = {
      source  = "kreuzwerker/docker"
      version = "3.9.0"
    }
  }
}

variable "domainname" {
  type    = string
}

resource "hcloud_primary_ip" "test" {
  name          = "test"
  type          = "ipv4"
  assignee_type = "server"
  location      = "nbg1"
  auto_delete   = false
}

resource "hcloud_ssh_key" "test" {
  name       = "test"
  public_key = file("id_ed25519.pub")
}

resource "hcloud_server" "test" {
  name        = "test"
  image       = "debian-13"
  server_type = "cx23"
  location    = "nbg1"
  public_net {
    ipv4_enabled = true
    ipv4         = hcloud_primary_ip.test.id
    ipv6_enabled = false
  }
  ssh_keys = [hcloud_ssh_key.test.id]
  provisioner "remote-exec" {
    inline = [
      "apt update -y",
      "apt upgrade -y",
      "apt install -y docker.io docker-cli",
    ]
    connection {
      type        = "ssh"
      host        = hcloud_primary_ip.test.ip_address
      user        = "root"
      private_key = file("id_ed25519")
    }
  }
}

resource "hcloud_zone" "test" {
  name = var.domainname
  mode = "primary"
}

resource "hcloud_zone_rrset" "test" {
  zone = hcloud_zone.test.name
  name = "test"
  type = "A"
  records = [
    { value = hcloud_primary_ip.test.ip_address, comment = "test server" },
  ]
}

provider "docker" {
  host     = "ssh://root@${hcloud_primary_ip.test.ip_address}"
  ssh_opts = ["-o", "StrictHostKeyChecking=no", "-o", "UserKnownHostsFile=/dev/null", "-i", "./id_ed25519"]
}

resource "docker_image" "nginx" {
  name       = "nginx:latest"
  depends_on = [hcloud_server.test]
}

resource "docker_container" "nginx" {
  name  = "webserver"
  image = docker_image.nginx.image_id
  ports {
    internal = 80
    external = 8080
  }
}
