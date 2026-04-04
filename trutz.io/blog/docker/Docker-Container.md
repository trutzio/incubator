## Nginx Container starten

1. Der einfachste Befehl um einen Docker Container zu starten
1. Was ist ein Image?
1. `docker container run nginx` Container mit Image nginx, Ctrl-C
1. Logs

Notizen:

1. [Nginx](https://nginx.org/) ein HTTP Webserver
   ![Marktanteil von nginx](https://www.pro-linux.de/images/NB3/imgdb/servermarktanteile-im-monat-mai.jpg)

## Detached Modus

1. `docker container run -p 8080:80 -d nginx` startet den Container im Hintergrund
1. die Id des Containers wird zurückgemeldet
1. `docker container ls` listet die Container, die gerade laufen
1. die Spalte PORTS enthält das Portmapping, das mit der Option -p mitgegeben wurde

## Port Mapping

1. `docker container run -p 8080:80 nginx` Host Port 8080 wird auf den Container Port 80 gemapped
1. http://localhost:8080 im Browser aufrufen um die Startseite von nginx zu sehen, Splitfenster mit den Logs in der Console

## Container stoppen

1. `docker container stop [container id]` stoppt den Container
1. `docker container ls` listet den gestoppten Container nicht mehr auf
1. `docker container ls -a` listet alle Container auf unabhängig vom Status (Up, Exited)

## Container aufräumen

1. `docker container rm [container id]` entfernt einen Container
1. `docker container prune` entfernt alle gestoppten Container

## Aufgabe

1. Untersuche, ob ein laufender Container gestoppt werden kann, ohne ihn vorher zu stoppen

## Postgres als Container

1. `docker container run postgres` enthält eine Fehlermeldung, da eine Environment Variable nicht gesetzt wurde
1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust postgres` fährt die Datenbank hoch, die Umgebungsvariable wird innerhalb des Containers gesetzt und kann von der Initialisierungsskript der Datenbank gelesen werden, Postgres wird damit ohne Authentifizierung hochgefahren
1. Learning: mit `-e` können Umgebungsvariablen innerhalb des Containers gesetzt werden
1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres` fährt den Postgres Container im Hintergrund hoch und Port 5432 wird vom Host in den Container gemappt

## Postgres Client (Visual Studio Code)

1. Postgres Extension `cweijan.vscode-postgresql-client2` in Visual Studio Code installieren
1. Verbindung zum Docker Container aufbauen, lokaler Port 5432 wird auf den Port 5432 innerhalb des Containers gemappt
1. `CREATE TABLE person ...` Query ausführen, 2-3 Personen in die Tabelle einfügen und ein `SELECT ...` danach ausführen, man kann sehen, dass man hier eine normale Postgres DB INstant hat

## Docker Volumes

1. Ein Docker Volume ist eine anonyme Festplatte für einen Docker Container
1. `docker volume ls` listet alle virtuellen Festplatten des Systems auf
1. jeder Container erhält automatisch eine anonyme virtuelle Festplatte auf der die Dateien geschrieben werden
1. Overlay2 Dateisystem
   ![Overlay2](https://docs.docker.com/engine/storage/drivers/images/overlay_constructs.webp)
1. `docker volume prune` entfernt nicht mehr verwendete Volumes

## Postgres mit einem Volume

1. `docker create volume data` erzeugt ein Volume
1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d -v data:/var/lib/postgresql postgres` -v gibt das Volume an, das an einer bestimmten Stelle im Dateisystem gemountet wird

## Aufgabe

1. Fahre einen Postgres Container hoch mit einem gemounteten Volume unter `/var/lib/postgresql`
1. mit dem Postgres Client erzeuge eine Tabelle, füge Zeilen hinzu und mit `SELECT` die Zeilen auflisten
1. Stoppe den Container und entferne ihn mit `docker container rm`
1. Beachte, dass das Volume nicht gelöscht wird
1. Starte einen neuen Container und verwende das Volume von vorhin wieder
1. Verbinde dich mit dem Postgres Client
1. Die Tabelle, die mit dem Contaiener davor erzeugt wurde ist da, da die Daten des Postgres DB Serves auf dem Volume gespeichert wurden

## Aufgabe

1. Starte einen Postgres Container mit einem nicht-anonymen Volume und mit der Environment Variable `POSTGRES_HOST_AUTH_METHOD=trust`
1. Stoppe und entferne den Container
1. Starte nun erneut einen Postgres Container indem das selbe Volume wiederverwendet wird, aber die Environment Variable `POSTGRES_HOST_AUTH_METHOD` nicht gesetzt wird
1. Warum startet der Container?

## Container Name

1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d -v data:/var/lib/postgresql --name
pgserver postgres` --name legt den Namen eines Containers fest

## Container automatisch entfernen nach dem Stopppen

1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d -v data:/var/lib/postgresql --name
pgserver --rm postgres` --rm entfernt automatisch einen Container nachdem er gestoppt wurde
1. `docker container stop pgserver` stoppt und entfernt automatisch den Container

## Aufgabe

1. Starte einen Postgres Container mit einem anonymen Volume und mit der Option `--rm`
1. Stoppe den Container
1. Überprüfe, ob beim entfernen des Containers aus das anonyme Volume automatisch entfernt wurde

## Container Logs

1. `docker container logs pgserver` listet die Logs eines Containers auf
1. `docker container logs -f pgserver` listet dauerhaft die Logs des Containers, mit Ctrl-C erhält man die Console wieder

## Docker Exec

1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d -v data:/var/lib/postgresql --name pgserver --rm postgres` starte einen Postgres Container
1. `docker container exec pgserver ls` der exec Unterbefehl führt innerhalb eines Containers einen beliebigen Befehl aus, der natürlich im Container installiert sein muss
1. `docker container exec -it pgserver bash` starte eine Bash innerhalb des Containers, die Parameter `-it` müssen mitgegeben werden da wir mit der Bash einen interaktiven Terminal erhalten
1. `env` liefert die Environment Variablen innerhalb des Containers, man sieht hier die gesetzte `POSTGRES_HOST_AUTH_METHOD`
1. beleibige Befehle wie `ps` können innerhalb des Containers ausgeführt werden

## Postgres Client

1. `docker container run -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d -v data:/var/lib/postgresql --name pgserver --rm postgres` starte einen Postgres Container
1. `docker container exec -it pgserver bash` starte eine Bash innerhalb des Containers
1. `psql -U postgres` starte den Postgres Client, der sich mit dem Server auf localhost verbindet auf dem PG Standard Port 5432 mit dem User `postgres` (Standard PG User)
1. Führe mit der Postgres Extension die SQL Befehle aus um eine Tabelle mit Inhalten zu erzeugen

```sql
create table person (id int primary key, name varchar(255) not null, age int not null);
insert into person (id, name, age) values (1, 'Alice', 30);
insert into person (id, name, age) values (2, 'Bob', 25);
insert into person (id, name, age) values (3, 'Charlie', 35);
select * from person;
```

1. Betrachte mit `\dt` diese Tabelle nun auf dem Console mit psql
1. Führe ein `SELECT * FROM person` aus, d.h. es wurde über zwei unterschiedliche Wege eine Verbindung zum PG Server aufgebaut

## Docker Network

1. `docker network ls` listet alle virtuellen Netzwerke auf
1. `docker network create pgnet`
1. `docker network connect pgnet pgserver` verbindet den Container pgserver in das Netz pgnet
1. `docker container inspect pgserver` listet die Konfiguration des Containers pgserver
1. `docker container inspect --format "{{json .NetworkSettings.Networks}}" pgserver | jq` listet die Netzwerkeinstellungen des Containers pgserver
1. Beachte zwei unterschiedliche Netzwerke und den DNS Namen im Netzwerk pgnet

## PGAdmin4

1. `docker container run --rm dpage/pgadmin4` erster Versuch um pgadmin4 zu starten
1. Environment Variablen `PGADMIN_DEFAULT_EMAIL` und `PGADMIN_DEFAULT_PASSWORD` müssen gesetzt werden, es sind die Credentials der Admins
1. `docker container run --rm -e PGADMIN_DEFAULT_EMAIL=admin@test.de -e PGADMIN_DEFAULT_PASSWORD=secret dpage/pgadmin4` startet PgAdmin4 erfolgreich, der im Container auf Port 80 erreichbar ist, Ctrl-C und erneuter Start mit
1. `docker container run --rm -e PGADMIN_DEFAULT_EMAIL=admin@test.de -e PGADMIN_DEFAULT_PASSWORD=secret -p 8080:80 --name pgadmin -d --network pgnet dpage/pgadmin4` durch die Option `--network pgnet` wird dieser Container in das Netzwerk pgnet aufgenommen

## Aufgabe

1. Überprüfe, dass der Container pgadmin im Netzwerk pgnet enthalten ist
1. `docker container inspect --format "{{json .NetworkSettings.Networks}}" pgadmin | jq`

## Administration des Postgres Servers

1. http://localhost:8080 und mit den obigen Credentials anmelden
1. Neue Postgres Server Verbindung aufbauen mit `pgserver` als Hostname und `postgres` als Username
1. Navigiere zu den Tabellen und betrachte die Personen-Tabelle
1. Disconnect from Server

## Aufgabe

1. Entferne mit `docker network disconnect pgnet pgserver` den pgserver aus dem Netzwerk pgnet
1. Versuche über die Oberfläche die Verbindung erneut aufzubauen, funktioniert nicht
1. Wie kann man dieses Problem beheben?
1. Lösung `docker network connect pgnet pgserver`

## Aufgabe

1. Entferne alle zuvor erstellten Container, Volumes und Networks

## Docker Compose

1. Erstelle eine Datei mit dem Name `compose.yaml` und folgendem Inhalt

```yaml
services:
  pgserver:
    image: postgres
    environment:
      POSTGRES_HOST_AUTH_METHOD: trust
    volumes:
      - data:/var/lib/postgresql
    ports:
      - "5432:5432"

  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@test.de
      PGADMIN_DEFAULT_PASSWORD: secret
    ports:
      - "8080:80"
    depends_on:
      - pgserver

volumes:
  data: {}
```

1. Starte alle definierten Services / Container innerhalb dieser Datei mit einem einzigen Befehl `docker compose up -d`
1. Untersuche wie welche Resourcen erzeugt wurden (Container, Volumes, Networks)
1. `docker compose down` um alle vorhin definierten Resourcen zu entfernen
