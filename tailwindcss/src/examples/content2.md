---
    tags: ['tag1']
---

# Docker Container

*Bla bla*

## Was ist ein Container?

Eine leichtgewichtige virtuelle Maschine, die die Fähigkeiten des Linux Kernels benutzt (Namespaces, cgroups).
Docker Container können sehr schnell hochgefahren werden, da im Hintergrund nicht ein komplettes Betriebssystem gestartet wird, sondern der Linux Kernel des Hosts verwendet wird. 3. Docker Container sind heutzutage state-of-the-art beim Deployment von Software, da sie sehr sparsam mit Ressourcen (CPU/RAM) umgehen.

## Container mit nginx Server

Erzeuge einen Docker Container[^docker_container] mit einem nginx[^nginx_documentation] Server, starte ihn und überprüfe, dass er läuft.

`docker container create nginx:latest` Container[^nginx_docker_image] erzeugen aber nicht starten, beachte: Docker Image wird automatisch heruntergeladen, falls noch nicht auf dem Host vorhanden
`docker container ls` Liste alle Container auf, gerade erzeugter Container nicht sichtbar, nur laufende Container werden angezeigt
`docker container ls -a` Liste alle Container auf, auch die, die nicht laufen
Merke die Container Id oder den Container Namen
`docker container start [container name | container id]` Starte einen Container, Container wird durch seine Id oder seinen Namen identifiziert 6. `docker container ls` Liste laufende Container erneut auf, Status ist Up, d.h. der Container ist hochgefahren und nginx läuft auf Post 80 7. `docker container logs [container name | container id]` gebe die Logs des laufendes Prozesses im Container aus 8. `docker container stop [container name | container id]` stoppe den Container 9. `docker container ls` Container taucht in der Liste der laufenden Container nicht mehr auf 10. `docker container ls -a` Container taucht aber in der Liste aller Container auf mit dem Status Exited 11. `docker container rm [container name | container id]` Container wird endgültig gelöscht und 12. `docker container ls -a` Container taucht nicht mehr in der Liste aller Container auf

[^docker_container]: [`docker container`](https://docs.docker.com/reference/cli/docker/container/) Dokumentation

[^nginx_docker_image]: [nginx Docker Image auf Docker Hub](https://hub.docker.com/_/nginx)

[^nginx_documentation]: [nginx Dokumentation](http://nginx.org/en/docs/)

### Übung: Kann ein Container entfernt werden, wenn er noch läuft?

1. `docker container create nginx:latest` einen Container erzeugen
2. `docker container ls -a` Liste alle Container auf
3. `docker container start [container id | container name]` Container starten
4. `docker container ls` Container ist Up
5. `docker container rm [container id | container name]` Versuche einen laufenden Container zu entfernen
6. Fehlermeldung enthält `container is running: stop the container before removing or force remove` also muss ein Container gestoppt sein (Status Existed) bevor er gelöscht werden kann
7. Mit der `-f` Option wie force, kann auch ein laufender Container entfernt werden, siehe Dokumentation von [docker container rm](https://docs.docker.com/reference/cli/docker/container/rm/)
8. `docker container rm -f [container id | container name]` Entfernt einen Container auch dann wenn er läuft
9. `docker container ls -a` Überprüfen, dass der Container nicht mehr existiert

### docker container run

`docker container run` als Zusammenfassung der `crete` und `start` Kommandos, aber auch mehr, denn für die Fehlersuche ist es wichtig auch sofort den Log aus dem Container zu sehen.

1. `docker container create` und `docker container start` können mit einem Befehl zusammengefasst werden `docker container run nginx:latest`
2. `run` verhält sich verschieden zu `start`, die Console enthält den Log von nginx und bei einem Ctrl-C wird der Container gestoppt (Status Exited)
3. `docker container ls -a` listet den Container als Exited
4. `docker container start [container id | container name]` kann den Container erneut starten
5. `docker container ls` zeigt, dass der Container läuft
6. `docker container stop [container id | container name]` stoppt den Container
7. `docker container run -d nginx:latest` startet einen Container im sog. detached mode
8. `docker container ls -a` listet zwei nginx Container auf, eins ist gestoppt und eins läuft
9. Übung: Starte den gestoppten Container und verwende `ls` um zu sehen, dass beide nginx Container laufen
10. Übung: Stoppe beide Container und entferne sie
11. Tipp: `docker container rm -f [1. container name] [2. container name]` entfernt beide Container gleichzeitig
12. `docker container ls -a` überprüft, dass alle Container ordnungsgemäß entfernt wurden

### Option `-P`

Offene Ports im Container werden auf zufällige Ports des Hosts mit der Option `-P` abgebildet.

1. Problem: Port 80 ist lediglich von nginx ist lediglich innerhalb des Containers gültig und muss auf einen Port des Hosts abgebildet werden
2. `docker container run -d -P nginx:latest` die Option `-P` bildet die Ports eines Containers auf zufällige, freie Ports des Hosts
3. `docker container ls` listet nun auch die Mappings auf
4. Im Browser mit `http://localhost:[port]` zugreifen und sehen, dass der nginx nun erreichbar ist, siehe Meldung "Welcome to nginx!" im Browser
5. Übung: zwei Container starten und unterschiedliche Port Mappings ausprobieren

### docker container exec

Programme und Kommandos innerhalb eines laufenden Containers ausführen.
`docker container exec` führt Programme innerhalb eines laufenden Containers aus, wobei diese auch im Container vorhanden bzw. installiert sein müssen.

1. Tipp: `/usr/share/nginx/html` im nginx Container ist der Ort, der die HTML Dateien enthält und `/etc/nginx` enthält die Konfiguration von nginx
2. `docker container exec [container id | container name] [command]` führt ein Linux Befehl innerhalb eines Containers
3. Tipp: Viele Container enthalten auch eine `bash`, die ausgeführt werden kann, so auch nginx
4. `docker container exec -i -t [container id | container name] bash` startet eine Bash innerhalb eines Containers, bei deiner Bash sind die Optionen `-i` wie interaktiv und `-t` wie TTY=Terminal notwendig
5. Übung: Versuche die Bash innerhalb des Containers ohne die Optionen `-t` und `-i` zu starten
6. Übung: Versuche den Linux Befehl `ls` innerhalb des Containers zu starten (ohne `-t` und `-i`)
7. Tipp: mit `exit` kann die Bash innerhalb des Containers beendet werden
8. Übung: Starte erneut eine Bash innerhalb des laufenden nginx Containers
9. `cd /usr/share/nginx/html` wechsle in das Verzeichnis, das den Content (z.B. HTML Dateien) von nginx enthält
10. `echo '<p>Hello world!</p>' > test.html` erzeugt eine neue HTML Datei innerhalb des Containers
11. `ls -la` listet alle Dateien des Containers in dem nginx Verzeichnis aus und die gerade neu erzeugte Datei ist dabei
12. `cat test.html` gibt den Inhalt der Datei `test.html` auf der Console aus
13. Rufe `http://localhost:[port]/test.html` im Browser auf und die gerade erzeugte HTML Datei wird im Browser über nginx angezeigt
14. `exit` beendet die Shell im Container
15. `docker container stop [container id | container name]` stoppt den Container, entfernt ihn aber nicht
16. Im Browser ist `http://localhost:[port]/test.html` nicht mehr erreichbar
17. Übung: Starte den nginx Container neu
18. `docker container ls` listet die laufenden Container auf, wobei der Container ein neues Post Mapping erhalten hat, d.h. der alte Port auf dem Host wird nicht mehr funktionieren
19. Rufe `http://localhost:[port]/test.html` im Browser auf um zu sehen, dass die von uns erzeugte Datei den Container Restart überlebt hat
20. Übung: Stoppe und entferne den nginx Container
21. Bemerkung: Im Kapitel Volumes werden wir sehen, wie Verzeichnisse in einen Container eingebunden werden können

### Port Mapping

1. `docker container run -d -p 8080:80 nginx:latest` startet einen nginx Container im detached Modus und verbindet den Port 8080 des Host Rechners mit dem Port 80 im nginx Container
2. Durch die Option `-p` kann explizit definiert werden welche Ports auf dem Host verwendet werden sollten
3. Übung: Starte die aktuellste (latest) Version der [Postgres Datenbank in einem Docker Container](https://hub.docker.com/_/postgres) und finde heraus welche Ports für den Zugriff auf die Datenbank verwendet werden können.
4. Lösung:
   1. `docker container run -d -P postgres:latest` führt **nicht** dazu, dass der Postgres Container gestartet wird, warum?
   2. `docker container run -P postgres:latest` liefert den Grund für das Problem, Postgres erwartet, dass entweder ein Passwort gesetzt wird oder die Environment Variable `POSTGRES_HOST_AUTH_METHOD=trust` gesetzt wird
   3. Environment Variablen für den Container werden mit der Option `-e` des `docker container run` Befehls gesetzt
   4. `docker run -P -e POSTGRES_HOST_AUTH_METHOD=trust postgres:latest` startet den Postgres Container mit ausgeschalteter Authentifizierung (dies ist natürlich nur in Demo Umgebungen sinnvoll)
   5. Ctrl-C beendet den Container
   6. Übung: Räume die fehlgeschlagenen Container wieder auf
   7. Lösung: `docker container rm [container id] [container id] [container id]`
   8. Tipp: Mit der Option `--rm` des `docker container run` Befehls werden Container automatisch nach dem Stop entfernt
   9. `docker container run -d -P --rm -e POSTGRES_HOST_AUTH_METHOD=trust postgres:latest` startet einen Postgres Container im detached Modus und mappt die Ports aus dem Container auf zufällige Ports des Hosts
   10. `docker container ls` listet unter PORTS die gemappten Ports des Postgres Containers
   11. `docker container inspect [container id | container name]` liefert die Einstellungen des Containers
   12. `docker container inspect --format '{{json .NetworkSettings.Ports}}' [container id | container name]` liefert die offenen Ports im Container, Dokumentation und Möglichkeiten von `--format` siehe [hier](https://docs.docker.com/engine/cli/formatting/)
   13. `docker stop [container id | container name]` stoppt den Container, beachte, dass der Container nach dem Stoppen direkt entfernt wurde, da beim `run` Befehl die Option `--rm` verwendet wurde
   14. `docker container run -d -p 5432:5432 --rm -e POSTGRES_HOST_AUTH_METHOD=trust postgres:latest` startet einen Postgres Container und mappt den Port 5432 des Containers auf den Port 5432 des Hosts
   15. Siehe weitere mögliche Environment Variablen für das offizielle [Postgres Docker Image auf Docker Hub](https://hub.docker.com/_/postgres)
   16. `docker container run -d -p 5432:5432 --rm --name=pgserver -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=testdb postgres:latest` startet einen Postgres Server Container mit dem Namen `pgserver` und einer Daten

### Kommunikation zwischen Container

Mit dem Postgres Client [`psql`](https://www.postgresql.org/docs/current/app-psql.html) aus einem Container auf den `pgserver` Container zugreifen.

1. `psql -h [hostname | ip] -p [port] -U [username]` ist der Befehl um auf einen Postgres Server zuzugreifen, der Client `psql` ist in dem Docker Image `postgres:latest` installiert
2. `docker container exec -i -t pgserver psql -U postgres` führt innerhalb des Postgres Server Containers den `psql` Befehl aus
3. Übung: `psql` aus einem separaten Container ausführen und auf den Postgres Server Container zugreifen
4. Ansatz 1: `d -h pgserver -U postgres` also einen separaten Container starten und den Containernamen `pgserver` als Hostname verwenden, funktioniert nicht, da der Hostname `pgserver` unbekannt ist
5. Ansatz 2: Mit `docker container exec -i -t pgserver bash` versuchen den Hostnamen oder IP Adresse zu bestimmen, `hostname` liefert die Id des Containers
6. Ansatz 3: Mit `docker container inspect pgserver` versuchen die IP Adresse zu finden, gefunden unter `.Networks.IPAddress`
7. `docker container run -i -t --rm postgres:latest psql -h [ip adresse] -U postgres` funktioniert nun und man kann sich auf den Postgres Server Container mit Hilfe eines anderen Containers verbinden
8. Besser Ansatz mit einem virtuellen Netzwerk
9. `docker container run -i -t --rm postgres:latest psqldocker network create test` erzeugt ein neues virtuelles Netzwerk für Docker Container
10. `docker network ls` listet alle existierenden Netzwerke auf
11. `docker network` listet alle Unterkommandos auf
12. `docker network connect [network] [container]` fügt einen Container in ein bestehendes Netzwerk ein
13. `docker container inspect pgserver` listet jetzt unter Networks zwei unterschiedliche Netzwerk auf, der Postgres Server hat jetzt den DNS Namen `pgserver`, also identisch zum Containernamen
14. `docker container run -i -t --rm postgres:latest psql -h pgserver -U postgres` erneuter Versuch sich mit `pgserver` als Hostname zu verbinden, nicht erfolgreich, da der Container, der gerade gestartet wurde sich nicht im gleichen Netzwerk wie `pgserver` befindet
15. `docker container run -i -t --rm --network test postgres:latest psql -h pgserver -U postgres` mit der Option `--network` klappt aber letztendlich die Verbindung
16. Insgesamt:
    1. `docker network create test` ein virtuelles Netzwerk erzeugen
    2. `docker container run -d -p 5432:5432 --rm --name=pgserver --network test -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=secret -e POSTGRES_DB=testdb postgres:latest` Postgres Server Container im Netzwerk `test` starten und automatisch eine Datenbank mit dem Namen `testdb` erzeugen
    3. `docker container run -i -t --rm --network test postgres:latest psql -h pgserver -U postgres -d testdb` Postgres Client starten im Netzwerk `test`
17. Übung: Virtuelles Netzwerk entfernen
