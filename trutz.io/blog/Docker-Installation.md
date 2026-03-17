# Docker Installation

# Linux

Unter Debian-artige Linux Distributionen reicht ein einfaches

```sh
sudo apt install docker.io
```

auf der Konsole um Docker zu installieren.

Nach der Installation kann mit

```sh
sudo docker version
```

die Version von Docker ausgeben werden um zu überprüfen, dass Docker korrekt installiert wurde. Dies ist der Fall, wenn sowohl die Client- als auch die Server Version von Docker ausgegeben werden.

Versucht man `docker version` als normaler Linux-User ohne `sudo` zu verwenden, wird zwar die Client Version von Docker ausgegeben, aber nicht die Server Version. Der Grund ist der fehlende Zugriff des Linux-Benutzers auf das Unix-Socket `/var/run/docker.sock`. Dieses Socket verwendet Docker für die Kommunikation zwischen Client und Server, wenn Client- und Server-Seite beide auf der selben Maschine laufen. Wenn man sich die Berechtigungen dieses Unix-Socket anschaut

```sh
srw-rw---- 1 root docker 0 Mar  8 09:16 /var/run/docker.sock
```

dann sieht man, dass der `root`-User und die Linux Gruppe `docker` Lese- und Schreibberechtigungen haben aber die restlichen Linux-Benutzer diese Berechtigungen nicht haben.

Docker hat eine Client-Server-Architektur, das Kommandozeilentool `docker` repräsentiert die Clientseite, die über das Unix-Socket `/var/run/docker.sock` auf die Serverseite zugreift. Die Lösung des obigen Problems (Version der Serverseite wird nicht angezeigt) ist denkbar einfach: der Linux-Benutzer muss in die Linux-Gruppe `docker` aufgenommen werden, zum Beispiel mit dem Befehl:

```sh
sudo usermod -aG docker $USER
```

Nachdem ein Linux-Benutzer in der Linux-Gruppe `docker` aufgenommen wurde, erhält dieser automatisch die Berechtigungen auf das obige Socket zuzugreifen und kann damit mit der Serverseite von `docker` kommunizieren.

Die Serverseite von Docker ist ein Linux-Daemon mit dem Namen `dockerd` (mit einem d wie Daemon am Ende). Bei der obigen Docker-Installation unter Linux werden sowohl die Clientseite `docker` als auch Serverseite `dockerd` auf dem gleichen Rechner installiert. Die Kommunikation zwischen `docker` und `dockerd` verläuft wie oben beschrieben über das Unix-Socket `/var/run/docker.sock`.

Man kann Docker so konfigurieren, dass die Kommunikation über TCPS oder über SSH läuft, wenn man einen entfernte Docker Server ansprechen möchte.

## Windows

Unter Windows lässt sich Docker am einfachsten über das Installationspaket [Docker Desktop](https://www.docker.com/products/docker-desktop/) installieren. Docker benötigt unter Windows [WSL](https://learn.microsoft.com/en-us/windows/wsl/install) (Windows Subsystem Linux), das mit dem Befehl

```sh
wsl --install
```

innerhalb der Powershell installiert wird. Danach kann Docker Desktop heruntergeladen und installiert werden. WSL wird benötigt, da Docker unter Windows eine klassische Virtualisierungsumgebung benötigt auf der die Serverseite von Docker läuft.

Unter einer einer Docker Desktop Installation läuft die Clientseite von Docker, also das Kommandozeilentool `docker` nativ unter Windows und die Serverseite, also der Docker Daemon `dockerd`, innerhalb einer klassischen virtuellen Maschine unter WSL. Dies ist deshalb notwendig, da die Serverseite von Docker auf Fähigkeiten des Linux-Kernels basiert und diese benötigt.

Docker Desktop hat als Windows-Programm auch eine UI, die von der Funktionalität her gleichwertig ist mit dem Kommandozeilentool `docker`.

Wenn Docker Desktop unter Windows gestartet wird, dann wird im Hintergrund automatisch auch eine klassische virtuelle Maschine unter WSL gestartet, die im dem Befehl

```sh
wsl --list --running
```

in einer Windows Powershell aufgelistet wird. Die WSL Maschine heißt `docker-desktop` und führt den Docker Daemon und das komplette serverseitige Docker System aus. Mit dem Befehl `wsl -d docker-desktop` kann eine Shell innerhalb dieser klassischen virtuellen Maschine starten.

Die Kommunikation zwischen dem nativen Windows-Programm `docker` und dem Docker Daemon innerhalb der WSL Maschine verläuft über das `npipe`-Protokoll.

## `docker context`

Mit dem Befehl `docker context` können Verbindungen zu unterschiedlichen Docker Daemons aufgebaut werden. Ein Docker Context ist eine Verbindungsdefinition zwischen dem `docker` Client und dem `dockerd` Server.

Mit dem `docker context ls` Befehl können alle bestehenden Verbindungsdefinitionen in der Konsole aufgelistet werden. Genau eine einzige dieser Verbindungen ist die der aktuell aktive Docker Context, also die Verbindung, die verwendet wird. Innerhalb einer frischen Docker Installation liefert `docker context ls` unter Linux

```sh
> docker context ls
NAME        DESCRIPTION                               DOCKER ENDPOINT
default *   Current DOCKER_HOST based configuration   unix:///var/run/docker.sock
```

und unter Windows

```sh
PS C:\Users\info> docker context ls
NAME              DOCKER ENDPOINT
default           npipe:////./pipe/docker_engine
desktop-linux *   npipe:////./pipe/dockerDesktopLinuxEngine
```

Ein Docker Context hat einen Namen, eine URI als Verbindung zu einem Docker Daemon und möglicherweise weitere Parameter, die notwendig sind um die Verbindung zu einem Docker Daemon zu konfigurieren. Dies können Zertifikate oder Benutzername und Passwort sein.

Das "\*"-Zeichen am Ende des Namens eines Docker Context bedeutet, dass diese Verbindung in Verwendung ist. Dies bedeutet, dass die alle Befehle, die vom Docker Client ausgeführt werden, gegen diesen "\*" Docker Context laufen.

Im obigen Beispiel bedeutet dies, dass unter Linux der Context mit dem Namen `default` in Verwendung ist und die Kommunikation zum Docker Daemon über das Unix-Socket `/var/run/docker.sock` verläuft.

Im Windows Beispiel gibt es zwei Docker Contexte, einer mit dem Namen `default` und einer mit dem Namen `desktop-linux` (der Name ist hier identisch mit dem Namen der WSL Maschine). Der Context, der in Verwendung ist, ist `desktop-linux`, siehe das "\*" Zeichen am Ende des Namens. Die Kommunikation zwischen dem Windows-Programm `docker` und dem Docker Daemon innerhalb der WSL Maschine läuft über das `npipe`-Protokoll mit der URI `npipe:////./pipe/dockerDesktopLinuxEngine`.

Gibt man in der Console lediglich den Befehl `docker context` an, so erhält man eine Auflistung mit möglichen Unterbefehlen.

Man kann also beispielsweise dem Unterbefehl `docker context use` den Docker Context wechseln oder mit `docker context create` einen neuen Context erzeugen.
