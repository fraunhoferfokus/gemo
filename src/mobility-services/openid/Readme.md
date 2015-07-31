# Authentifizierungs- und Autorisierungsprovider

## Installationsanleitung

### Allgemeines
Es wird davon ausgegangen, dass Tomcat als Container verwendet wird
* Es m�ssen zwei Datenbanken existieren: Einmal die 'service' und einmal die 'token'
Innerhalb der service muss eine Tabelle users existieren. Siehe dazu SQL Script service-users.sql. (**TODO erstellen**)
* Datenbank 'token' erstellen mit Einstellungen aus settings.xml (owner:gemo)
* Tabellen authcode, accesstoken, clients und refreshtoken erstellen (**TODO SQL**)
* settings.xml aktualisieren

* aus dem Ordner openid heraus die jars aus src/libs in ein lokales repo hinzuf�gen. Daf�r folgendes Kommando verwenden:

mvn install:install-file -DlocalRepositoryPath=repo -DcreateChecksum=true -Dpackaging=jar -Dfile=src/lib/fuzzino.jar -DgroupId=fuzzino -DartifactId=fuzzino -Dversion=0.2.4


Nachdem die Datenbanken existieren und auch eingerichtet sind, kann der Dienst deployed werden

dazu im Hauptverzeichnis 'mvn clean install tomcat:redeploy' ausf�hren. 
nachdem alles deployed wurde nochmals mit 'mvn verify' alle Tests durchf�hren.


### Accesstokenvalidierung

Sende im *Header* die folgenden Werte

1. access token mit Schlüssel **access_token**
1. scope mit Schlüssel **scope**
1. username mit Schlüssel **username**

Anschließend per **POST** an `<tomcat>/openid/token/validate`.
