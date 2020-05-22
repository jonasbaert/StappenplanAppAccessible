# StappenplanAppAccessible

Titel bachelorproef: Studie rond toegankelijkheid van mobiele apps voor personen met een beperking a.d.h.v. een proof-of-concept

Organisatie: Hogeschool Gent

Studierichting: Toegepaste Informatica

Academiejaar: 2019-2020

Promotor: Giselle Vercauteren

Co-promotor: Sara Daelman

Deze app voor het creëren van stappenplannen is de toegankelijke versie van een proof-of-concept app waarop personen met een beperking stappenplannen kunnen aanmaken. Er is ook een andere versie die de basisversie is, en dus minder toegankelijk dan deze versie is. De basisversie vind je door op de volgende link te klikken: https://github.com/jonasbaert/StappenplanBaseApp. 

## Opstarten van de app

Om deze app op te kunnen starten vanaf de computer heeft u volgende zaken nodig:
- Een account voor Firebase;
- Android Studio met een Android emulator.

Stappen die nodig zijn om de app op te kunnen starten:
- Maak in Firebase een nieuw project aan met een naam naar keuze;
- Klik een aantal keer op `continue` zodat het project wordt gecreëerd;
- Klik op het android-icoon boven de tekst `Add an app to get started`;
- Geef onder `Android package name` de naam `hogent.bachelor.stappenplanappaccessible` in;
- Klik op `Register`;
- Download het `google-service.json` bestand en ga dan naar Android Studio;
- Open het juiste project en kopieër het bestand op de plaats dat in een afbeelding wordt weergegeven in Firebase;
- Sla de volgende stappen over en ga naar het dashboard;
- Creëer een firestore database (`Cloud Firestore`) en een storage;
- Verander de `Rules` van de firestore database van `allow read, write: if false;` naar `allow read, write: if true;`.
- Kies in Android Studio de juiste emulator en klik vervolgens op `run`.
