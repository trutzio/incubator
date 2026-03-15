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

Versucht man `docker version` als normaler Linux-User ohne `sudo` zu verwenden, wird zwar die Client Version von Docker ausgegeben, aber nicht die Server Version. Der Grund ist der fehlende Zugriff des Linux-Benutzers auf das Unix-Socket `/var/run/docker.sock`. Wenn man sich die Berechtigungen dieses Unix-Socket anschaut

```sh
srw-rw---- 1 root docker 0 Mar  8 09:16 /var/run/docker.sock
```

dann sieht man, dass der `root`-User und die Linux Gruppe `docker` zwar Lese- und Schreibberechtigungen haben aber die restlichen Linux-Benutzer diese Berechtigungen nicht haben.

Docker hat eine Client-Server-Architektur, das Kommandozeilentool `docker` repräsentiert die Clientseite, die über das Unix-Socket `/var/run/docker.sock` auf die Serverseite zugreift. Die Lösung des obigen Problems ist denkbar einfach: der Linux-Benutzer, der `docker` aufruft muss in die Linux-Gruppe `docker` aufgenommen werden. Zum Beispiel mit dem Befehl:

```sh
sudo usermod -aG docker $USER
```

Nachdem ein Linux-Benutzer in der Linux-Gruppe `docker` aufgenommen wurde, erhält dieser automatisch diese Berechtigungen und kann damit mit der Serverseite von `docker` kommunizieren.

Die Serverseite von Docker ist ein Linux-Daemon mit dem Namen `dockerd` (mit einem d wie Daemon am Ende). Bei der obigen Docker-Installation unter Linux werden sowohl die Clientseite `docker` als auch Serverseite `dockerd` auf dem gleichen Rechner installiert. Die Kommunikation zwischen `docker` und `dockerd` verläuft aus Effizienzgründen über das Unix-Socket `/var/run/docker.sock` und nicht über den TCP/IP Stack.

Man kann Docker so konfigurieren, dass die Kommunikation über TCPS oder über SSH läuft, wenn man mit deinem `docker` Client entfernte Docker Server ansprechen möchte.

## Windows

Unter Windows lässt sich Docker am einfachsten über das Installationspaket [Docker Desktop](https://www.docker.com/products/docker-desktop/) installieren. Docker benötigt unter Windows [WSL](https://learn.microsoft.com/en-us/windows/wsl/install) (Windows Subsystem Linux), das mit dem Befehl

```sh
wsl --install
```

innerhalb der Powershell Console unter Windows installiert wird. Danach kann Docker Desktop heruntergeladen und installiert werden. WSL wird benötigt, da Docker unter Windows eine klassische Virtualisierungsumgebung benötigt in der die Serverseite von Docker läuft.

Unter einer einer Docker Desktop Installation läuft die Clientseite von Docker, also das Kommandozeilentool `docker` nativ unter Windows und die Serverseite, also der Docker Daemon `dockerd`, innerhalb einer klassischen virtuellen Maschine unter WSL. Dies ist deshalb notwendig, da die Serverseite von Docker auf Fähigkeiten des Linux-Kernels basiert und diese benötigt.

Docker Desktop hat als Windows-Programm auch eine UI, die aber von der Funktionalität her gleichwertig ist mit dem Kommandozeilentool `docker`.

Wenn Docker Desktop gestartet wird, dann wird im Hintergrund automatisch auch eine klassische virtuelle Maschine unter WSL gestartet, die durch dem Befehl

```sh
wsl --list --running
```

in einer Windows Powershell aufgeführt wird. Die WSL Maschine heißt `docker-desktop` und führt den Docker Daemon und das komplette serverseitige Docker System aus mit einem Linux Kernel aus. Mit dem Befehl `wsl -d docker-desktop` kann eine Shell innerhalb dieser WSL Maschine gestartet werden und der `ps aux` listet folgende Prozesse innerhalb dieser Maschine auf:

Die Kommunikation zwischen dem nativen Windows-Programm `docker` und dem Docker Daemon innerhalb der WSL Maschine verläuft über das `npipe`-Protokoll.

## `docker context`

Mit dem Befehl `docker context` können clientseitig Verbindungen zu unterschiedlichen Docker Daemons aufgebaut werden, die lokal oder auf entfernten Server laufen. Ein Context ist in diesem Zusammenhang eine Verbindung zwischen dem `docker` Client und dem `dockerd` Server.

Mit dem `docker context ls` Befehl können alle bestehenden Verbindungen in der Konsole aufgelistet werden. Innerhalb einer frischen Docker Installation liefert diese Befehl unter Linux

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

Das "\*"-Zeichen am Ende des Namens eines Docker Context bedeutet, dass diese Verbindung in Verwendung ist. Dies bedeutet, dass die alle Befehle, die vom Docker Client sich auf den Docker Daemon beziehen, dessen Context aktuell in Verwendung ist.

Im obigen Beispiel bedeutet dies, dass unter Linux der einzige Context mit dem Namen `default` in Verwendung ist und die Kommunikation zum Docker Daemon über das Unix-Socket `/var/run/docker.sock` verläuft.

Im Windows Beispiel gibt es zwei Docker Contexts, einer mit dem Namen `default` und einer mit dem Namen `desktop-linux` (der Name ist hier identisch mit dem Namen der WSL Maschine). Der Context, der in Verwendung ist, ist `desktop-linux`, siehe das "\*" Zeichen. Die Kommunikation läuft zwischen dem Windows-Programm `docker` und dem Docker Daemon innerhalb der WSL Maschine über das `npipe`-Protokoll mit der URI `npipe:////./pipe/dockerDesktopLinuxEngine`.

Gibt man in der Console lediglich den Befehl `docker context`, so erhält man eine Auflistung mit Unterbefehlen, die man auch erwarten würde:

Man kann also mit dem Unterbefehl `use` den Docker Context wechseln oder mit `create` einen neuen Context erzeugen.
